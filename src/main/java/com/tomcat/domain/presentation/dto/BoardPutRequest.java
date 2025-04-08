package com.tomcat.domain.presentation.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardPutRequest {
    private String title;
    private String content;
}
