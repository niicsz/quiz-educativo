package com.quiz.controller;

import com.quiz.dto.LoginRequest;
import com.quiz.dto.ResponderRequest;
import com.quiz.model.*;
import com.quiz.service.QuizService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class QuizController {
  @Autowired private QuizService service;

  @PostMapping("/login")
  public User login(@RequestBody LoginRequest loginRequest) {
    return service.loginOuCadastro(
        loginRequest.getNome(), loginRequest.getEmail(), loginRequest.getSenha());
  }

  @GetMapping("/perguntas")
  public List<Question> listar() {
    return service.listarPerguntas();
  }

  @PostMapping("/responder")
  public UserAnswer responder(@RequestBody ResponderRequest responderRequest) {
    return service.responderPergunta(
        responderRequest.getUsuarioId(),
        responderRequest.getPerguntaId(),
        responderRequest.getResposta());
  }

  @GetMapping("/pontuacao/{usuarioId}")
  public int pontuacao(@PathVariable Long usuarioId) {
    return service.getPontuacao(usuarioId);
  }

  @GetMapping("/historico/{usuarioId}")
  public List<UserAnswer> historico(@PathVariable Long usuarioId) {
    return service.getHistorico(usuarioId);
  }
}
