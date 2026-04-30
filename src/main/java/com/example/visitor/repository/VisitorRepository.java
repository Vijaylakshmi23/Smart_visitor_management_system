package com.example.visitor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.visitor.entity.Visitor;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    Optional<Visitor> findByUserEmail(String email);
}
