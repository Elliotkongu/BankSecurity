package com.example.banksecurity.Services;

import com.example.banksecurity.Storage.Customer.Customer;
import com.example.banksecurity.Storage.Customer.CustomerRepository;
import com.example.banksecurity.Storage.Customer.SavingsAccount.SavingsAccount;
import com.example.banksecurity.Storage.Customer.SavingsAccount.SavingsAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerService {

    CustomerRepository customerRepository;
    SavingsAccountRepository savingsAccountRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, SavingsAccountRepository savingsAccountRepository) {
        this.customerRepository = customerRepository;
        this.savingsAccountRepository = savingsAccountRepository;
    }

    public Customer addCustomer(Long userId) {
        return customerRepository.save(new Customer(userId));
    }

    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok().body(customerRepository.findAll());
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
}