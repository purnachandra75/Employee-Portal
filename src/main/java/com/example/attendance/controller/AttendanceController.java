package com.example.attendance.controller;

import com.example.attendance.dto.AttendanceRecordDto;
import com.example.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/login")
    public ResponseEntity<AttendanceRecordDto> login(@RequestParam String username) {
        AttendanceRecordDto record = attendanceService.login(username);
        return ResponseEntity.ok(record);
    }

    @PostMapping("/logout")
    public ResponseEntity<AttendanceRecordDto> logout(@RequestParam String username) {
        AttendanceRecordDto record = attendanceService.logout(username);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/my-records")
    public ResponseEntity<List<AttendanceRecordDto>> getMyRecords(@RequestParam String username) {
        return ResponseEntity.ok(attendanceService.getMyRecords(username));
    }

    @GetMapping("/current-status")
    public ResponseEntity<Map<String, Object>> getCurrentStatus(@RequestParam String username) {
        return ResponseEntity.ok(attendanceService.getCurrentStatus(username));
    }
}
