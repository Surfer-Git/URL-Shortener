package com.surfer.codes.url_shortener.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "authorities")
@Getter @Setter
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "authorities") // mapped by the 'authorities' field in User entity
    private Set<User> users;
}
