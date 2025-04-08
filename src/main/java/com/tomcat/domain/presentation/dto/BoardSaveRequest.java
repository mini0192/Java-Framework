package com.tomcat.domain.presentation.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardSaveRequest {
    String title;
    String content;
}
