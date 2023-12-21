package com.fastcampus.programming.dmaker.dto;

import com.fastcampus.programming.dmaker.entity.Developer;
import com.fastcampus.programming.dmaker.type.DeveloperLevel;
import com.fastcampus.programming.dmaker.type.DeveloperSkillType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

public class CreateDeveloper {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Request{
        @NotNull private DeveloperLevel developerLevel;

        @NotNull private DeveloperSkillType developerSkillType;

        @NotNull @Min(0) @Max(20) private Integer experienceYears;

        @NotNull @Size(min=3, max=50, message="memberId size 3 ~ 50")
        private String memberId;

        @NotNull @Size(min=3, max=20, message="name size 3 ~ 20")
        private String name;

        @Min(18) private Integer age;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private DeveloperLevel developerLevel;
        private DeveloperSkillType developerSkillType;
        private Integer experienceYears;

        private String memberId;
        private String name;
        private Integer age;
    }
}
