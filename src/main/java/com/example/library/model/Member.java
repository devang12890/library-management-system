package com.example.library.model;

import com.example.library.fine.FineCalculator;
import com.example.library.fine.PremiumFineCalculator;
import com.example.library.fine.StandardFineCalculator;

// Inherits from User and composes a FineCalculator (Strategy pattern)
public class Member extends User {
    private MembershipType membershipType;
    private FineCalculator fineCalculator;

    public Member(String name, String email, MembershipType membershipType) {
        super(name, email);
        this.membershipType = membershipType;
        this.fineCalculator = createFineCalculator(membershipType);
    }

    private FineCalculator createFineCalculator(MembershipType type) {
        switch (type) {
            case PREMIUM:
                return new PremiumFineCalculator();
            case REGULAR:
            default:
                return new StandardFineCalculator();
        }
    }

    public MembershipType getMembershipType() { return membershipType; }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
        this.fineCalculator = createFineCalculator(membershipType);
    }

    public FineCalculator getFineCalculator() { return fineCalculator; }

    @Override
    public String getRoleDescription() {
        return "Library member (" + membershipType + ")";
    }
}