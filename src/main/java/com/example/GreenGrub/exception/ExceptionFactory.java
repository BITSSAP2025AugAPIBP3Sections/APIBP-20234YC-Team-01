package com.example.GreenGrub.exception;

import com.example.GreenGrub.exception.base.ControllerException;
import com.example.GreenGrub.exception.error.ErrorCode;
import com.example.GreenGrub.exception.errorResponse.ErrorDescription;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Data
@Service
@RequiredArgsConstructor
public class ExceptionFactory {
    private final MessageSource mMessageSource;

    private ErrorDescription createError(
            @NotNull ErrorCode errorCode,
            Object[] errorArgs,
            Object[] suggestionArgs
            ) {
        return ErrorDescription.builder()
                .code(errorCode.getCode())
                .message(getFormatedMessage(errorCode,errorArgs))
                .suggestions(getFormatedSuggestions(errorCode,suggestionArgs))
                .build();
    }

    private String getFormatedMessage( ErrorCode code,Object... args) {
        var lTemp = MessageFormat.format(mMessageSource.getMessage(code.getErrorMessageTag(),null, Locale.getDefault()), args);
        return String.format(lTemp, args);
    }

    private List<String> getFormatedSuggestions(ErrorCode code, Object... args ) {
        return Arrays.stream(code.getErrorSuggestionTag()).map(tag -> getSuggestions(tag,args)).toList();
    };

    private String getSuggestions(String tag, Object[] args) {
        var lTemp = MessageFormat.format(mMessageSource.getMessage(tag, null, Locale.getDefault()), args);
        return String.format(lTemp, args);
    }


    public ControllerException userNotFoundException() {
        var lErrorCode = ErrorCode.USER_NOT_FOUND;
        var lMessage = mMessageSource.getMessage(lErrorCode.getErrorMessageTag(), null, Locale.getDefault());
        var lSuggestion = Arrays.stream(lErrorCode.getErrorSuggestionTag()).map(tag -> mMessageSource.getMessage(tag, null, Locale.getDefault())).toList();
        var error = ErrorDescription.builder().code(lErrorCode.getCode()).message(lMessage).suggestions(lSuggestion).build();
        return new ControllerException(error);
    }

}
