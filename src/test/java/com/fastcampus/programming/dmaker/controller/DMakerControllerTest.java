package com.fastcampus.programming.dmaker.controller;


import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastcampus.programming.dmaker.code.StatusCode;
import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDetailDto;
import com.fastcampus.programming.dmaker.dto.EditDeveloper;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fastcampus.programming.dmaker.dto.DeveloperDto;
import com.fastcampus.programming.dmaker.service.DMakerService;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(DMakerController.class)
class DMakerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DMakerService dMakerService;

    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                    MediaType.APPLICATION_JSON.getSubtype(),
                                                    StandardCharsets.UTF_8);
    @Test
    void testGetAllDevelopers() throws Exception {
        // 모의 반환값 설정
        DeveloperDto juniorDevelperDto = DeveloperDto.builder()
                                                    .developerSkillType(DeveloperSkillType.BACK_END)
                                                    .developerLevel(DeveloperLevel.JUNIOR)
                                                    .memberId("memberId1").build();
        DeveloperDto seniorDevelperDto = DeveloperDto.builder()
                                                    .developerSkillType(DeveloperSkillType.FRONT_END)
                                                    .developerLevel(DeveloperLevel.SENIOR)
                                                    .memberId("memberId2").build();

        given(dMakerService.getAllEmployedDevelopers())
        .willReturn(Arrays.asList(juniorDevelperDto, seniorDevelperDto));


        // GET 요청 수행 및 결과 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/developers").contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].memberId").value(juniorDevelperDto.getMemberId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].developerLevel").value(juniorDevelperDto.getDeveloperLevel().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].developerSkillType").value(juniorDevelperDto.getDeveloperSkillType().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].memberId").value(seniorDevelperDto.getMemberId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].developerLevel").value(seniorDevelperDto.getDeveloperLevel().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].developerSkillType").value(seniorDevelperDto.getDeveloperSkillType().name()));
    }

    @Test
    void testGetDeveloperDetail() throws Exception {
        String memberId = "memberId";
        DeveloperDetailDto developerDetailDto = DeveloperDetailDto.builder() // 필요한 속성 설정
                .build();

        given(dMakerService.getDeveloperDetail(memberId)).willReturn(developerDetailDto);

        mockMvc.perform(get("/developers/" + memberId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.memberId").value(developerDetailDto.getMemberId()));
    }


    @Test
    void testEditDeveloper() throws Exception {
        String memberId = "memberId";
        EditDeveloper.Request request = EditDeveloper.Request.builder()
                .developerLevel(DeveloperLevel.SENIOR)
                .developerSkillType(DeveloperSkillType.BACK_END)
                .experienceYears(10)
                .build();

        DeveloperDetailDto updatedDeveloperDetail = DeveloperDetailDto.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .memberId(memberId)
                .build();

        given(dMakerService.editDeveloper(any(EditDeveloper.Request.class), eq(memberId)))
                .willReturn(updatedDeveloperDetail);

        mockMvc.perform(put("/developers/" + memberId)
                        .contentType(contentType)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.memberId").value(updatedDeveloperDetail.getMemberId()));
    }

    @Test
    void testCreateDeveloper() throws Exception {
        CreateDeveloper.Request request = new CreateDeveloper.Request(); // 요청 데이터 설정
        CreateDeveloper.Response response = new CreateDeveloper.Response(); // 응답 데이터 설정

        given(dMakerService.createDeveloper(any(CreateDeveloper.Request.class))).willReturn(response);

        mockMvc.perform(post("/developers")
                        .contentType(contentType)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteDeveloper() throws Exception {
        String memberId = "memberId";
        DeveloperDetailDto developerDetailDto = DeveloperDetailDto.builder() // 삭제된 개발자 정보 설정
                .build();

        given(dMakerService.deleteDeveloper(memberId)).willReturn(developerDetailDto);

        mockMvc.perform(delete("/developers/" + memberId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.memberId").value(developerDetailDto.getMemberId()));
    }
}
