package com.example.banksecurity.Services;

import com.example.banksecurity.DTOs.Request.CustomerRegistrationDTO;
import com.example.banksecurity.Storage.Customer.Customer;
import com.example.banksecurity.Storage.Customer.CustomerRepository;
import com.example.banksecurity.Storage.Customer.RegistrationRequest.RegistrationRequest;
import com.example.banksecurity.Storage.Customer.RegistrationRequest.RegistrationRequestRepository;
import com.example.banksecurity.Storage.User.User;
import com.example.banksecurity.Storage.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    RegistrationRequestRepository registrationRequestRepository;
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    CustomerRepository customerRepository;

    @Autowired
    public RegistrationService(RegistrationRequestRepository registrationRequestRepository, PasswordEncoder passwordEncoder,
                               UserRepository userRepository, CustomerRepository customerRepository) {
        this.registrationRequestRepository = registrationRequestRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    public ResponseEntity<String> sendRegistrationRequest(CustomerRegistrationDTO customerRegistrationDTO) {
        RegistrationRequest registrationRequest = new RegistrationRequest(customerRegistrationDTO.getFirstName(),
                                                                        customerRegistrationDTO.getLastName(),
                                                                        customerRegistrationDTO.getUsername(),
                                                                        passwordEncoder.encode(customerRegistrationDTO.getPassword()));
        registrationRequestRepository.save(registrationRequest);
        return ResponseEntity.ok().body("Registration request sent in, please wait until a banker opens your account");
    }

    public ResponseEntity<String> openCustomerAccount(Long registrationReguestId) {
        if (registrationRequestRepository.findById(registrationReguestId).isPresent()) {
            RegistrationRequest registrationRequest = registrationRequestRepository.findById(registrationReguestId).get();
            User user = new User(registrationRequest.getUsername(), registrationRequest.getPassword());
            userRepository.save(user);
            Customer customer = new Customer(user.getId());
            customer.setFirstName(registrationRequest.getFirstName());
            customer.setLastName(registrationRequest.getLastName());
            customerRepository.save(customer);
            registrationRequest.setResolved(true);


            registrationRequestRepository.save(registrationRequest);
            return ResponseEntity.ok().body("Customer account successfully opened");
        }
        return ResponseEntity.badRequest().body("Registration request not found");
    }
}
