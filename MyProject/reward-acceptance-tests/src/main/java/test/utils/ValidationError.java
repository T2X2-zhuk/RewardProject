package test.utils;

import lombok.*;

import java.lang.annotation.Target;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    private String errorCode;
    private String description;

}
