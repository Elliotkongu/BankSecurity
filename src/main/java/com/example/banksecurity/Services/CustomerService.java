package com.example.banksecurity.Services;

import com.example.banksecurity.DTOs.Request.StartTransactionDTO;
import com.example.banksecurity.DTOs.Response.CustomerDTO;
import com.example.banksecurity.Storage.Customer.Customer;
import com.example.banksecurity.Storage.Customer.CustomerRepository;
import com.example.banksecurity.Storage.Customer.SavingsAccount.SavingsAccount;
import com.example.banksecurity.Storage.Customer.SavingsAccount.SavingsAccountRepository;
import com.example.banksecurity.Storage.Message.Message;
import com.example.banksecurity.Storage.Transaction.Transaction;
import com.example.banksecurity.Storage.Transaction.TransactionRepository;
import com.example.banksecurity.Storage.User.User;
import com.example.banksecurity.Storage.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    CustomerRepository customerRepository;
    SavingsAccountRepository savingsAccountRepository;
    TransactionRepository transactionRepository;
    UserRepository userRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, SavingsAccountRepository savingsAccountRepository,
                           TransactionRepository transactionRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.savingsAccountRepository = savingsAccountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public void addCustomer(Long userId) {
        customerRepository.save(new Customer(userId));
    }

    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customerDTOs = new ArrayList<>();

        for (Customer customer: customerRepository.findAll()) {
            if (userRepository.findById(customer.getUserId()).isPresent()) {
                User user = userRepository.findById(customer.getUserId()).get();
                customerDTOs.add(new CustomerDTO(customer.getId(), customer.getFirstName(), customer.getLastName(),
                        customer.getUserId(), user.getUsername(), customer.getMainAccount(), customer.getSavingsAccountList()));
            }
        }
        return ResponseEntity.ok().body(customerDTOs);
    }

    public ResponseEntity<String> addToMainAccount(Long id, Double amount) {
        if (customerRepository.findById(id).isPresent()) {
            Customer customer = customerRepository.findById(id).get();
            customer.addToMainAccount(BigDecimal.valueOf(amount));
            customerRepository.save(customer);
            return ResponseEntity.ok().body(amount + " successfully added to account");
        } else {
            return ResponseEntity.badRequest().body("Customer not found");
        }
    }

    public ResponseEntity<String> subtractFromMainAccount(Long id, Double amount) {
        if (customerRepository.findById(id).isPresent()) {
            Customer customer = customerRepository.findById(id).get();
            customer.subtractFromMainAccount(BigDecimal.valueOf(amount));
            customerRepository.save(customer);
            return ResponseEntity.ok().body(amount + " successfully removed from account");
        } else {
            return ResponseEntity.badRequest().body("Customer not found");
        }
    }

    public ResponseEntity<String> addNewSavingsAccount(Long id) {
        if (customerRepository.findById(id).isPresent()) {
            Customer customer = customerRepository.findById(id).get();
            SavingsAccount savingsAccount = new SavingsAccount();
            savingsAccount.setCustomerId(customer.getId());
            savingsAccountRepository.save(savingsAccount);
            customer.getSavingsAccountList().add(savingsAccount);
            customerRepository.save(customer);
            return ResponseEntity.ok().body("Successfully added new savings account");
        } else {
            return ResponseEntity.badRequest().body("Custoemr not found");
        }
    }

    public ResponseEntity<?> getAllSavingsAccounts(Long id) {
        if (customerRepository.findById(id).isPresent()) {
            return ResponseEntity.ok().body(customerRepository.findById(id).get().getSavingsAccountList());
        } else {
            return ResponseEntity.badRequest().body("Customer not found");
        }
    }

    public ResponseEntity<String> transferToSavings(Long id, Integer savingsIndex, BigDecimal amount) {
        if (customerRepository.findById(id).isPresent()) {
            Customer customer = customerRepository.findById(id).get();

            if (customer.getSavingsAccountList().size() >= savingsIndex) {
                BigDecimal amountTransferred = customer.transferToSavings(savingsIndex - 1, amount);
                savingsAccountRepository.save(customer.getSavingsAccountList().get(savingsIndex - 1));
                customerRepository.save(customer);
                if (amountTransferred.compareTo(amount) == 0) {
                    return ResponseEntity.ok().body("Successfully transferred " + amount.toString() + " from main account to savings " + savingsIndex);
                } else {
                    return ResponseEntity.ok().body("Successfully transferred " + amountTransferred + " from main account to savings " + savingsIndex);
                }
            } else {
                return ResponseEntity.badRequest().body("Customer does not have that many savings accounts");
            }
        }
        return ResponseEntity.badRequest().body("Customer not found");
    }

    public ResponseEntity<String> transferFromSavings(Long id, Integer savingsIndex, BigDecimal amount) {
        if (customerRepository.findById(id).isPresent()) {
            Customer customer = customerRepository.findById(id).get();

            if (customer.getSavingsAccountList().size() >= savingsIndex) {
                BigDecimal amountTransferred = customer.transferFromSavings(savingsIndex - 1, amount);
                savingsAccountRepository.save(customer.getSavingsAccountList().get(savingsIndex - 1));
                customerRepository.save(customer);
                if (amountTransferred.compareTo(amount) == 0) {
                    return ResponseEntity.ok().body("Successfully transferred " + amount.toString() + " from savings " + savingsIndex + " to main account");
                } else {
                    return ResponseEntity.ok().body("Successfully transferred " + amountTransferred + " from savings " + savingsIndex + " main account");
                }
            } else {
                return ResponseEntity.badRequest().body("Customer does not have that many savings accounts");
            }
        }
        return ResponseEntity.badRequest().body("Customer not found");
    }

    public ResponseEntity<String> startTransaction(StartTransactionDTO startTransactionDTO) {
        if (customerRepository.findById(startTransactionDTO.getCustomerFrom()).isPresent()) {
            if (customerRepository.findById(startTransactionDTO.getCustomerTo()).isPresent()) {

                Transaction transaction = new Transaction(startTransactionDTO.getCustomerFrom(),
                        startTransactionDTO.getCustomerTo(), startTransactionDTO.getAmount());
                transactionRepository.save(transaction);
                return ResponseEntity.ok().body("Transaction started, waiting for banker to accept");
            } else {
                return ResponseEntity.badRequest().body("Receiver customer not found");
            }
        } else {
            return ResponseEntity.badRequest().body("Sender customer not found");
        }
    }

    public ResponseEntity<List<Transaction>> getAllTransactionsByCustomerId(Long id) {
        List<Transaction> returnList = new ArrayList<>();

        for (Transaction transaction : transactionRepository.findAll()) {
            if (transaction.getCustomerFrom().equals(id) || transaction.getCustomerTo().equals(id)) {
                returnList.add(transaction);
            }
        }
        return ResponseEntity.ok().body(returnList);
    }


}
