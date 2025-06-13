package com.quiz.service;

import com.quiz.model.*;
import com.quiz.repository.*;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {
  @Autowired private UserRepository usuarioRepo;

  @Autowired private QuestionRepository perguntaRepo;

  @Autowired private UserAnswerRepository respostaRepo;

  public User loginOuCadastro(String nome, String email, String senha) {
    return usuarioRepo
        .findByEmail(email)
        .orElseGet(
            () -> {
              User u = new User();
              u.setNome(nome);
              u.setEmail(email);
              u.setSenha(senha);
              return usuarioRepo.save(u);
            });
  }

  public List<Question> listarPerguntas() {
    return perguntaRepo.findAll();
  }

  public UserAnswer responderPergunta(Long usuarioId, Long perguntaId, String resposta) {
    User usuario =
        usuarioRepo
            .findById(usuarioId)
            .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
    Question pergunta =
        perguntaRepo
            .findById(perguntaId)
            .orElseThrow(() -> new NoSuchElementException("Pergunta não encontrada"));

    boolean correta = pergunta.getCorreta().equalsIgnoreCase(resposta);
    if (correta) {
      usuario.setPontuacao(usuario.getPontuacao() + 10);
      usuarioRepo.save(usuario); // Salva o usuário apenas se a resposta estiver correta
    }

    UserAnswer respostaUsuario = new UserAnswer(null, usuario, pergunta, resposta, correta);
    return respostaRepo.save(respostaUsuario);
  }

  public int getPontuacao(Long usuarioId) {
    return usuarioRepo
        .findById(usuarioId)
        .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"))
        .getPontuacao();
  }

  public List<UserAnswer> getHistorico(Long usuarioId) {
    User u =
        usuarioRepo
            .findById(usuarioId)
            .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
    return respostaRepo.findByUsuario(u);
  }
}
