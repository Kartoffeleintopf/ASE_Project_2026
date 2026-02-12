package com.example.ase_project_2026;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class VersuchController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/Versuch")
    public Versuch testing(@RequestParam(defaultValue = "World") String name) {
        return new Versuch(counter.incrementAndGet(), template.formatted(name));
    }
}