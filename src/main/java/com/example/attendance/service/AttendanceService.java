package com.example.attendance.service;

import com.example.attendance.dto.AttendanceRecordDto;
import com.example.attendance.exception.BadRequestException;
import com.example.attendance.exception.ResourceNotFoundException;
import com.example.attendance.model.AttendanceRecord;
import com.example.attendance.model.User;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    public AttendanceRecordDto login(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (attendanceRepository.findOpenRecordByEmployee(user).isPresent()) {
            throw new BadRequestException("You already have an active login. Please logout first.");
        }

        AttendanceRecord record = new AttendanceRecord(user, LocalDateTime.now());
        AttendanceRecord saved = attendanceRepository.save(record);
        return AttendanceRecordDto.fromEntity(saved);
    }

    @Transactional
    public AttendanceRecordDto logout(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AttendanceRecord record = attendanceRepository.findOpenRecordByEmployee(user)
                .orElseThrow(() -> new BadRequestException("No active login found. Please login first."));

        record.setLogoutTime(LocalDateTime.now());
        record.setStatus("COMPLETED");
        attendanceRepository.save(record);
        return AttendanceRecordDto.fromEntity(record);
    }

    public List<AttendanceRecordDto> getMyRecords(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return attendanceRepository.findByEmployeeId(user.getId()).stream()
                .map(AttendanceRecordDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getCurrentStatus(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return attendanceRepository.findOpenRecordByEmployee(user)
                .map(record -> {
                    Map<String, Object> currentStatus = new java.util.HashMap<>();
                    currentStatus.put("isLoggedIn", true);
                    currentStatus.put("loginTime", record.getLoginTime() != null ? record.getLoginTime().toString() : null);
                    currentStatus.put("recordId", record.getId());
                    return currentStatus;
                })
                .orElseGet(() -> {
                    Map<String, Object> currentStatus = new java.util.HashMap<>();
                    currentStatus.put("isLoggedIn", false);
                    return currentStatus;
                });
    }
}
