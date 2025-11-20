package com.example.GreenGrub.exception.errorResponse;

import java.util.Map;

public record FieldErrorList(
        Map<String,String> errors
){
}
