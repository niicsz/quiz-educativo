package com.quiz.dto;

import lombok.Data;

@Data
public class ResponderRequest {
  private Long usuarioId;
  private Long perguntaId;
  private String resposta;
}
