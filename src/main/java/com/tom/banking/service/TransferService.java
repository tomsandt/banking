package com.tom.banking.service;

import com.tom.banking.domain.Transfer;
import com.tom.banking.domain.TransferDTO;
import com.tom.banking.repository.TransferRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    public List<Transfer> findRecentTransfers(HttpSession session) {

        String loggedUsername = (String) session.getAttribute("username");

        return transferRepository.findRecentTransfers(loggedUsername);
    }

    public void createTransfer(TransferDTO transferDTO) {

        Transfer newTransfer = new Transfer();

        newTransfer.setSenderUser(transferDTO.getSenderUser());
        newTransfer.setSenderIBAN(transferDTO.getSenderIBAN());
        newTransfer.setTargetUser(transferDTO.getTargetUser());
        newTransfer.setTargetIBAN(transferDTO.getTargetIBAN());
        newTransfer.setTransferAmount(transferDTO.getTransferAmount());
        newTransfer.setPurpose(transferDTO.getPurpose());

        transferRepository.saveTransfer(
                newTransfer.getTransferID(),
                newTransfer.getSenderUser().getUsername(),
                newTransfer.getSenderIBAN(),
                newTransfer.getTargetUser().getUsername(),
                newTransfer.getTargetIBAN(),
                newTransfer.getTransferAmount(),
                newTransfer.getPurpose());

    }

}