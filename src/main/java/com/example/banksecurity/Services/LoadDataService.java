package com.example.banksecurity.Services;

import com.example.banksecurity.Storage.Admin.Admin;
import com.example.banksecurity.Storage.Admin.AdminRepository;
import com.example.banksecurity.Storage.Customer.CustomerRepository;
import com.example.banksecurity.Storage.Message.MessageType.EMessageType;
import com.example.banksecurity.Storage.Message.MessageType.MessageType;
import com.example.banksecurity.Storage.Message.MessageType.MessageTypeRepository;
import com.example.banksecurity.Storage.User.Role.ERole;
import com.example.banksecurity.Storage.User.Role.Role;
import com.example.banksecurity.Storage.User.Role.RoleRepository;
import com.example.banksecurity.Storage.User.User;
import com.example.banksecurity.Storage.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class LoadDataService implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CustomerService customerService;
    @Autowired
    BankerService bankerService;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    MessageTypeRepository messageTypeRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName(ERole.ROLE_CUSTOMER).isEmpty()) {
            roleRepository.save(new Role(ERole.ROLE_CUSTOMER));
        }
        if (roleRepository.findByName(ERole.ROLE_BANKER).isEmpty()) {
            roleRepository.save(new Role(ERole.ROLE_BANKER));
        }
        if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }

        roleRepository.findAll().forEach(role -> System.out.println(role.getName()));

        if (messageTypeRepository.findByMessageType(EMessageType.FEEDBACK).isEmpty()) {
            messageTypeRepository.save(new MessageType(EMessageType.FEEDBACK));
        }
        if (messageTypeRepository.findByMessageType(EMessageType.SUPPORT).isEmpty()) {
            messageTypeRepository.save(new MessageType(EMessageType.SUPPORT));
        }
        if (messageTypeRepository.findByMessageType(EMessageType.ADMIN_REPLY).isEmpty()) {
            messageTypeRepository.save(new MessageType(EMessageType.ADMIN_REPLY));
        }

        messageTypeRepository.findAll().forEach(messageType -> System.out.println(messageType.getMessageType()));

        if (userRepository.findAll().isEmpty()) {
            registerCustomer();
            registerBanker();
            registerAdmin();
        }
    }

    void registerCustomer() {
        User user = new User("customer", passwordEncoder.encode("password"));
        Set<Role> role = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Error: Customer Role not found"));
        role.add(userRole);
        user.setRole(role);
        userRepository.save(user);
        customerService.addCustomer(user.getId());
    }

    void registerBanker() {
        User user = new User("banker", passwordEncoder.encode("password"));
        Set<Role> role = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_BANKER)
                .orElseThrow(() -> new RuntimeException("Error: Banker Role not found"));
        role.add(userRole);
        user.setRole(role);
        userRepository.save(user);
        bankerService.addBanker(user.getId());
    }

    void registerAdmin() {
        User user = new User("admin", passwordEncoder.encode("password"));
        Set<Role> role = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Admin role not found"));
        role.add(userRole);
        user.setRole(role);
        userRepository.save(user);
        adminRepository.save(new Admin(user.getId()));
    }
}
