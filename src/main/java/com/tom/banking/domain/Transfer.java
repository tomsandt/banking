package com.tom.banking.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int transferID;

    @ManyToOne
    @JoinColumn(name = "senderUser", referencedColumnName = "username")
    public User senderUser;

    @Column(nullable = false)
    public String senderIBAN;

    @ManyToOne
    @JoinColumn(name = "targetUser", referencedColumnName = "username")
    public User targetUser;

    @Column(nullable = false)
    public String targetIBAN;

    @Column(nullable = false)
    public Double transferAmount;

    @Column(nullable = false)
    public String purpose;

    public int getTransferID() {
        return transferID;
    }

    public void setTransferID(int transferID) {
        this.transferID = transferID;
    }

    public User getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(User senderUser) {
        this.senderUser = senderUser;
    }

    public String getSenderIBAN() {
        return senderIBAN;
    }

    public void setSenderIBAN(String senderIBAN) {
        this.senderIBAN = senderIBAN;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    public String getTargetIBAN() {
        return targetIBAN;
    }

    public void setTargetIBAN(String targetIBAN) {
        this.targetIBAN = targetIBAN;
    }

    public User getTargetUsername() {
        return targetUser;
    }

    public void setTargetUsername(User targetUsername) {
        this.targetUser = targetUser;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

}
