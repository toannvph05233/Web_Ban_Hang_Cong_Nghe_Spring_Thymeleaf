package com.bienvan.store.service.Adapter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

@Service
public class PaymentAdapter {
    public Map<String, String> ipn(HttpServletRequest request) {
        return new HashMap();
    }

    public String createOrder(int total, String orderInfor, String urlReturn) {
        return "";

    }
}
