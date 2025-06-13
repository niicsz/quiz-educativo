package com.quiz.dto;

public class ResponderRequest {
  private Long usuarioId;
  private Long perguntaId;
  private String resposta;

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public Long getPerguntaId() {
    return perguntaId;
  }

  public void setPerguntaId(Long perguntaId) {
    this.perguntaId = perguntaId;
  }

  public String getResposta() {
    return resposta;
  }

  public void setResposta(String resposta) {
    this.resposta = resposta;
  }
}
