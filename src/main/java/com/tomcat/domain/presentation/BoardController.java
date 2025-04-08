package com.tomcat.domain.presentation;

import com.tomcat.domain.presentation.dto.BoardFindAllResponse;
import com.tomcat.domain.presentation.dto.BoardSaveRequest;
import com.tomcat.framework.Model;
import com.tomcat.framework.annotation.param.ModelAttribute;
import com.tomcat.framework.annotation.type.Controller;
import com.tomcat.framework.annotation.type.RequestMapping;
import com.tomcat.framework.annotation.method.GetMapping;
import com.tomcat.framework.annotation.method.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/app")
public class BoardController {
    @GetMapping
    public String board(Model model) {
        BoardFindAllResponse a =  BoardFindAllResponse.builder()
                .title("good")
                .build();

        BoardFindAllResponse b =  BoardFindAllResponse.builder()
                .title("good")
                .build();

        List<BoardFindAllResponse> dto = new ArrayList<>();
        dto.add(a);
        dto.add(b);

        model.append("posts", dto);
        return "index";
    }

    @GetMapping("/write")
    public String write() {
        return "write";
    }

    @PostMapping
    public String post(@ModelAttribute BoardSaveRequest dto) {
        return "index";
    }
}
