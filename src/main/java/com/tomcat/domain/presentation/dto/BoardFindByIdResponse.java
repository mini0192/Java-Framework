package com.tomcat.domain.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardFindByIdResponse {
    private String title;
    private String writer;
    private String content;
}
