package com.example.swechallenge.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.swechallenge.model.Income;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, String> {

  Optional<Income> findByName(String name);

  // @Query(value = "SELECT * FROM Income WHERE salary >= ?1 AND salary <= ?2", nativeQuery = true)
  // List<Income> findByRangeSalary(BigDecimal minSalary, BigDecimal maxSalary);

  @Query(value = "SELECT * FROM Income WHERE salary >= ?1 AND salary <= ?2 limit ?3 offset ?4", nativeQuery = true)
  List<Income> findByRangeSalaryPagination(BigDecimal minSalary, BigDecimal maxSalary, Integer limit, Integer offset);

  @Query(value = "SELECT i FROM Income i WHERE salary >= ?1 AND salary <= ?2")
  List<Income> findByRangeSalaryPaginationSorting(BigDecimal minSalary, BigDecimal maxSalary, Pageable page);
}

