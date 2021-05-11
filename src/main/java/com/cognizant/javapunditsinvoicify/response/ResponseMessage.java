package com.cognizant.javapunditsinvoicify.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage {

    String responseMessage;
    @JsonIgnore
    HttpStatus httpStatus;

}
