package com.example.simpleRest.controllers;

import com.example.simpleRest.entities.DbUser;
import com.example.simpleRest.repositories.DbUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Resource(name="authenticationManager")
    private AuthenticationManager authManager;
    @Autowired
    DbUserRepository dbUserRepository;

    @PostMapping("/login")
    private ResponseEntity<DbUser> login(@RequestBody DbUser user, HttpServletRequest req) {
        DbUser dbUser;
        try {
            dbUser = dbUserRepository.findOneByUsernameIgnoreCase(user.getUsername());
            if (dbUser == null) {
                throw new UsernameNotFoundException("User not found by name: " + user.getUsername());
            }

            UsernamePasswordAuthenticationToken authReq
                    = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication auth = authManager.authenticate(authReq);

            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            HttpSession session = req.getSession(true);
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
        }catch (BadCredentialsException err) {
            throw new BadCredentialsException("Bad credentials");
        }
        return new ResponseEntity<>(dbUser,HttpStatus.OK);
    }

    @GetMapping("/logout")
    private ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
            }
            } catch (Exception err) {}
        return new ResponseEntity<>("Logged out",HttpStatus.RESET_CONTENT);
    }
}
