package com.example.demo.src;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {
    @GetMapping(value = "/")
    public String main() {
        return "main 화면 접속 성공";
    }
}
