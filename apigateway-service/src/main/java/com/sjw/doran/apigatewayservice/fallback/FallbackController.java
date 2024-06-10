package com.sjw.doran.apigatewayservice.fallback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/member")
    public ResponseEntity<String> memberServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Member Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/order")
    public ResponseEntity<String> orderServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Order Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/item")
    public ResponseEntity<String> itemServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Item Service is currently unavailable. Please try again later.");
    }

//    @GetMapping("/auth-simple")
//    public ResponseEntity<String> authSimpleServiceFallback() {
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body("Auth Simple Service is currently unavailable. Please try again later.");
//    }
}
