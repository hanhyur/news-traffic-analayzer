package me.hanhyur.newstrafficanalyzer.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private List<FieldErrorDetail> fieldErrors;

    public ErrorResponse(HttpStatus status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }

    public ErrorResponse(HttpStatus status, String message, String path, List<FieldErrorDetail> fieldErrors) {
        this(status, message, path);
        this.fieldErrors = fieldErrors;
    }

    @Getter
    public static class FieldErrorDetail {
        private final String field;
        private final String rejectedValue;
        private final String reason;

        public FieldErrorDetail(String field, Object rejectedValue, String reason) {
            this.field = field;
            this.rejectedValue = rejectedValue != null ? rejectedValue.toString() : null;
            this.reason = reason;
        }
    }

}
