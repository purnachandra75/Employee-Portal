package com.example.attendance.controller;

import com.example.attendance.model.LeaveRequest;
import com.example.attendance.model.User;
import com.example.attendance.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping("/request")
    public ResponseEntity<LeaveRequest> createLeaveRequest(@RequestParam Long userId, @RequestBody LeaveRequest leaveRequest) {
        // Fetch the user by userId
        User user = leaveService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body(null); // Not Found if user does not exist
        }

        // Associate the user with the leave request
        leaveRequest.setUser(user);

        // Save the leave request
        LeaveRequest createdLeaveRequest = leaveService.createLeaveRequest(leaveRequest);
        return ResponseEntity.ok(createdLeaveRequest);
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveService.getAllLeaveRequests());
    }

    @GetMapping("/{leaveRequestId}")
    public ResponseEntity<LeaveRequest> getLeaveRequestById(@PathVariable Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveService.getLeaveRequestById(leaveRequestId);
        if (leaveRequest == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(leaveRequest);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LeaveRequest>> getLeaveRequestsByUser(@PathVariable Long userId) {
        List<LeaveRequest> leaveRequests = leaveService.getLeaveRequestsByUser(userId);
        return ResponseEntity.ok(leaveRequests);
    }

    @PutMapping("/{leaveRequestId}/status")
    public ResponseEntity<LeaveRequest> updateLeaveStatus(@PathVariable Long leaveRequestId, @RequestParam String status) {
        LeaveRequest updatedLeaveRequest = leaveService.updateLeaveStatus(leaveRequestId, status);
        return ResponseEntity.ok(updatedLeaveRequest);
    }
}