package com.example.banksecurity.Services;

import com.example.banksecurity.DTOs.Response.RegistrationRequestDTO;
import com.example.banksecurity.Storage.Banker.Banker;
import com.example.banksecurity.Storage.Banker.BankerRepository;
import com.example.banksecurity.Storage.Customer.Customer;
import com.example.banksecurity.Storage.Customer.CustomerRepository;
import com.example.banksecurity.Storage.Customer.RegistrationRequest.RegistrationRequest;
import com.example.banksecurity.Storage.Customer.RegistrationRequest.RegistrationRequestRepository;
import com.example.banksecurity.Storage.Transaction.Transaction;
import com.example.banksecurity.Storage.Transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankerService {

    BankerRepository bankerRepository;
    RegistrationRequestRepository registrationRequestRepository;
    TransactionRepository transactionRepository;
    CustomerRepository customerRepository;

    @Autowired
    public BankerService (BankerRepository bankerRepository, RegistrationRequestRepository registrationRequestRepository,
                          TransactionRepository transactionRepository, CustomerRepository customerRepository) {
        this.bankerRepository = bankerRepository;
        this.registrationRequestRepository = registrationRequestRepository;
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    public void addBanker(Long userId) {
        bankerRepository.save(new Banker(userId));
    }

    public ResponseEntity<List<Banker>> getAllBankers() {
        return ResponseEntity.ok().body(bankerRepository.findAll());
    }

    public ResponseEntity<List<RegistrationRequestDTO>> getAllRegistrationRequests() {
        List<RegistrationRequestDTO> registrationRequestDTOS = new ArrayList<>();
        for (RegistrationRequest regReq: registrationRequestRepository.findAll()) {
            registrationRequestDTOS.add(new RegistrationRequestDTO(regReq.getId(), regReq.getFirstName(), regReq.getLastName(), regReq.getResolved()));
        }
        return ResponseEntity.ok().body(registrationRequestDTOS);
    }

    public ResponseEntity<String> handleTransaction(Long transactionId) {
        if (transactionRepository.findById(transactionId).isPresent()) {
            Transaction transaction = transactionRepository.findById(transactionId).get();
            if (!transaction.getHandled()) {
                transaction.setHandled(Boolean.TRUE);
                transactionRepository.save(transaction);
                Customer customerFrom = customerRepository.findById(transaction.getCustomerFrom()).get();
                Customer customerTo = customerRepository.findById(transaction.getCustomerTo()).get();

                customerFrom.subtractFromMainAccount(transaction.getAmount());
                customerTo.addToMainAccount(transaction.getAmount());

                customerRepository.save(customerFrom);
                customerRepository.save(customerTo);
                return ResponseEntity.ok().body("Transaction completed");
            }
            return ResponseEntity.ok().body("That transaction has already been handled");
        } else {
            return ResponseEntity.badRequest().body("Transaction not found");
        }
    }
}
