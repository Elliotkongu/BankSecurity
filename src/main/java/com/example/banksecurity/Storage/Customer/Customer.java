package com.example.banksecurity.Storage.Customer;

import com.example.banksecurity.Storage.Customer.SavingsAccount.SavingsAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    private Long userId;

    private BigDecimal mainAccount;

    @OneToMany(fetch = FetchType.EAGER)
    private List<SavingsAccount> savingsAccountList;

    public Customer(Long userId) {
        this.userId = userId;
        mainAccount = BigDecimal.valueOf(0);
    }

    public void addToMainAccount(BigDecimal amount) {
        mainAccount = mainAccount.add(amount);
    }
    public void subtractFromMainAccount(BigDecimal amount) {
        mainAccount = mainAccount.subtract(amount);
    }

    public BigDecimal transferToSavings(Integer savingsIndex, BigDecimal amount) {
        if (mainAccount.compareTo(amount) >= 0) {
            mainAccount = mainAccount.subtract(amount);
            savingsAccountList.get(savingsIndex).addToSavingsAccount(amount);
        } else {
            amount = mainAccount;
            savingsAccountList.get(savingsIndex).addToSavingsAccount(amount);
            mainAccount = BigDecimal.ZERO;
        }
        return amount;
    }

    public BigDecimal transferFromSavings(Integer savingsIndex, BigDecimal amount) {
        amount = savingsAccountList.get(savingsIndex).subtractFromSavingsAccount(amount);
        mainAccount = mainAccount.add(amount);
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Customer customer = (Customer) o;
        return id != null && Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
