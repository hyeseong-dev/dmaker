package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDetailDto;
import com.fastcampus.programming.dmaker.dto.DeveloperDto;
import com.fastcampus.programming.dmaker.dto.EditDeveloper;
import com.fastcampus.programming.dmaker.service.DMakerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class DMakerController {

    private final DMakerService dMakerService;

    private DMakerController(DMakerService dMakerService){
        this.dMakerService = dMakerService;
    }

    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers(){
        // GET /developers HTTP/1.1
        log.info("GET /developers HTTP/1.1");
        return dMakerService.getAllEmployedDevelopers();
    }

    @GetMapping("/developers/{memberId}")
    public DeveloperDetailDto getDeveloperDetail(@PathVariable String memberId){
        // GET /developers/{memeberId} HTTP/1.1

        log.info(String.format("GET /developers/%s HTTP/1.1", memberId));
        return dMakerService.getDeveloperDetail(memberId);
    }

    @PutMapping("/developers/{memberId}")
    public DeveloperDetailDto editDeveloper(
            @PathVariable String memberId,
            @Valid @RequestBody EditDeveloper.Request request
    ){
        // PUT /developers/{memberId} HTTP/1.1
        log.info(String.format("PUT /developers/%s HTTP/1.1", memberId));
        return dMakerService.editDeveloper(request, memberId);
    }

    @PostMapping("/developers")
    public CreateDeveloper.Response createDeveloper(
            @Valid @RequestBody CreateDeveloper.Request request
    ){
        // POST /developers HTTP/1.1
        log.info("request : " + request);

        return dMakerService.createDeveloper(request);
    }

    @DeleteMapping("/developers/{memberId}")
    public DeveloperDetailDto deleteDeveloper(@PathVariable String memberId){
        // DELETE /developers HTTP/1.1
        log.info(String.format("DELETE /developers/%s HTTP/1.1", memberId));

        return dMakerService.deleteDeveloper(memberId);
    }
}
