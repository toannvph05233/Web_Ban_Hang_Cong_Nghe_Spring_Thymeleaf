package com.bienvan.store.config;

import org.springframework.stereotype.Component;

@Component
public class OnePayConfig {
    public static String onePayUrl = "https://mtf.onepay.vn/paygate/vpcpay.op";
    public static String oneReturnurl = "/payment?gw=onepay";
    public static String oneMerchantID = "TESTONEPAY";
    public static String oneAccessCode = "6BEB2546";
    // 4000000000001091
    // 05/24 123
    // 1234
}
