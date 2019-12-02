package com.example.simpleRest.repositories;

import com.example.simpleRest.entities.DbUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbUserRepository extends MongoRepository<DbUser,String> {

    DbUser findOneByUsernameIgnoreCase(String username);
}
