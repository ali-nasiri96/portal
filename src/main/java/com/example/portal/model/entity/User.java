package com.example.portal.model.entity;

import com.example.portal.model.dto.internal.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "PORTAL_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String emailAddress;
    @Column
    private String passwordHash;
    @Column
    private String passwordSalt;
    @Enumerated(EnumType.ORDINAL)
    private UserType userType;
    @Column
    private Integer unsuccessfulLoginCount;
    @Column
    private Long lastUnsuccessfulLoginDate;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "USER_BOOK",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id")})
    private Set<Book> books;
}
