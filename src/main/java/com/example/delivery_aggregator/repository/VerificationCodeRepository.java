package com.example.delivery_aggregator.repository;

import com.example.delivery_aggregator.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, UUID> {
    @Query("SELECT vc FROM VerificationCode vc " +
            "WHERE vc.email = :email " +
            "AND vc.code = :code " +
            "AND vc.createdAt >= :cutoffTime")
    List<VerificationCode> findValidCodes(
            @Param("email") String email,
            @Param("code") String code,
            @Param("cutoffTime") LocalDateTime cutoffTime);

    @Query("SELECT CASE WHEN COUNT(vc) > 0 THEN TRUE ELSE FALSE END " +
            "FROM VerificationCode vc " +
            "WHERE vc.email = :email " +
            "AND vc.code = :code " +
            "AND vc.createdAt >= :cutoffTime")
    Boolean existsValidCode(
            @Param("email") String email,
            @Param("code") String code,
            @Param("cutoffTime") LocalDateTime cutoffTime);
}
