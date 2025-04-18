package com.andhraempower.service;

import com.andhraempower.dto.VillageProjectExpenseResponse;
import com.andhraempower.entity.Finance;
import com.andhraempower.exception.RequiredFieldMissingException;
import com.andhraempower.repository.FinanceRepository;
import com.andhraempower.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class FinanceService {

    @Autowired
    private FinanceRepository financeRepository;

    @Autowired
    private ProjectRepository villageProjectRepository;


    public Finance addTransaction(Finance finance) {

        if (finance.getVillageProjectId() == null) {
            throw new RequiredFieldMissingException("VillageProjectId is mandatory.");
        }
        return financeRepository.save(finance);
    }

    public Finance updateFinanceById(Integer id, Finance updatedFinance, MultipartFile multipartFile) throws IOException {

        Finance existingFinance = financeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Finance record not found with id: " + id));

        if (updatedFinance.getVillageProjectId() != null) {
            existingFinance.setVillageProjectId(updatedFinance.getVillageProjectId());
        }
        if (updatedFinance.getExpenseCategoryId() != null) {
            existingFinance.setExpenseCategoryId(updatedFinance.getExpenseCategoryId());
        }
        if (updatedFinance.getTransactionAmount() != null) {
            existingFinance.setTransactionAmount(updatedFinance.getTransactionAmount());
        }
        if (updatedFinance.getPaymentMode() != null) {
            existingFinance.setPaymentMode(updatedFinance.getPaymentMode());
        }
        if (updatedFinance.getTransactionDate() != null) {
            existingFinance.setTransactionDate(updatedFinance.getTransactionDate());
        }
        if (updatedFinance.getCommitteeId() != null) {
            existingFinance.setCommitteeId(updatedFinance.getCommitteeId());
        }
        if (updatedFinance.getPaidTo() != null) {
            existingFinance.setPaidTo(updatedFinance.getPaidTo());
        }
        if (updatedFinance.getExpenseType() != null) {
            existingFinance.setExpenseType(updatedFinance.getExpenseType());
        }
        if (updatedFinance.getDescription() != null) {
            existingFinance.setDescription(updatedFinance.getDescription());
        }
        if (updatedFinance.getBillProofs() != null) {
            existingFinance.setBillProofs(updatedFinance.getBillProofs());
        }
        if (updatedFinance.getSpentBy() != null) {
            existingFinance.setSpentBy(updatedFinance.getSpentBy());
        }
        if (updatedFinance.getApprovedBy() != null) {
            existingFinance.setApprovedBy(updatedFinance.getApprovedBy());
        }
        if (multipartFile != null && !multipartFile.isEmpty()) {
            existingFinance.setBillImage(multipartFile.getBytes());
        }

        return financeRepository.save(existingFinance);
    }

    public List<Finance> getTransaction() {
        return financeRepository.findAll();
    }

    public VillageProjectExpenseResponse getTransactionsForProject(Long projectId) {
        List<Finance> transactions = financeRepository.findByVillageProjectId(projectId);
        Double totalExpenses = transactions.stream().mapToDouble(Finance::getTransactionAmount).sum();
        Double totalFunds = villageProjectRepository.findById(projectId).map(p->p.getProjectEstimation()).orElse(0.0);
        Double remainingBalance = totalFunds - totalExpenses;
        VillageProjectExpenseResponse response = new VillageProjectExpenseResponse();
        response.setTotalExpenses(totalExpenses);
        response.setTotalFunds(totalFunds);
        response.setRemainingBalance(remainingBalance);
        response.setTransactions(transactions);
        return response;
    }

    @Transactional
    public void removeTransactionFromProject(Long transactionId, Long projectId) {
        financeRepository.deleteByIdAndVillageProjectId(transactionId, projectId);
    }
}
