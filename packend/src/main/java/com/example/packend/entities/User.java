package com.example.packend.entities;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Setter
@Entity(name = "user")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String username;
    // TODO: Adjust datatype for password
    @Column
    private String password;

    @Builder
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}