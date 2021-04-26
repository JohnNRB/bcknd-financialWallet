package com.evertix.financialwallet.controller.commons;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@JsonPropertyOrder
public class MessageResponse implements Serializable {
    private Integer code;
    private String message;
    private Object data;
    @Builder.Default
    private Date date = new Date();
}
