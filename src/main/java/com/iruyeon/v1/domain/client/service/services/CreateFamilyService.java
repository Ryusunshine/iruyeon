package com.iruyeon.v1.domain.client.service.services;

import com.iruyeon.v1.config.exception.BaseException;
import com.iruyeon.v1.config.response.ErrorCode;
import com.iruyeon.v1.domain.client.dto.FamilyRequestDTO;
import com.iruyeon.v1.domain.client.entity.Family;
import com.iruyeon.v1.domain.client.enums.Relationship;
import com.iruyeon.v1.domain.member.enums.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateFamilyService {

    public Family createFamily(FamilyRequestDTO dto) {
        try {
            return Family.builder()
                    .name(dto.getName())
                    .phoneNumber(dto.getPhoneNumber())
                    .address(dto.getAddress())
                    .birthYear(dto.getBirthYear())
                    .university(dto.getUniversity())
                    .major(dto.getMajor())
                    .property(dto.getProperty())
                    .religion(dto.getReligion())
                    .currentJob(dto.getCurrentJob())
                    .previousJob(dto.getPreviousJob())
                    .jobDetail(dto.getJobDetail())
                    .info(dto.getInfo())
                    .homeTown(dto.getHomeTown())
                    .gender(Gender.valueOf(dto.getGender()))
                    .relationship(Relationship.valueOf(dto.getRelationShip()))
                    .build();
        } catch (Exception e) {
            throw new BaseException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR); // 원인 예외 포함
        }
    }
}





