package com.fastcampus.programming.dmaker.controller;

import com.fastcampus.programming.dmaker.dto.*;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.service.DMakerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(DMakerException.class)
    public DMakerErrorResponse handleException(
            DMakerException e,
            HttpServletRequest request
    ){
        log.error("errorCode: {}, url: {}, message {}",
                e.getDMakerErrorCode(), request.getRequestURI(), e.getDetailMessage());

        return DMakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }
}
