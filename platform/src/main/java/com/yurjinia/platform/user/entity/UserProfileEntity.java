package com.yurjinia.platform.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profile")
public class UserProfileEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column
    private String avatarId;

    @Column(unique = true, nullable = false)
    private String username;

    @OneToOne(mappedBy = "userProfile")
    private UserEntity user;

}