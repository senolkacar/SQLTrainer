package com.senolkacar.sqltrainer.service;

import com.senolkacar.sqltrainer.dto.QuestionWithSolutionAnswerDTO;
import com.senolkacar.sqltrainer.dto.EvalDTO;
import com.senolkacar.sqltrainer.entity.*;
import com.senolkacar.sqltrainer.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttemptRepository attemptRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private ModelMapper modelMapper;

    public QuestionWithSolutionAnswerDTO getQuestionWithSolutionAnswer(int id, String username) {
        // Find user by username (pseudo)
        var user = userRepository.findByPseudo(username);
        if (user == null) {
            return null; // User not found
        }

        // Find question with all related data
        Optional<Question> questionOpt = questionRepository.findByIdWithDetails(id);
        if (questionOpt.isEmpty()) {
            return null; // Question not found
        }

        Question question = questionOpt.get();
        Quiz quiz = question.getQuiz();

        // Set quiz status (assuming there's a getStatus method on Quiz)
        // quiz.setStatut(quiz.getStatus(user)); // This would need to be implemented

        // Find the latest attempt for this user and quiz
        Optional<Attempt> lastAttemptOpt = attemptRepository.findLatestByStudentAndQuiz(user.get().getId(), quiz.getId());

        if (lastAttemptOpt.isEmpty()) {
            // No attempt found, return question without answer info
            return mapToDTO(question, null, null);
        }

        Attempt lastAttempt = lastAttemptOpt.get();

        // Check if user has answered this question in the latest attempt
        boolean hasAnswer = answerRepository.existsByAttemptIdAndQuestionId(lastAttempt.getId(), question.getId());
        question.setHasAnswer(hasAnswer);

        // Get the latest answer for this question in the latest attempt
        Optional<Answer> answerOpt = answerRepository.findLatestByAttemptAndQuestion(lastAttempt.getId(), question.getId());
        Answer answer = answerOpt.orElse(null);
        question.setAnswer(answer);

        // Find previous and next question IDs
        Optional<Integer> previousQuestionId = questionRepository.findPreviousQuestionId(quiz.getId(), question.getOrder());
        Optional<Integer> nextQuestionId = questionRepository.findNextQuestionId(quiz.getId(), question.getOrder());

        question.setPreviousQuestionId(previousQuestionId.orElse(null));
        question.setNextQuestionId(nextQuestionId.orElse(null));

        // Map to DTO and return
        return mapToDTO(question, previousQuestionId.orElse(null), nextQuestionId.orElse(null));
    }

    private QuestionWithSolutionAnswerDTO mapToDTO(Question question, Integer previousQuestionId, Integer nextQuestionId) {
        QuestionWithSolutionAnswerDTO dto = modelMapper.map(question, QuestionWithSolutionAnswerDTO.class);
        dto.setPreviousQuestionId(previousQuestionId);
        dto.setNextQuestionId(nextQuestionId);
        return dto;
    }

    // Placeholder methods for other functionality that might be needed
    public Query eval(EvalDTO evalDTO, String username) {
        if (evalDTO.getQuery() == null || evalDTO.getQuery().isEmpty()) {
            throw new IllegalArgumentException("The query field is required.");
            // Or return a ResponseEntity.badRequest().body("The query field is required.");
        }

        var user = userRepository.findByPseudo(username);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
            // Or return a ResponseEntity.badRequest().build();
        }

        var questionOpt = questionRepository.findByIdWithQuizAndSolutions(evalDTO.getQuestionId());
        var queryResult = questionOpt.get().eval(evalDTO.getQuery(), questionOpt.get().getQuiz().getDatabase().getName());
        boolean isCorrect = queryResult.getErrors().isEmpty();
        var attempt = attemptRepository.findLatestByStudentAndQuiz(user.get().getId(), questionOpt.get().getQuiz().getId())
                .orElseThrow(() -> new IllegalArgumentException("No attempt found for user and quiz."));
        if(attempt.getStart() == null) {
            attempt.setStart(OffsetDateTime.now());
            attemptRepository.save(attempt);
        }

        // Create and save new Answer
        Answer answer = new Answer();
        answer.setId(attempt.getId());
        answer.setQuestionId(questionOpt.get().getId());
        answer.setSql(evalDTO.getQuery());
        answer.setTimestamp(OffsetDateTime.now());
        answer.setCorrect(isCorrect);
        answerRepository.save(answer);

        return queryResult;

    }

    public Query getLastQueryResult(int id, String username) {
        // Implementation would go here
        return null;
    }
}
