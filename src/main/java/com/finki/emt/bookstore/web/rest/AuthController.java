package com.finki.emt.bookstore.web.rest;

import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.security.jwt.TokenProvider;
import com.finki.emt.bookstore.service.UserService;
import com.finki.emt.bookstore.web.rest.response.JWTResponse;
import com.finki.emt.bookstore.web.rest.response.PrincipalResponse;
import com.finki.emt.bookstore.web.rest.validators.RegisterRequestValidator;
import com.finki.emt.bookstore.web.rest.vm.LoginVM;
import com.finki.emt.bookstore.web.rest.vm.RegisterVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private Logger log = LoggerFactory.getLogger(AuthController.class);

    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private AuthenticationManager authenticationManager;

    @Inject
    private UserService userService;

    @Inject
    private RegisterRequestValidator registerRequestValidator;

    @InitBinder("registerVM")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(registerRequestValidator);
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginVM loginVM) {
        log.debug("Authenticate request - {}", loginVM);
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication);
            return ResponseEntity.ok(new JWTResponse(jwt));
        } catch (AuthenticationException exp) {
            Map<String, String> msg = Collections.singletonMap("errors", exp.getLocalizedMessage());
            return new ResponseEntity<>(msg, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/register")
    public JWTResponse register(@Valid @RequestBody RegisterVM registerVM) {
        User saved = userService.create(registerVM);

        List<? extends GrantedAuthority> authorities = saved.getAuthorities().stream()
                .map(a -> new SimpleGrantedAuthority(a.getName()))
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(registerVM.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);
        return new JWTResponse(jwt);
    }

    @GetMapping(value = "/account")
    public PrincipalResponse account(Principal principal) {
        String username = principal.getName();
        Optional<User> user = userService.findByEmail(username);
        return user.map(PrincipalResponse::new)
                .orElseThrow(() -> new IllegalStateException(
                        "User with email " + username + "can't be find in the database"));
    }
}
