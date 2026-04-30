package com.example.visitor.service;

import java.util.List;

import com.example.visitor.entity.Visitor;

public interface VisitorService {

    Visitor addVisitor(Visitor visitor);

    List<Visitor> getAllVisitors();

    Visitor getVisitorById(Long id);
}
