package test.utils;

import lombok.*;

import java.lang.annotation.Target;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ValidationError {

    private String errorCode;
    private String description;

}
