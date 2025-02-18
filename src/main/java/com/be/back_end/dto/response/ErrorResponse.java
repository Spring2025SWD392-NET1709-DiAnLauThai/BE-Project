package com.be.back_end.dto.response;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
@Data
public class ErrorResponse {
    private int code;
    private String message;
    private LocalDateTime timestamp;
    private List<String> details;

    public ErrorResponse(int code, String message, List<String> details) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.details = details;
    }

}