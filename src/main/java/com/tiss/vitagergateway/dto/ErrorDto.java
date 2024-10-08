package com.tiss.vitagergateway.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

@Data
public class ErrorDto {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date date;

    private String message;

    private String type;

    private Integer code;

    private String traceId;

    public ErrorDto() {
        this.date = new Date();
        this.traceId = RandomStringUtils.randomAlphanumeric(10);
    }
}
