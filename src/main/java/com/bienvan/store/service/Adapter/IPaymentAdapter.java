package com.bienvan.store.service.Adapter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface IPaymentAdapter {
    public Map<String, String> ipn(HttpServletRequest request);
    public String createOrder(int total, String orderInfor, String urlReturn);
}
