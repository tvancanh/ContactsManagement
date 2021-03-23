/******************************************************
 *                                                     *
 *    Copyright (c) 2021 SAFETRUST. All rights reserved.  *
 *                                                     *
 ******************************************************/
package com.safetrust.contacts.management.ExceptionHandler;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

/**
 * @author tvancanh
 */
@Data
@NoArgsConstructor
public class ErrorResponse
{
    private String message;
    private List<String> details;
    private int statusCode;
    private String resMessage;
    private Date timestamp;
    private String description;

    public ErrorResponse(String message, int statusCode, String resMessage, Date timestamp, String description, List<String> details) {
        this.message = message;
        this.statusCode = statusCode;
        this.resMessage = resMessage;
        this.timestamp = timestamp;
        this.description = description;
        this.details = details;
    }
}
