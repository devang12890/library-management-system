package com.example.library.repository;

import com.example.library.model.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<LoanEntity, String> {
    List<LoanEntity> findByMemberId(String memberId);
    List<LoanEntity> findByBookId(String bookId);
    List<LoanEntity> findByMemberIdAndReturnDateIsNull(String memberId);
}
