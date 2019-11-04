package com.shiro.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "sys_users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String salt;
    @Column(nullable = false)
    private Byte locked;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
