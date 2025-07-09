package com.senolkacar.sqltrainer.service;

import com.senolkacar.sqltrainer.dto.AuthenticationRequest;
import com.senolkacar.sqltrainer.dto.AuthenticationResponse;
import com.senolkacar.sqltrainer.entity.User;
import com.senolkacar.sqltrainer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPseudo(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByPseudo(request.getPseudo())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getPseudo());
        String token = jwtService.generateToken(userDetails);

        return new AuthenticationResponse(
                token,
                user.getPseudo(),
                user.getEmail(),
                user.getRole(),
                user.getId()
        );
    }
}
