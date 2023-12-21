package com.fastcampus.programming.dmaker.service;

import com.fastcampus.programming.dmaker.dto.CreateDeveloper;
import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.exception.DMakerErrorCode;
import com.fastcampus.programming.dmaker.exception.DMakerException;
import com.fastcampus.programming.dmaker.repository.DeveloperRepository;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DMakerService {

    private final DeveloperRepository developerRepository;

    public DMakerService(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    @Transactional
    public void createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        // business logic start
        Developer developer = Developer.builder()
                .develperLevel(DeveloperLevel.JUNIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .experiencYears(2)
                .name("Olaf")
                .age(5)
                .build();
        developerRepository.save(developer);
    }
    public void validateCreateDeveloperRequest(CreateDeveloper.Request request){
        // business validation
        DeveloperLevel developerLevel = request.getDeveloperLevel();
        Integer experienceYears = request.getExperienceYears();
        if(developerLevel == DeveloperLevel.SENIOR
            && experienceYears < 10){
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if(developerLevel == DeveloperLevel.JUNGIOR
            && (experienceYears < 4 ||experienceYears > 10)){
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (developerLevel == DeveloperLevel.JUNGIOR && experienceYears > 4){
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        // 절차적 코드
//        Optional<Developer> developer = developerRepository.findByMemberId(request.getMemberId());
//        if(developer.isPresent())
//            throw new DMakerException(DMakerErrorCode.DUPLICATED_MEMBER_ID);

        // 선언식 코드
        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer) ->{
                    throw new DMakerException(DMakerErrorCode.DUPLICATED_MEMBER_ID);
                });
    }
}