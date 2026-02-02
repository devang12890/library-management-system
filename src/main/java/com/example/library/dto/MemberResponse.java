package com.example.library.dto;

import com.example.library.model.MemberEntity;
import com.example.library.model.MembershipType;

/**
 * DTO for member responses
 */
public class MemberResponse {
    private String id;
    private String name;
    private String email;
    private MembershipType membershipType;

    public MemberResponse() {
    }

    public MemberResponse(MemberEntity member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.membershipType = member.getMembershipType();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
