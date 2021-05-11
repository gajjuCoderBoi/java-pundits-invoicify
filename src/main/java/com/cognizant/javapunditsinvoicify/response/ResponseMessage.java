package com.cognizant.javapunditsinvoicify.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMessage {

    String responseMessage;
    @JsonIgnore
    HttpStatus httpStatus;

}
