package com.tom.banking.repository;

import com.tom.banking.domain.Transfer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Integer> {

    @Query(value = "SELECT t FROM Transfer t WHERE t.senderUser.username = :username OR " +
            "t.targetUser.username = :username ORDER BY t.transferID DESC")
    List<Transfer> findRecentTransfers(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO transfer(transferid, sender_user," +
            " senderIBAN, target_user, targetIBAN, transfer_amount, purpose)" +
            " VALUES (:transferID, :senderUser, :senderIBAN, :targetUser, :targetIBAN," +
            " :transferAmount, :purpose)",
            nativeQuery = true)

    void saveTransfer(@Param("transferID") int transferID, @Param("senderUser") String senderUser,
    @Param("senderIBAN") String senderIBAN, @Param("targetUser") String targetUser,
    @Param("targetIBAN") String targetIBAN, @Param("transferAmount") Double transferAmount,
    @Param("purpose") String purpose);
}
