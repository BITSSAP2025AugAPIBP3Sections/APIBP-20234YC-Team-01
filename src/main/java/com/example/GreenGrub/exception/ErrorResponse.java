package com.example.GreenGrub.exception;

import java.util.Map;

public record ErrorResponse(
        Map<String,String> errors
){
}
