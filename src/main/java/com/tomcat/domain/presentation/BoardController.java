package com.tomcat.domain.presentation;

import com.tomcat.domain.presentation.dto.BoardFindAllResponse;
import com.tomcat.domain.presentation.dto.BoardFindByIdResponse;
import com.tomcat.domain.presentation.dto.BoardPutRequest;
import com.tomcat.domain.presentation.dto.BoardSaveRequest;
import com.tomcat.framework.Model;
import com.tomcat.framework.annotation.method.DeleteMapping;
import com.tomcat.framework.annotation.method.PutMapping;
import com.tomcat.framework.annotation.param.ModelAttribute;
import com.tomcat.framework.annotation.param.PathVariable;
import com.tomcat.framework.annotation.type.Controller;
import com.tomcat.framework.annotation.type.RequestMapping;
import com.tomcat.framework.annotation.method.GetMapping;
import com.tomcat.framework.annotation.method.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/app/board")
public class BoardController {

    @GetMapping
    public String board(Model model) {
        BoardFindAllResponse a =  BoardFindAllResponse.builder()
                .id(3L)
                .title("good")
                .build();

        BoardFindAllResponse b =  BoardFindAllResponse.builder()
                .id(2L)
                .title("good")
                .writer("me")
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

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") int id, Model model) {
        System.out.println(id);
        BoardFindByIdResponse dto = BoardFindByIdResponse.builder()
                .title("good")
                .content("content")
                .writer("wir")
                .build();
        model.append("post", dto);
        return "detail";
    }

    @PutMapping("/{id}")
    public String put(@PathVariable("id") int id, @ModelAttribute BoardSaveRequest dto) {
        return "redirect:/app/board/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        return "redirect:/app/board/" + id;
    }

    @PostMapping
    public String post(@ModelAttribute BoardSaveRequest dto) {
        return "redirect:/app/board";
    }
}
