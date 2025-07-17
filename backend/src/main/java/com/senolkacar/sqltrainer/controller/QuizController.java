package com.senolkacar.sqltrainer.controller;

import com.senolkacar.sqltrainer.dto.BasicQuizDTO;
import com.senolkacar.sqltrainer.dto.QuizDTO;
import com.senolkacar.sqltrainer.dto.QuizWithAttemptsAndDBDTO;
import com.senolkacar.sqltrainer.dto.QuizWithDBDTO;
import com.senolkacar.sqltrainer.entity.*;
import com.senolkacar.sqltrainer.repository.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizRepository quizRepository;
    private final ModelMapper modelMapper;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AttemptRepository attemptRepository;

    @Autowired
    private Validator validator;

    @Autowired
    public QuizController(QuizRepository quizRepository, ModelMapper modelMapper) {
        this.quizRepository = quizRepository;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/all")
    public List<QuizDTO> getAllQuizzes() {
        return quizRepository.findAllWithDatabaseAndAttempts()
                .stream()
                .map(quiz -> modelMapper.map(quiz, QuizDTO.class))
                .collect(Collectors.toList());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/tests")
    public List<QuizWithAttemptsAndDBDTO> getTests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<Quiz> databasesWithQuizzes = quizRepository.findAllWithDatabaseAndAttempts()
            .stream()
            .filter(Quiz::isTest)
            .toList();

        for (Quiz q : databasesWithQuizzes) {
            q.setStatut(q.getStatus(user));
            q.setEvaluation(q.getEvaluation(user));
        }

        return databasesWithQuizzes.stream()
            .map(quiz -> modelMapper.map(quiz, QuizWithAttemptsAndDBDTO.class))
            .collect(Collectors.toList());
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/trainings")
    public List<QuizWithAttemptsAndDBDTO> getTrainings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<Quiz> databasesWithQuizzes = quizRepository.findAllWithDatabaseAndAttemptsForQuizzes()
            .stream()
            .filter(quiz -> !quiz.isTest())
            .toList();
        for( Quiz q : databasesWithQuizzes) {
            q.setStatut(q.getStatus(user));
            q.setEvaluation(q.getEvaluation(user));
        }
        return databasesWithQuizzes.stream()
            .map(quiz -> modelMapper.map(quiz, QuizWithAttemptsAndDBDTO.class))
            .collect(Collectors.toList());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getFirstQuestionId/{id}")
    public ResponseEntity<Integer> getFirstQuestionId(@PathVariable Long id) {
        Question question = questionRepository.findFirstByQuizId(id);
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getQuizById/{id}")
    public QuizWithAttemptsAndDBDTO getQuizById(@PathVariable Long id) {
        return quizRepository.findAllWithDatabaseAndAttempts()
                 .stream()
                 .filter(quiz -> quiz.getId().equals(id))
                .findFirst()
                 .map(quiz -> modelMapper.map(quiz, QuizWithAttemptsAndDBDTO.class))
                 .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/closeQuiz")
    public ResponseEntity<Void> closeQuiz(@RequestBody BasicQuizDTO basicQuizDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String pseudo = authentication.getName();
        User user = userRepository.findByPseudo(pseudo).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        Quiz quiz = quizRepository.findByIdWithAttempts(basicQuizDTO.getId());
        if (quiz == null) {
            return ResponseEntity.notFound().build();
        }
        // Find last attempt for this user
        var attempt = quiz.getAttempts().stream()
            .filter(a -> a.getStudent().getId().equals(user.getId()))
            .reduce((first, second) -> second).orElse(null);
        if (attempt == null) {
            return ResponseEntity.badRequest().build();
        }
        attempt.setFinish(java.time.OffsetDateTime.now());
        var answers = answerRepository.findAllByAttemptId(attempt.getId());
        var questions = questionRepository.findAllByQuizId(quiz.getId());
        for (var q : questions) {
            boolean hasAnswer = answers.stream().anyMatch(a -> a.getQuestionId() == q.getId());
            if (!hasAnswer) {
                Answer answer = new Answer();
                answer.setAttempt(attempt);
                answer.setQuestionId(q.getId());
                answer.setSql("");
                answer.setCorrect(false);
                answerRepository.save(answer);
            }
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/createAttempt")
    public ResponseEntity<Void> createAttempt(@RequestBody BasicQuizDTO basicQuizDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String pseudo = authentication.getName();
        User user = userRepository.findByPseudo(pseudo).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        Quiz quiz = quizRepository.findByIdWithAttempts(basicQuizDTO.getId());
        if (quiz == null) {
            return ResponseEntity.notFound().build();
        }
        Attempt attempt = new Attempt();
        attempt.getStudent().setId(user.getId());
        attempt.getQuiz().setId(quiz.getId());
        attemptRepository.save(attempt);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/quizNameExists/{name}")
    public ResponseEntity<Boolean> quizNameExists(@PathVariable String name) {
        boolean exists = quizRepository.existsByName(name);
        return ResponseEntity.ok(exists);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping
    public ResponseEntity<?> putQuiz(@RequestBody QuizWithAttemptsAndDBDTO quizDTO) {
        // Find the existing quiz with all related entities
        Quiz quiz = quizRepository.findById(quizDTO.getId())
                .orElse(null);

        if (quiz == null) {
            return ResponseEntity.notFound().build();
        }

        // Map the DTO to the existing entity
        modelMapper.map(quizDTO, quiz);

        // Validate the quiz
        Set<ConstraintViolation<Quiz>> quizViolations = validator.validate(quiz);
        if (!quizViolations.isEmpty()) {
            return ResponseEntity.badRequest().body(buildValidationErrors(quizViolations));
        }

        // Validate questions and their solutions
        for (Question question : quiz.getQuestions()) {
            Set<ConstraintViolation<Question>> questionViolations = validator.validate(question);
            if (!questionViolations.isEmpty()) {
                return ResponseEntity.badRequest().body(buildValidationErrors(questionViolations));
            }

            for (Solution solution : question.getSolutions()) {
                Set<ConstraintViolation<Solution>> solutionViolations = validator.validate(solution);
                if (!solutionViolations.isEmpty()) {
                    return ResponseEntity.badRequest().body(buildValidationErrors(solutionViolations));
                }
            }
        }

        quiz = quizRepository.save(quiz);
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> buildValidationErrors(Set<? extends ConstraintViolation<?>> violations) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("message", "Validation failed");
        errors.put("errors", violations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList()));
        return errors;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/createNewQuiz")
    public ResponseEntity<?> createNewQuiz(@RequestBody QuizWithDBDTO quizDTO) {
        // Map the DTO to a new Quiz entity
        Quiz quiz = modelMapper.map(quizDTO, Quiz.class);

        // Validate the quiz
        Set<ConstraintViolation<Quiz>> quizViolations = validator.validate(quiz);
        if (!quizViolations.isEmpty()) {
            return ResponseEntity.badRequest().body(buildValidationErrors(quizViolations));
        }

        // Validate questions and their solutions
        for (Question question : quiz.getQuestions()) {
            Set<ConstraintViolation<Question>> questionViolations = validator.validate(question);
            if (!questionViolations.isEmpty()) {
                return ResponseEntity.badRequest().body(buildValidationErrors(questionViolations));
            }

            for (Solution solution : question.getSolutions()) {
                Set<ConstraintViolation<Solution>> solutionViolations = validator.validate(solution);
                if (!solutionViolations.isEmpty()) {
                    return ResponseEntity.badRequest().body(buildValidationErrors(solutionViolations));
                }
            }
        }

        // Save the new quiz
        quizRepository.save(quiz);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Integer id) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if (quiz == null) {
            return ResponseEntity.notFound().build();
        }

        // Delete all related questions, solutions, and attempts
        questionRepository.deleteAll(quiz.getQuestions());
        attemptRepository.deleteAll(quiz.getAttempts());
        quizRepository.delete(quiz);

        return ResponseEntity.noContent().build();
    }
}
