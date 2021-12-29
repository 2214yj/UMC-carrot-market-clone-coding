package com.example.demo.src;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {
    @GetMapping(value = "/app/main")
    public String main() {
        return "main 화면 입니다.";
    }
}
