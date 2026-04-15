package com.example.payment_service.constants;

public class ApiConstants {
    private ApiConstants(){}


    public static final String BASIC_API_URL="/api/v1";
    public static final String PAYMENTS="/payments";
    public static final String VERIFY="/payments/verify";
    public static final String PAYMENT_BY_ORDER_ID="/payments/order/{orderId}";

}
