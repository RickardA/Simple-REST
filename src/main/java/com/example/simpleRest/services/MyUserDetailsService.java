package com.example.simpleRest.services;

import com.example.simpleRest.entities.DbUser;
import com.example.simpleRest.repositories.DbUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Configuration
public class MyUserDetailsService implements UserDetailsService {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public BCryptPasswordEncoder getEncoder() { return encoder; }

    @Autowired
    private DbUserRepository repository;


    @PostConstruct
    private void createDefaultUsers(){
        if (repository.findOneByUsernameIgnoreCase("user") == null) {
            addUser("user", "password", Arrays.asList("USER"));
            addUser("userAdmin", "password", Arrays.asList("USER","ADMIN"));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DbUser user = repository.findOneByUsernameIgnoreCase(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found by name: " + username);
        }
        return toUserDetails(user);
    }

    public void addUser(String name, String password, List<String> userRoles){
        DbUser u = new DbUser(name, encoder.encode(password),userRoles);
        try {
            repository.save(u);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private UserDetails toUserDetails(DbUser user) {
        return User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getUserRoles().toArray(String[]::new)).build();
    }

    public static UserDetails getCurrentUserDetails(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            return principal instanceof UserDetails ? (UserDetails) principal : null;
        }
        return null;

    }
}