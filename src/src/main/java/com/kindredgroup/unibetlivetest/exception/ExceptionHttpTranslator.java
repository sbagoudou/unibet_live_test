package com.kindredgroup.unibetlivetest.exception;

import com.kindredgroup.unibetlivetest.dto.ExceptionDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
@RequestMapping(produces = "application/vnd.error+json")
public class ExceptionHttpTranslator {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity businessException(HttpServletRequest request, final CustomException e, HttpServletResponse response) {
        return new ResponseEntity(new ExceptionDto()
                .setErrormessage(e.getMessage())
                .setPath(request.getServletPath()), e.getException().getStatus());
    }

}
