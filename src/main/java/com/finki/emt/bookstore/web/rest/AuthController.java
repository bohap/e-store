package com.finki.emt.bookstore.web.rest;

import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.security.Principal;
import com.finki.emt.bookstore.security.SecurityUtil;
import com.finki.emt.bookstore.security.jwt.JWTConfigurer;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private Logger log = LoggerFactory.getLogger(AuthController.class);

    @Inject
    private UserService userService;

    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private SecurityUtil securityUtil;

    @Inject
    private AuthenticationManager authenticationManager;

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
        log.debug("Register request - {}", registerVM);
        User saved = userService.create(registerVM);

        Collection<? extends GrantedAuthority> authorities =
                SecurityUtil.createAuthorities(saved.getAuthorities());
        Principal principal = new Principal(saved, authorities);
        Authentication auth = securityUtil.authenticate(principal, authorities);

        String jwt = tokenProvider.createToken(auth);
        return new JWTResponse(jwt);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/account")
    public PrincipalResponse account(@AuthenticationPrincipal Principal principal) {
        return new PrincipalResponse(principal.getUser());
    }

    @GetMapping(value = JWTConfigurer.AUTHORIZATION_REFRESH_ROUTE)
    public ResponseEntity refresh(HttpServletRequest request) {
        log.debug("Refresh token request");
        String token = tokenProvider.resolveTokenFromRequest(request);
        String newToken = tokenProvider.refreshToken(token);
        String bearer = JWTConfigurer.AUTHORIZATION_CARRIER + newToken;
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(JWTConfigurer.AUTHORIZATION_HEADER, bearer);

        return new ResponseEntity(headers, HttpStatus.OK);
    }
}
