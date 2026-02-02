package com.example.library.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * JPA Entity for Library Members
 * Represents a library member with membership type
 */
@Entity
@Table(name = "members")
public class MemberEntity implements Serializable, Identifiable {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipType membershipType;

    protected MemberEntity() {
    }

    public MemberEntity(String name, String email, MembershipType membershipType) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.membershipType = membershipType;
    }

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    @Override
    public String toString() {
        return String.format("MemberEntity{id='%s', name='%s', email='%s', type=%s}",
                id, name, email, membershipType);
    }
}
