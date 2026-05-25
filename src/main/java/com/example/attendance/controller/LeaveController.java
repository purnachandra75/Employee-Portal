package com.example.attendance.controller;

import com.example.attendance.dto.LeaveRequestDto;
import com.example.attendance.model.LeaveRequest;
import com.example.attendance.model.User;
import com.example.attendance.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping("/request")
    public ResponseEntity<LeaveRequest> createLeaveRequest(@RequestBody LeaveRequestDto leaveRequestDto) {
        User user = leaveService.getUserById(leaveRequestDto.getUserId());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setUser(user);
        leaveRequest.setLeaveCategory(leaveRequestDto.getLeaveCategory());
        leaveRequest.setStartDate(LocalDate.parse(leaveRequestDto.getStartDate()));
        leaveRequest.setEndDate(LocalDate.parse(leaveRequestDto.getEndDate()));
        leaveRequest.setReason(leaveRequestDto.getReason());

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