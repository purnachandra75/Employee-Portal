package com.example.attendance.controller;

import com.example.attendance.model.AttendanceRecord;
import com.example.attendance.model.User;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.DocumentSubmissionRepository;
import com.example.attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentSubmissionRepository documentSubmissionRepository;

    @GetMapping("/all-records")
    public ResponseEntity<?> getAllRecords() {
        List<AttendanceRecord> records = attendanceRepository.findAllOrderByLoginTime();
        List<Map<String, Object>> response = records.stream()
                .map(this::toAttendanceRecordDto)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{employeeId}/records")
    public ResponseEntity<?> getEmployeeRecords(@PathVariable Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<AttendanceRecord> records = attendanceRepository.findByEmployeeId(employeeId);
        List<Map<String, Object>> recordDtos = records.stream()
                .map(this::toAttendanceRecordDto)
                .toList();

        Map<String, Object> employeeDto = new HashMap<>();
        employeeDto.put("id", employee.getId());
        employeeDto.put("username", employee.getUsername());
        employeeDto.put("email", employee.getEmail());
        employeeDto.put("fullName", employee.getFullName());

        Map<String, Object> response = new HashMap<>();
        response.put("employee", employeeDto);
        response.put("records", recordDtos);
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> toAttendanceRecordDto(AttendanceRecord record) {
        User employee = record.getEmployee();
        Map<String, Object> employeeDto = new HashMap<>();
        employeeDto.put("id", employee != null ? employee.getId() : null);
        employeeDto.put("username", employee != null ? employee.getUsername() : null);
        employeeDto.put("email", employee != null ? employee.getEmail() : null);
        employeeDto.put("fullName", employee != null ? employee.getFullName() : null);

        Map<String, Object> dto = new HashMap<>();
        dto.put("id", record.getId());
        dto.put("employee", employeeDto);
        dto.put("loginTime", record.getLoginTime());
        dto.put("logoutTime", record.getLogoutTime());
        dto.put("workingHours", record.getWorkingHours());
        dto.put("status", record.getStatus());
        return dto;
    }

    @GetMapping("/all-employees")
    public ResponseEntity<?> getAllEmployees() {
        List<User> employees = userRepository.findAll();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/all-employees-with-doc-status")
    public ResponseEntity<?> getAllEmployeesWithDocStatus() {
        List<User> employees = userRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();

        for (User employee : employees) {
            boolean hasSubmission = documentSubmissionRepository.findByUserId(employee.getId()).isPresent();
            response.add(Map.of(
                    "id", employee.getId(),
                    "username", employee.getUsername(),
                    "fullName", employee.getFullName(),
                    "email", employee.getEmail(),
                    "documentStatus", hasSubmission ? "Submitted" : "Not submitted"
            ));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics() {
        List<AttendanceRecord> allRecords = attendanceRepository.findAllOrderByLoginTime();

        long totalRecords = allRecords.size();
        long completedRecords = allRecords.stream()
                .filter(r -> "COMPLETED".equals(r.getStatus()))
                .count();
        double averageWorkingHours = allRecords.stream()
                .filter(r -> r.getWorkingHours() != null && "COMPLETED".equals(r.getStatus()))
                .mapToDouble(AttendanceRecord::getWorkingHours)
                .average()
                .orElse(0.0);

        return ResponseEntity.ok(Map.of(
                "totalRecords", totalRecords,
                "completedRecords", completedRecords,
                "inProgressRecords", totalRecords - completedRecords,
                "averageWorkingHours", String.format("%.2f", averageWorkingHours)
        ));
    }
}
