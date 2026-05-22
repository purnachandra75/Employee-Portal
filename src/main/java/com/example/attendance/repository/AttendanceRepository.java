package com.example.attendance.repository;

import com.example.attendance.model.AttendanceRecord;
import com.example.attendance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
    @Query("SELECT a FROM AttendanceRecord a WHERE a.employee = :employee AND a.logoutTime IS NULL ORDER BY a.loginTime DESC")
    Optional<AttendanceRecord> findOpenRecordByEmployee(@Param("employee") User employee);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.employee.id = :employeeId ORDER BY a.loginTime DESC")
    List<AttendanceRecord> findByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT a FROM AttendanceRecord a ORDER BY a.loginTime DESC")
    List<AttendanceRecord> findAllOrderByLoginTime();
}
