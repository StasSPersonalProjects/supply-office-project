package com.supplyoffice.controller;

import com.supplyoffice.dto.AuthenticationRequest;
import com.supplyoffice.dto.AuthenticationResponse;
import com.supplyoffice.dto.RegisterRequest;
import com.supplyoffice.entities.Role;
import com.supplyoffice.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    static Logger LOG = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/validate/{token}")
    public ResponseEntity<Boolean> validateToken(@PathVariable String token) {
        boolean response = authenticationService.validate(token);
        if (response) {
            LOG.debug("Validated!");
            return ResponseEntity.ok().body(true);
        } else {
            LOG.debug("Not Validated!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
    }

    @GetMapping("/role")
    public ResponseEntity<String> getUserRole(@RequestParam(value = "token") String token) {
        String response = authenticationService.getRole(token);
        try {
            Role role = Role.valueOf(response);
            return ResponseEntity.ok().body(role.name());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh")
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }
}
