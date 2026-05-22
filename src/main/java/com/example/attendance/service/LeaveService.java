package com.example.attendance.service;

import com.example.attendance.model.LeaveRequest;
import com.example.attendance.model.User;
import com.example.attendance.repository.LeaveRepository;
import com.example.attendance.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private UserRepository userRepository;

    public LeaveRequest createLeaveRequest(
            LeaveRequest leaveRequest) {

        leaveRequest.setStatus("PENDING");

        return leaveRepository.save(leaveRequest);
    }

    public List<LeaveRequest> getLeaveRequestsByUser(
            Long userId) {

        return leaveRepository.findByUserId(userId);
    }

    public LeaveRequest updateLeaveStatus(
            Long leaveRequestId,
            String status) {

        LeaveRequest leaveRequest =
                leaveRepository.findById(leaveRequestId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Leave request not found"));

        leaveRequest.setStatus(status);

        return leaveRepository.save(leaveRequest);
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRepository.findAll();
    }

    public LeaveRequest getLeaveRequestById(Long leaveRequestId) {
        return leaveRepository.findById(leaveRequestId).orElse(null);
    }

    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElse(null);
    }
}