package com.quiz.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.model.Question;
import com.quiz.repository.QuestionRepository;
import java.io.InputStream;
import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class PerguntaDataLoader {

  @Bean
  CommandLineRunner loadData(QuestionRepository repository) {
    return args -> {
      ObjectMapper mapper = new ObjectMapper();
      InputStream inputStream = new ClassPathResource("Question.json").getInputStream();

      Question[] perguntas = mapper.readValue(inputStream, Question[].class);
      repository.saveAll(Arrays.asList(perguntas));
    };
  }
}
