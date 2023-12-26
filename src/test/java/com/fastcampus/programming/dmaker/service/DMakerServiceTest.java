package com.fastcampus.programming.dmaker.service;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.dto.DeveloperDetailDto;
import com.fastcampus.programming.dmaker.dto.DeveloperDto;
import com.fastcampus.programming.dmaker.dto.EditDeveloper;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.repository.RetiredDeveloperRepository;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {

    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;

    @InjectMocks
    private DMakerService dMakerService;

    @Test
    void testGetDeveloperDetail() {
        // setup mock behavior
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(Developer.builder()
                        .developerLevel(DeveloperLevel.SENIOR)
                        .developerSkillType(DeveloperSkillType.FRONT_END)
                        .experienceYears(12)
                        .name("name")
                        .age(12)
                        .build()));

        // test the method
        DeveloperDetailDto developerDetailDto = dMakerService.getDeveloperDetail("memberId");

        // assert results
        assertEquals(DeveloperLevel.SENIOR, developerDetailDto.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, developerDetailDto.getDeveloperSkillType());
        assertEquals(12, developerDetailDto.getExperienceYears());
        assertEquals("name", developerDetailDto.getName());
        assertEquals(12, developerDetailDto.getAge());
    }

    @Test
    void testCreateDeveloper() {
        // 테스트를 위한 CreateDeveloper.Request 객체 생성
        // 여기서는 개발자 레벨, 기술 타입, 경력, 멤버 ID를 설정
        CreateDeveloper.Request request = CreateDeveloper.Request.builder()
                .developerLevel(DeveloperLevel.SENIOR)
                .developerSkillType(DeveloperSkillType.BACK_END)
                .experienceYears(15)
                .memberId("memberId")
                .build();

        // Mockito를 사용하여 developerRepository의 findByMemberId 메서드가 호출될 때
        // 항상 Optional.empty()를 반환하도록 설정
        // 즉, 해당 멤버 ID를 가진 개발자가 없음을 의미
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());

        // Mockito를 사용하여 developerRepository의 save 메서드가 호출될 때
        // 입력된 Developer 객체를 그대로 반환하도록 설정
        given(developerRepository.save(any(Developer.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // DMakerService의 createDeveloper 메서드를 호출하여
        // 개발자 생성 요청을 처리하고 응답 객체를 받음
        CreateDeveloper.Response response = dMakerService.createDeveloper(request);

        assertNotNull(response);                                                    // 반환된 response 객체가 null이 아닌지 검증
        assertEquals(DeveloperLevel.SENIOR, response.getDeveloperLevel());          // 반환된 response 객체의 개발자 레벨이 요청한 레벨(선임 개발자)과 일치하는지 검증
        assertEquals(DeveloperSkillType.BACK_END, response.getDeveloperSkillType());// 반환된 response 객체의 기술 타입이 요청한 타입(백엔드)과 일치하는지 검증
        assertEquals(request.getExperienceYears(), response.getExperienceYears());  // 반환된 response 객체의 경력이 요청한 경력(15년)과 일치하는지 검증
        assertEquals(request.getMemberId(), response.getMemberId());                // 반환된 response 객체의 멤버 ID가 요청한 ID("memberId")와 일치하는지 검증

    }

    @Test
    void testGetAllEmployedDevelopers(){
        // Mock 데이터 설정
        given(developerRepository.findDevelopersByStatusCodeEquals(any()))
                .willReturn(List.of(Developer.builder()
                                .developerLevel(DeveloperLevel.SENIOR)
                                .developerSkillType(DeveloperSkillType.BACK_END)
                                .experienceYears(10)
                                .memberId("Developer1")
                                .age(30)
                                .build()));
        // 메서드 실행
        List<DeveloperDto> developerDtos = dMakerService.getAllEmployedDevelopers();

        // 결과 검증
        assertNotNull(developerDtos);
        assertFalse(developerDtos.isEmpty());
        assertEquals(1, developerDtos.size());
        assertEquals(DeveloperLevel.SENIOR, developerDtos.get(0).getDeveloperLevel());
        assertEquals(DeveloperSkillType.BACK_END, developerDtos.get(0).getDeveloperSkillType());
        assertEquals("Developer1", developerDtos.get(0).getMemberId());

    }

    @Test
    void testEditDeveloper() {
        // 요청 객체 생성
        EditDeveloper.Request request = new EditDeveloper.Request(DeveloperLevel.SENIOR, DeveloperSkillType.FRONT_END, 12);

        // 모의 데이터 설정
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(Developer.builder()
                        .developerLevel(DeveloperLevel.JUNIOR)
                        .developerSkillType(DeveloperSkillType.BACK_END)
                        .experienceYears(5)
                        .name("Developer")
                        .age(25)
                        .build()));

        // 메서드 실행
        DeveloperDetailDto updatedDeveloper = dMakerService.editDeveloper(request, "memberId");

        // 결과 검증
        assertNotNull(updatedDeveloper);
        assertEquals(DeveloperLevel.SENIOR, updatedDeveloper.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, updatedDeveloper.getDeveloperSkillType());
        assertEquals(12, updatedDeveloper.getExperienceYears());
    }

    @Test
    void testDeleteDeveloper() {
        // 모의 데이터 설정
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(Developer.builder()
                        .developerLevel(DeveloperLevel.SENIOR)
                        .developerSkillType(DeveloperSkillType.BACK_END)
                        .experienceYears(10)
                        .name("Developer")
                        .age(30)
                        .build()));

        // 메서드 실행
        DeveloperDetailDto deletedDeveloper = dMakerService.deleteDeveloper("memberId");

        // 결과 검증
        assertNotNull(deletedDeveloper);
        assertEquals(DeveloperLevel.SENIOR, deletedDeveloper.getDeveloperLevel());
        assertEquals(DeveloperSkillType.BACK_END, deletedDeveloper.getDeveloperSkillType());
        assertEquals(10, deletedDeveloper.getExperienceYears());

        // 또한, retiredDeveloperRepository.save가 호출되었는지 확인 (실제로는 Mockito.verify()를 사용할 수 있음)
    }

    @Test
    void testCreateDeveloperWithDuplicatedMemberId() {
        // 테스트를 위한 CreateDeveloper.Request 객체 생성
        CreateDeveloper.Request request = CreateDeveloper.Request.builder()
                .developerLevel(DeveloperLevel.SENIOR)
                .developerSkillType(DeveloperSkillType.BACK_END)
                .experienceYears(15)
                .memberId("duplicatedMemberId")
                .build();

        // Mockito를 사용하여 developerRepository의 findByMemberId 메서드가 호출될 때
        // 이미 존재하는 멤버 ID로 인식하도록 설정
        given(developerRepository.findByMemberId("duplicatedMemberId"))
                .willReturn(Optional.of(Developer.builder().build()));

        // 메서드 실행 시 DMakerException이 발생하는지 검증
        assertThrows(DMakerException.class, () -> dMakerService.createDeveloper(request));
    }

    @Test
    void testCreateDeveloperWithInvalidExperienceYears() {
        // 테스트를 위한 CreateDeveloper.Request 객체 생성 (경험 연수가 부적절한 경우)
        CreateDeveloper.Request request = CreateDeveloper.Request.builder()
                .developerLevel(DeveloperLevel.SENIOR)
                .developerSkillType(DeveloperSkillType.BACK_END)
                .experienceYears(5) // SENIOR 레벨에는 부적절한 경험 연수
                .memberId("memberId")
                .build();

//      [오류 발생] Unnecessary stubbings detected.
//        testCreateDeveloperWithInvalidExperienceYears 테스트 메서드에서 developerRepository.findByMemberId(anyString())
//        에 대한 모의 설정이 필요 없기 때문에 발생합니다. 이 설정은 createDeveloper 메서드 내부에서 해당 멤버 ID의 개발자가
//        이미 존재하는지 확인할 때 사용되는데, 이 테스트 케이스에서는 해당 확인 과정이 중요하지 않습니다. createDeveloper 메서드의 핵심
//        로직은 입력된 경험 연수가 개발자 레벨과 일치하지 않을 때 DMakerException을 발생시키는 것이므로, 멤버 ID의 존재 여부는
//        이 테스트의 핵심이 아닙니다.

        // Mockito를 사용하여 developerRepository의 findByMemberId 메서드가 호출될 때
        // 해당 멤버 ID가 존재하지 않음을 의미하도록 설정
//        given(developerRepository.findByMemberId("memberId"))
//                .willReturn(Optional.empty());

        // 메서드 실행 시 DMakerException이 발생하는지 검증
        assertThrows(DMakerException.class, () -> dMakerService.createDeveloper(request));
    }


}