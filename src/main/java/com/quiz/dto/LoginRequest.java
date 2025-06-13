package com.quiz.dto;

import lombok.Data;

@Data
public class LoginRequest {
  private String nome;
  private String email;
  private String senha;
}
