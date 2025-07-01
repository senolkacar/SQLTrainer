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
            student.setUsername("student");
            student.setPassword(passwordEncoder.encode("password"));
            student.setEmail("student@sqltrainer.com");
            student.setRole(User.Role.STUDENT);
            userRepository.save(student);

            // Teacher user
            User teacher = new User();
            teacher.setUsername("teacher");
            teacher.setPassword(passwordEncoder.encode("password"));
            teacher.setEmail("teacher@sqltrainer.com");
            teacher.setRole(User.Role.TEACHER);
            userRepository.save(teacher);

            // Admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setEmail("admin@sqltrainer.com");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);

            System.out.println("Sample users created:");
            System.out.println("Student: username=student, password=password");
            System.out.println("Teacher: username=teacher, password=password");
            System.out.println("Admin: username=admin, password=password");
        }
    }
}
