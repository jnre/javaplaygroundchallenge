package com.example.swechallenge.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.swechallenge.model.Income;
import com.example.swechallenge.repository.IncomeRepository;

@Service
public class CSVService {
  @Autowired
  IncomeRepository repository;

  public void save(MultipartFile file) {
    try {
      List<Income> incomes = CSVHelper.csvToIncomes(file.getInputStream());
      for (Income income : incomes) {
        if (income.getSalary().intValue() < 0)
          continue;
        Optional<Income> incomeData = repository.findByName(income.getName());
        if (incomeData.isPresent()) {
          Income _income = incomeData.get();
          _income.setName(income.getName());
          _income.setSalary(income.getSalary());
          repository.save(_income);
        } else {
          repository.save(income);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }

  public List<Income> getAllIncomes() {
    return repository.findAll();
  }
}
