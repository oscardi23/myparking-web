package com.devsoft.myparking.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ApiResponse {

    public static ResponseEntity<Map<String, Object>> success(Object data) {
        return ResponseEntity.ok(
                Map.of(
                        "ok", true,
                        "data", data
                )
        );
    }

    public static ResponseEntity<Map<String, Object>> message(String message) {
        return ResponseEntity.ok(
                Map.of(
                        "ok", true,
                        "message", message
                )
        );
    }

    public static ResponseEntity<Map<String, Object>> error(String message) {
        return ResponseEntity.badRequest().body(
                Map.of(
                        "ok", false,
                        "message", message
                )
        );
    }

    public static ResponseEntity<Map<String, Object>> serverError(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "ok", false,
                        "message", message
                )
        );
    }
}
