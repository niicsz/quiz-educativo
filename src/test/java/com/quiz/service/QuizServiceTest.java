package com.quiz.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.quiz.model.Question;
import com.quiz.model.User;
import com.quiz.model.UserAnswer;
import com.quiz.repository.QuestionRepository;
import com.quiz.repository.UserAnswerRepository;
import com.quiz.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class QuizServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private QuestionRepository questionRepository;

  @Mock
  private UserAnswerRepository
      userAnswerRepository; // Reintroduzido para corrigir o problema de null

  @InjectMocks private QuizService quizService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testLoginOuCadastro_UserExists() {
    String email = "test@example.com";
    String nome = "Test User";
    String senha = "password";

    User existingUser = new User();
    existingUser.setEmail(email);

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

    User result = quizService.loginOuCadastro(nome, email, senha);

    assertNotNull(result);
    assertEquals(existingUser, result);
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void testLoginOuCadastro_UserDoesNotExist() {
    String email = "newuser@example.com";
    String nome = "New User";
    String senha = "password";

    User newUser = new User();
    newUser.setEmail(email);
    newUser.setNome(nome);
    newUser.setSenha(senha);

    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
    when(userRepository.save(any(User.class))).thenReturn(newUser); // Configuração para evitar null

    User result = quizService.loginOuCadastro(nome, email, senha);

    assertNotNull(result);
    assertEquals(email, result.getEmail());
    assertEquals(nome, result.getNome());
    verify(userRepository).save(any(User.class));
  }

  @Test
  void testListarPerguntas() {
    when(questionRepository.findAll())
        .thenReturn(
            Arrays.asList(new Question(), new Question())); // Substituído List.of por Arrays.asList

    List<Question> result = quizService.listarPerguntas();

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(questionRepository).findAll();
  }

  @Test
  void testResponderPergunta_CorrectAnswer() {
    Long userId = 1L;
    Long questionId = 1L;
    String resposta = "correta";

    User user = new User();
    user.setId(userId);
    user.setPontuacao(0);

    Question question = new Question();
    question.setId(questionId);
    question.setCorreta("correta");

    UserAnswer userAnswer = new UserAnswer();
    userAnswer.setCorreta(true);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
    when(userAnswerRepository.save(any(UserAnswer.class)))
        .thenReturn(new UserAnswer()); // Configuração para evitar null

    UserAnswer result = quizService.responderPergunta(userId, questionId, resposta);

    assertNotNull(result);
    assertTrue(user.getPontuacao() > 0);
    verify(userRepository).save(user);
    verify(userAnswerRepository).save(any(UserAnswer.class));
  }

  @Test
  void testResponderPergunta_WrongAnswer() {
    Long userId = 1L;
    Long questionId = 1L;
    String resposta = "errada";

    User user = new User();
    user.setId(userId);
    user.setPontuacao(0);

    Question question = new Question();
    question.setId(questionId);
    question.setCorreta("correta");

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
    when(userAnswerRepository.save(any(UserAnswer.class)))
        .thenReturn(new UserAnswer()); // Configuração para evitar null no caso de resposta errada

    UserAnswer result = quizService.responderPergunta(userId, questionId, resposta);

    assertNotNull(result);
    assertEquals(0, user.getPontuacao());
    verify(userRepository, never()).save(user);
  }
}
