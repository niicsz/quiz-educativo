package com.quiz.controller;

import com.quiz.model.*;
import com.quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class QuizController {
    @Autowired
    private QuizService service;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/quiz")
    public String startQuiz(Model model, HttpSession session) {
        List<Question> questions = service.listarPerguntas();
        session.setAttribute("questions", questions);
        session.setAttribute("currentQuestion", 0);
        session.setAttribute("correctAnswers", 0);

        if (!questions.isEmpty()) {
            model.addAttribute("question", questions.get(0));
            model.addAttribute("currentQuestionNumber", 1);
            model.addAttribute("totalQuestions", questions.size());
            return "quiz";
        }
        return "redirect:/";
    }

    @PostMapping("/quiz/submit")
    public String submitAnswer(@RequestParam Long questionId,
                             @RequestParam String answer,
                             Model model,
                             HttpSession session) {
        List<Question> questions = (List<Question>) session.getAttribute("questions");
        if (questions == null || questions.isEmpty()) {
            return "redirect:/quiz";
        }

        Integer currentQuestion = (Integer) session.getAttribute("currentQuestion");
        if (currentQuestion == null) {
            currentQuestion = 0;
        }

        Integer correctAnswers = (Integer) session.getAttribute("correctAnswers");
        if (correctAnswers == null) {
            correctAnswers = 0;
        }

        // Verifica a resposta atual
        Question current = questions.get(currentQuestion);
        if (current.getCorreta().equals(answer)) {
            correctAnswers++;
            session.setAttribute("correctAnswers", correctAnswers);
        }

        // Avança para a próxima questão
        currentQuestion++;
        session.setAttribute("currentQuestion", currentQuestion);

        // Verifica se chegou ao fim do quiz
        if (currentQuestion >= questions.size()) {
            model.addAttribute("showResults", true);
            model.addAttribute("correctAnswers", correctAnswers);
            model.addAttribute("totalQuestions", questions.size());
            return "quiz";
        }

        // Prepara a próxima questão
        Question nextQuestion = questions.get(currentQuestion);
        model.addAttribute("question", nextQuestion);
        model.addAttribute("currentQuestionNumber", currentQuestion + 1);
        model.addAttribute("totalQuestions", questions.size());

        return "quiz";
    }

    // API endpoints mantidos para compatibilidade
    @RestController
    @RequestMapping("/api")
    public class ApiController {
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
}
