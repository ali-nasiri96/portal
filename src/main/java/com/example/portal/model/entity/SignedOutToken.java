package com.example.portal.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter
@Table(name = "SIGNED_OUT_TOKEN")
public class SignedOutToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column
    private String tokenId;
    @Column
    private Long tokenIssuedDate;
    @Column
    private Long signedOutDate;
}
