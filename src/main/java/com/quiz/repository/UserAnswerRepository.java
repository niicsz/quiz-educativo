package com.quiz.repository;

import com.quiz.model.User;
import com.quiz.model.UserAnswer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
  List<UserAnswer> findByUsuario(User usuario);
}
