package com.tomcat.domain.presentation.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardSaveRequest {
    private String title;
    private String writer;
    private String content;
}
