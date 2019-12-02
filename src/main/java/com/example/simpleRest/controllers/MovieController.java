package com.example.simpleRest.controllers;

import com.example.simpleRest.entities.Movie;
import com.example.simpleRest.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping
    private ResponseEntity<List<Movie>> getMovies(){
        try{
            List<Movie> result = movieRepository.findAll();
            if (result.size() > 0){
                return new ResponseEntity<>(result, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }catch (Exception err){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<Movie> getMovie(@PathVariable("id") String id){
        try {
            Optional searchResult = movieRepository.findById(id);
            if (searchResult.isPresent()){
                return new ResponseEntity<>((Movie)searchResult.get(),HttpStatus.OK);
            }else{
                throw new ResponseStatusException(HttpStatus.NO_CONTENT,"Could not find movie with this id");
            }
        }catch (Exception err){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    private ResponseEntity<Movie> createMovie(@RequestBody Movie movie){
        try {
            return new ResponseEntity<>(movieRepository.save(movie),HttpStatus.CREATED);
        }catch (Exception err){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    private ResponseEntity<Movie> updateMovie(@RequestBody Movie movie){
        try{
            if (movieRepository.findById(movie.getId()).isPresent()){
                return new ResponseEntity<>(movieRepository.save(movie),HttpStatus.OK);
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Could not update no existing movie");
            }
        }catch (Exception err){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity deleteMovie(@PathVariable String id){
        try{
            movieRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception err){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
