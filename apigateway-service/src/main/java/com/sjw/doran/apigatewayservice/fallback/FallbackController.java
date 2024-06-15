package com.sjw.doran.apigatewayservice.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/member")
    public ResponseEntity<String> memberServiceFallback() {
        log.info("Fallback method for member service is called");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Member Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/order")
    public ResponseEntity<String> orderServiceFallback() {
        log.info("Fallback method for order service is called");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Order Service is currently unavailable. Please try again later.");
    }

//    @GetMapping("/auth-simple")
//    public ResponseEntity<String> authSimpleServiceFallback() {
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body("Auth Simple Service is currently unavailable. Please try again later.");
//    }
}
