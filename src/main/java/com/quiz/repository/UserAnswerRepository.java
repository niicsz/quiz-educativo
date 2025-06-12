package com.quiz.repository;

import com.quiz.model.UserAnswer;
import com.quiz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findByUsuario(User usuario);
}
