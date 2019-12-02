package com.example.simpleRest.controllers;

import com.example.simpleRest.entities.DbUser;
import com.example.simpleRest.repositories.DbUserRepository;
import com.example.simpleRest.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController{

        @Autowired
        private DbUserRepository repository;
        @Autowired
        private MyUserDetailsService myUserDetailsService;

        @PutMapping
        private ResponseEntity<DbUser> updateMovie(@RequestBody DbUser user){
            try{
                if (repository.findById(user.getId()).isPresent()){
                    return new ResponseEntity<>(repository.save(user),HttpStatus.OK);
                }else{
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Could not update non existing user");
                }
            }catch (Exception err){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
}
