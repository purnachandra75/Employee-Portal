package com.example.attendance.service;

import com.example.attendance.exception.BadRequestException;
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

        int requestedDays = calculateLeaveDays(leaveRequest.getStartDate(), leaveRequest.getEndDate());
        int alreadyApprovedDays = countApprovedLeaveDays(leaveRequest.getUser(), leaveRequest.getLeaveCategory(), null);
        int allowance = getLeaveAllowance(leaveRequest.getLeaveCategory());
        int remaining = allowance - alreadyApprovedDays;

        if (requestedDays <= 0) {
            throw new BadRequestException("Leave request must include a valid date range.");
        }

        if (requestedDays > remaining) {
            throw new BadRequestException(
                    "Cannot submit leave request. Only " + remaining + " " + leaveRequest.getLeaveCategory() + " leave day(s) are currently available.");
        }

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

        if (status == null) {
            throw new IllegalArgumentException("Leave status is required.");
        }

        if (status.equalsIgnoreCase(leaveRequest.getStatus())) {
            return leaveRequest;
        }

        if (status.equalsIgnoreCase("APPROVED")) {
            User user = leaveRequest.getUser();
            int requestedDays = calculateLeaveDays(leaveRequest.getStartDate(), leaveRequest.getEndDate());
            int alreadyApprovedDays = countApprovedLeaveDays(user, leaveRequest.getLeaveCategory(), leaveRequest.getId());
            int allowance = getLeaveAllowance(leaveRequest.getLeaveCategory());
            int remaining = allowance - alreadyApprovedDays;

            if (requestedDays > remaining) {
                throw new com.example.attendance.exception.BadRequestException(
                        "Cannot approve leave. Only " + remaining + " " + leaveRequest.getLeaveCategory() + " leave day(s) are available.");
            }
        }

        leaveRequest.setStatus(status.toUpperCase());
        return leaveRepository.save(leaveRequest);
    }

    private int calculateLeaveDays(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return (int) (java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1);
    }

    private int countApprovedLeaveDays(User user, String category, Long excludeLeaveRequestId) {
        if (user == null || category == null) {
            return 0;
        }
        return leaveRepository.findByUserId(user.getId()).stream()
                .filter(request -> request.getStatus() != null && request.getStatus().equalsIgnoreCase("APPROVED"))
                .filter(request -> request.getId() != null && !request.getId().equals(excludeLeaveRequestId))
                .filter(request -> request.getLeaveCategory() != null && request.getLeaveCategory().equalsIgnoreCase(category))
                .mapToInt(request -> calculateLeaveDays(request.getStartDate(), request.getEndDate()))
                .sum();
    }

    private int getLeaveAllowance(String category) {
        if (category == null) {
            return 0;
        }
        switch (category.toLowerCase()) {
            case "sick":
                return 6;
            case "casual":
                return 12;
            case "paid":
                return 18;
            default:
                return 0;
        }
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