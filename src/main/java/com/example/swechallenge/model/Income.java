package com.example.swechallenge.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "income")
public class Income {

  Income(){}

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "name", unique = true)
  private String name;
  
  @Column(name = "salary", precision=9, scale =2)
  private BigDecimal salary;
  // @Column(name = "created_at")
  // private Timestamp createdAt;
  // @Column(name = "updated_at")
  // private Timestamp updatedAt;

  // public Users(String name, BigDecimal salary, Timestamp createdAt, Timestamp
  // updatedAt) {
  public Income(String name, BigDecimal salary) {
    this.name = name;
    this.salary = salary;
    // this.createdAt = createdAt;
    // this.updatedAt = updatedAt;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getSalary() {
    return salary;
  }

  public void setSalary(BigDecimal salary) {
    this.salary = salary;
  }

  // public Timestamp getCreatedAt() {
  // return createdAt;
  // }

  // public void setCreatedAt(Timestamp createdAt) {
  // this.createdAt = createdAt;
  // }

  // public Timestamp getUpdatedAt() {
  // return updatedAt;
  // }

  // public void setUpdatedAt(Timestamp updatedAt) {
  // this.updatedAt = updatedAt;
  // }

}