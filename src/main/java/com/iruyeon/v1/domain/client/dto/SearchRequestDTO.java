package com.iruyeon.v1.domain.client.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SearchRequestDTO {

    private List<String> job;
    private int minAge;
    private int maxAge;
    private List<String> eduDegree;
    private List<String> universities;
    private List<String> gender;
    private List<String> maritalStatus;

}
