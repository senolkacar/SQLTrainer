package com.senolkacar.sqltrainer.service;

import com.senolkacar.sqltrainer.entity.User;
import com.senolkacar.sqltrainer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create sample users for testing
        if (userRepository.count() == 0) {
            // Student user
            User student = new User();
            student.setPseudo("student");
            student.setPassword(passwordEncoder.encode("password"));
            student.setEmail("student@sqltrainer.com");
            student.setRole(User.Role.Student);
            userRepository.save(student);

            // Teacher user
            User teacher = new User();
            teacher.setPseudo("teacher");
            teacher.setPassword(passwordEncoder.encode("password"));
            teacher.setEmail("teacher@sqltrainer.com");
            teacher.setRole(User.Role.Teacher);
            userRepository.save(teacher);

            // Admin user
            User admin = new User();
            admin.setPseudo("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setEmail("admin@sqltrainer.com");
            admin.setRole(User.Role.Admin);
            userRepository.save(admin);

            System.out.println("Sample users created:");
            System.out.println("Student: pseudo=student, password=password");
            System.out.println("Teacher: pseudo=teacher, password=password");
            System.out.println("Admin: pseudo=admin, password=password");
        }
    }
}
