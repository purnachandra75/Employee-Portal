package com.example.attendance.dto;

import com.example.attendance.model.AttendanceRecord;

import java.time.LocalDateTime;

public class AttendanceRecordDto {
    private Long id;
    private UserDto employee;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private Double workingHours;
    private String status;

    public AttendanceRecordDto() {
    }

    public AttendanceRecordDto(Long id, UserDto employee, LocalDateTime loginTime, LocalDateTime logoutTime, Double workingHours, String status) {
        this.id = id;
        this.employee = employee;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.workingHours = workingHours;
        this.status = status;
    }

    public static AttendanceRecordDto fromEntity(AttendanceRecord record) {
        return new AttendanceRecordDto(
                record.getId(),
                UserDto.fromUser(record.getEmployee()),
                record.getLoginTime(),
                record.getLogoutTime(),
                record.getWorkingHours(),
                record.getStatus()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getEmployee() {
        return employee;
    }

    public void setEmployee(UserDto employee) {
        this.employee = employee;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public LocalDateTime getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }

    public Double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Double workingHours) {
        this.workingHours = workingHours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
