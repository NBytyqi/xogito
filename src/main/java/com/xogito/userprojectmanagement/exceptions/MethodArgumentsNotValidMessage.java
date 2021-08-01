package com.xogito.userprojectmanagement.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MethodArgumentsNotValidMessage {

    private HttpStatus httpStatus;
    private List<String> errors;
}
