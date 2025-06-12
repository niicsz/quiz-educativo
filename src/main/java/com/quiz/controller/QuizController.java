package com.quiz.controller;

import com.quiz.model.*;
import com.quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QuizController {
    @Autowired
    private QuizService service;

    @PostMapping("/login")
    public User login(@RequestParam String nome, @RequestParam String email, @RequestParam String senha) {
        return service.loginOuCadastro(nome, email, senha);
    }

    @GetMapping("/perguntas")
    public List<Question> listar() {
        return service.listarPerguntas();
    }

    @PostMapping("/responder")
    public UserAnswer responder(@RequestParam Long usuarioId, @RequestParam Long perguntaId, @RequestParam String resposta) {
        return service.responderPergunta(usuarioId, perguntaId, resposta);
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
