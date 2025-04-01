package com.iruyeon.v1;

import com.iruyeon.v1.config.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@RequiredArgsConstructor
@EnableWebMvc
public class HealthCheckController {
    @GetMapping("/healthCheck")
    public ResponseEntity<Response<String>> checkServerHealth() {
        return ResponseEntity.ok(
                Response.of(
                        "alive"
                )
        );
    }
}
