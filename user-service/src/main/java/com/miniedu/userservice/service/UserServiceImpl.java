package com.miniedu.userservice.service;

import com.miniedu.model.kafka.events.PasswordResetEvent;
import com.miniedu.model.kafka.events.UserRegisteredEvent;
import com.miniedu.securitylib.model.JwtTokenData;
import com.miniedu.securitylib.service.JwtService;
import com.miniedu.userservice.dto.request.LoginRequest;
import com.miniedu.userservice.dto.request.PasswordChangeRequest;
import com.miniedu.userservice.dto.request.RegisterUserRequest;
import com.miniedu.userservice.dto.response.AuthResponse;
import com.miniedu.userservice.dto.response.MessageResponse;
import com.miniedu.userservice.dto.response.UserResponse;
import com.miniedu.userservice.entity.User;
import com.miniedu.userservice.entity.UserRole;
import com.miniedu.userservice.error.handlers.UserErrorHandler;
import com.miniedu.userservice.exception.EmailAlreadyExistsException;
import com.miniedu.userservice.exception.UserNotExistException;
import com.miniedu.userservice.kafka.service.KafkaProducerService;
import com.miniedu.userservice.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

import static com.miniedu.userservice.util.UserConstants.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtService jwtService, PasswordEncoder encoder, KafkaProducerService kafkaProducerService) {
        this.repository = userRepository;
        this.jwtService = jwtService;
        this.encoder = encoder;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public MessageResponse register(RegisterUserRequest request) {
        if (emailExists(request.email())) {
            throw new EmailAlreadyExistsException(ERROR_EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .createdAt(LocalDateTime.now())
                .isActive(false)
                .role(UserRole.USER)
                .build();

        User registeredUser = null;

        try {
            registeredUser = repository.save(user);
        } catch (DataIntegrityViolationException ex) {
            UserErrorHandler.handle(ex);
        }


        assert registeredUser != null;
        String token = jwtService.generateVerificationToken(new JwtTokenData(registeredUser.getEmail(), Map.of(CLAIM_USER_ID, registeredUser.getId())));

        UserRegisteredEvent event = new UserRegisteredEvent(
                registeredUser.getEmail(),
                registeredUser.getId().toString(),
                token
        );

        kafkaProducerService.sendEmailEvent(event);

        return new MessageResponse(MSG_USER_REGISTERED);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }

    @Override
    public AuthResponse activateUser(String token) {
        if (jwtService.isTokenExpired(token)) {
            throw new JwtException("The provided token has expired");
        }

        String email = jwtService.extractSubjectFromToken(token);
        User user = getUser(email);

        if (!user.isActive()) {
            user.setActive(Boolean.TRUE);
            user = repository.save(user);
        }

        Map<String, Object> claims = Map.of(
                CLAIM_USERNAME, user.getUsername(),
                CLAIM_ROLE, user.getRole().name(),
                CLAIM_EMAIL, user.getEmail()
        );
        String loginToken = jwtService.generateToken(new JwtTokenData(user.getEmail(), claims));

        return new AuthResponse(loginToken, UserResponse.map(user));
    }

    @Override
    public MessageResponse requestUserPasswordChange(PasswordChangeRequest request) {
        User user = getUser(request.email());

        String token = jwtService.generateToken(new JwtTokenData(user.getEmail(), Map.of(CLAIM_USER_ID, user.getId())));

        PasswordResetEvent event = new PasswordResetEvent(
                user.getEmail(),
                user.getId().toString(),
                token
        );

        kafkaProducerService.sendEmailEvent(event);

        return new MessageResponse(USER_PASSWORD_REQUEST_CHANGED);
    }

    @Override
    public AuthResponse reactivateUser(String email) {
        User user = getUser(email);

        if (!user.isActive()) {
            user.setActive(Boolean.TRUE);
            user = repository.save(user);
        }

        Map<String, Object> claims = Map.of(
                CLAIM_USERNAME, user.getUsername(),
                CLAIM_ROLE, user.getRole().name(),
                CLAIM_EMAIL, user.getEmail()
        );
        String loginToken = jwtService.generateToken(new JwtTokenData(user.getEmail(), claims));

        return new AuthResponse(loginToken, UserResponse.map(user));
    }

    private User getUser(String email) {
        User user = repository.findByEmail(email).orElseThrow(() -> new UserNotExistException(ERROR_USER_NOT_EXIST));
        return user;
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email.toLowerCase()).isPresent();
    }
}
