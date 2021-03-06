package com.example.banksecurity.Storage.User;

import com.example.banksecurity.Storage.Message.Message;
import com.example.banksecurity.Storage.User.Role.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> role = new HashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Message> inbox = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
