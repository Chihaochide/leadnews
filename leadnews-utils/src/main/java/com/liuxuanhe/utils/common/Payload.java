package com.liuxuanhe.utils.common;

import lombok.Data;

import java.util.Date;

@Data
public class Payload<T> {
    private String id;
    private T info;
    private Date expiration;
}