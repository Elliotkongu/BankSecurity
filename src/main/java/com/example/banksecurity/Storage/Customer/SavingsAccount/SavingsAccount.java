package com.example.banksecurity.Storage.Customer.SavingsAccount;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
public class SavingsAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long customerId;
    private BigDecimal account;

    public SavingsAccount() {
        account = BigDecimal.valueOf(0);
    }

    public void addToSavingsAccount(BigDecimal amount) {
        account = account.add(amount);
    }
    public BigDecimal subtractFromSavingsAccount(BigDecimal amount) {
        if (account.compareTo(amount) >= 0) {
            account = account.subtract(amount);
        } else {
            amount = account;
            account = BigDecimal.ZERO;
        }
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SavingsAccount that = (SavingsAccount) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
