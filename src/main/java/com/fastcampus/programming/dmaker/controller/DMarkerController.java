package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.service.DMakerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class DMarkerController {

    private final DMakerService dMakerService;

    private DMarkerController(DMakerService dMakerService){
        this.dMakerService = dMakerService;
    }

    @GetMapping("/developers")
    public List<String> getAllDevelopers(){
        // GET /developers HTTP/1.1
        log.info("GET /developers HTTP/1.1");
        return Arrays.asList("snow", "elsa", "oulaf");
    }

    @PostMapping("/developers")
    public List<String> createDeveloper(
            @Valid @RequestBody CreateDeveloper.Request request
    ){
        // POST /developers HTTP/1.1
        log.info("request : " + request);
        dMakerService.createDeveloper(request);

        return Collections.singletonList("Olaf");
    }
}
