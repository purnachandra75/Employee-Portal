package com.example.attendance.controller;

import com.example.attendance.model.AttendanceRecord;
import com.example.attendance.model.User;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.UserRepository;
import com.example.attendance.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<?> login(HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        if (username == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (attendanceRepository.findOpenRecordByEmployee(user).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "You already have an active login. Please logout first."));
        }

        AttendanceRecord record = new AttendanceRecord(user, LocalDateTime.now());
        attendanceRepository.save(record);

        return ResponseEntity.ok(Map.of(
                "message", "Login recorded successfully.",
                "loginTime", record.getLoginTime().toString(),
                "recordId", record.getId()
        ));
    }

    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        if (username == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return attendanceRepository.findOpenRecordByEmployee(user)
                .map(record -> {
                    record.setLogoutTime(LocalDateTime.now());
                    record.setStatus("COMPLETED");
                    attendanceRepository.save(record);

                    Map<String, Object> result = new HashMap<>();
                    result.put("message", "Logout recorded successfully.");
                    result.put("logoutTime", record.getLogoutTime().toString());
                    result.put("workingHours", String.format("%.2f", record.getWorkingHours()));
                    return ResponseEntity.ok(result);
                })
                .orElseGet(() -> ResponseEntity.badRequest().body(Map.of("error", "No active login found. Please login first.")));
    }

    @GetMapping("/my-records")
    public ResponseEntity<?> getMyRecords(HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        if (username == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<AttendanceRecord> records = attendanceRepository.findByEmployeeId(user.getId());
        return ResponseEntity.ok(records);
    }

    @GetMapping("/current-status")
    public ResponseEntity<?> getCurrentStatus(HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        if (username == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return attendanceRepository.findOpenRecordByEmployee(user)
                .map(record -> ResponseEntity.ok(Map.of(
                        "isLoggedIn", true,
                        "loginTime", record.getLoginTime().toString(),
                        "recordId", record.getId()
                )))
                .orElseGet(() -> ResponseEntity.ok(Map.of(
                        "isLoggedIn", false
                )));
    }

    private String getUsernameFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return null;
        }
        String token = bearerToken.substring(7);
        if (!tokenProvider.validateToken(token)) {
            return null;
        }
        return tokenProvider.getUsernameFromToken(token);
    }
}
