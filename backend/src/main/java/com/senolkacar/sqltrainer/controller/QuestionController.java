package com.senolkacar.sqltrainer.controller;

import com.senolkacar.sqltrainer.dto.EvalDTO;
import com.senolkacar.sqltrainer.dto.QuestionWithSolutionAnswerDTO;
import com.senolkacar.sqltrainer.entity.Query;
import com.senolkacar.sqltrainer.service.QuestionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;
    private final ModelMapper modelMapper;

    @Autowired
    public QuestionController(QuestionService questionService, ModelMapper modelMapper) {
        this.questionService = questionService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<QuestionWithSolutionAnswerDTO> getQuestion(@PathVariable int id, Principal principal) {
        var dto = questionService.getQuestionWithSolutionAnswer(id, principal.getName());
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/eval")
    public ResponseEntity<Query> eval(@RequestBody EvalDTO evalDTO, Principal principal) {
        if (evalDTO.getQuery() == null || evalDTO.getQuery().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        var result = questionService.eval(evalDTO, principal.getName());
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getQuery/{id}")
    public ResponseEntity<Query> getQuery(@PathVariable int id, Principal principal) {
        var result = questionService.getLastQueryResult(id, principal.getName());
        if (result == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable int id) {
        boolean deleted = questionService.deleteQuestion(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/solution/{id}")
    public ResponseEntity<Void> deleteSolution(@PathVariable int id) {
        boolean deleted = questionService.deleteSolution(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }
}