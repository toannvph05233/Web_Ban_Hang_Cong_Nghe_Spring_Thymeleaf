package com.bienvan.store.service;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.bienvan.store.config.OnePayConfig;
import com.bienvan.store.model.Logger;
import com.bienvan.store.service.Adapter.IPaymentAdapter;
import com.bienvan.store.service.Adapter.PaymentAdapter;

@Service
public class OnePayService implements IPaymentAdapter {
    private PaymentAdapter paymentAdapter;
    public OnePayService(PaymentAdapter paymentAdapter)
    {
        this.paymentAdapter = paymentAdapter; 
    }
    // static final String SECURE_SECRET = "your-secure-hash-secret";
    static final String SECURE_SECRET = "6D0870CDE5F24F34F3915FB0045120DB";
    static final char[] HEX_TABLE = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    private static byte[] decodeHexArray = new byte[103];

    static {
        int i = 0;
        for (byte b : new byte[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' }) {
            decodeHexArray[b] = (byte) i++;
        }
        decodeHexArray['a'] = decodeHexArray['A'];
        decodeHexArray['b'] = decodeHexArray['B'];
        decodeHexArray['c'] = decodeHexArray['C'];
        decodeHexArray['d'] = decodeHexArray['D'];
        decodeHexArray['e'] = decodeHexArray['E'];
        decodeHexArray['f'] = decodeHexArray['F'];
    }

    String hashAllFields(Map fields) {
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        StringBuffer buf = new StringBuffer();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0) && fieldName.indexOf("vpc_") == 0) {
                buf.append(fieldName + "=" + fieldValue);
                if (itr.hasNext()) {
                    buf.append('&');
                }
            }
        }
        byte[] mac = null;
        try {
            byte[] b = decodeHexa(SECURE_SECRET.getBytes());
            SecretKey key = new SecretKeySpec(b, "HMACSHA256");
            Mac m = Mac.getInstance("HMACSHA256");
            m.init(key);
            m.update(buf.toString().getBytes("UTF-8"));
            mac = m.doFinal();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String hashValue = hex(mac);
        return hashValue;
    }

    public static byte[] decodeHexa(byte[] data) throws Exception {
        if (data == null) {
            return null;
        }
        if (data.length % 2 != 0) {
            throw new Exception("Invalid data length:" + data.length);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte b1, b2;
        int i = 0;
        while (i < data.length) {
            b1 = decodeHexArray[data[i++]];
            b2 = decodeHexArray[data[i++]];
            out.write((b1 << 4) | b2);
        }
        out.flush();
        out.close();
        return out.toByteArray();
    }

    static String hex(byte[] input) {
        StringBuffer sb = new StringBuffer(input.length * 2);
        for (int i = 0; i < input.length; i++) {
            sb.append(HEX_TABLE[(input[i] >> 4) & 0xf]);
            sb.append(HEX_TABLE[input[i] & 0xf]);
        }
        return sb.toString();
    }

    void appendQueryFields(StringBuffer buf, Map fields) {
        List fieldNames = new ArrayList(fields.keySet());
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);

            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                buf.append(URLEncoder.encode(fieldName));
                buf.append('=');
                buf.append(URLEncoder.encode(fieldValue));
            }
            if (itr.hasNext()) {
                buf.append('&');
            }

        }

    }

    public String createOrder(int total,String orderInfor, String urlReturn) {
        Map<String, String> params = new HashMap<>();
        params.put("vpc_Version", "2");
        params.put("vpc_Currency", "VND");
        params.put("vpc_Command", "pay");
        params.put("vpc_AccessCode", OnePayConfig.oneAccessCode);
        params.put("vpc_Merchant", OnePayConfig.oneMerchantID);
        params.put("vpc_Locale", "vi");
        params.put("vpc_ReturnURL",  urlReturn + OnePayConfig.oneReturnurl);
        params.put("vpc_MerchTxnRef", getRandomNumber(8));
        params.put("vpc_OrderInfo", orderInfor);
        params.put("vpc_TicketNo", "127.0.0.1");

        params.put("AgainLink", "/again-link");
        params.put("Title", "Thanh toan");
        
        params.put("vpc_Amount", String.valueOf(total*100));

        Map fields = new HashMap();
        List fieldNames = new ArrayList(params.keySet());
        Collections.sort(fieldNames);

        Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }
        
        // String vpcURL = (String) fields.remove("virtualPaymentClientURL");
        String vpcURL = OnePayConfig.onePayUrl;
        fields.remove("SubButL");
        if (SECURE_SECRET != null && SECURE_SECRET.length() > 0) {
            String secureHash = hashAllFields(fields);
            fields.put("vpc_SecureHash", secureHash);
        }
        StringBuffer buf = new StringBuffer();
        buf.append(vpcURL).append('?');
        appendQueryFields(buf, fields);
        System.out.println(buf.toString());
        return buf.toString();
    }

    String getResponseDescription(String vResponseCode) {
        Map<String, String> map_result = new HashMap<String, String>() {
            {
                put("0", "Giao dịch thành công");
                put("1", "Ngân hàng từ chối giao dịch");
                put("3", "Mã đơn vị không tồn tại");
                put("4", "Không đúng access code");
                put("5", "Số tiền không hợp lệ");
                put("6", "Mã tiền tệ không tồn tại");
                put("7", "Lỗi không xác định");
                put("8", "Số thẻ không đúng");
                put("9", "Tên chủ thẻ không đúng");
                put("10", "Thẻ hết hạn/Thẻ bị khóa");
                put("11", "Thẻ chưa đăng ký sử dụng dịch vụ");
                put("12", "Ngày phát hành/Hết hạn không đúng");
                put("13", "Vượt quá hạn mức thanh toán");
                put("21", "Số tiền không đủ để thanh toán");
                put("99", "Người sủ dụng hủy giao dịch");
            }
        };
        String result = "";
        result = map_result.get(vResponseCode);
        if (result != null)
            return result;
        else
            return "No Value Returned";
    }
    private static String null2unknown(String in) {
        if (in == null || in.length() == 0) {
            return "No Value Returned";
        } else {
            return in;
        }
    }

    public Map<String, String> ipn(HttpServletRequest request){
        Map fields = new HashMap();
        for (Enumeration enum1 = request.getParameterNames(); enum1.hasMoreElements(); ) {
            String fieldName = (String) enum1.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0) && (!fieldName.equals("gw") )) {
                fields.put(fieldName, fieldValue);
            }
        }
        String vpc_Txn_Secure_Hash = null2unknown((String) fields.remove("vpc_SecureHash"));
        String hashValidated = null;
        if (fields.get("vpc_TxnResponseCode") != null || fields.get("vpc_TxnResponseCode") != "No Value Returned") {
            String secureHash = hashAllFields(fields);
            if (vpc_Txn_Secure_Hash.equalsIgnoreCase(secureHash)) {
                hashValidated = "CORRECT";
            } else {
                hashValidated = "INVALID HASH";
            }
        } else {
            hashValidated = "INVALID HASH";
        }
        String amount = null2unknown((String) fields.get("vpc_Amount"));
        String message = null2unknown((String) fields.get("vpc_Message"));
        String orderInfo = null2unknown((String) fields.get("vpc_OrderInfo"));
        String merchantID = null2unknown((String) fields.get("vpc_Merchant"));
        String merchTxnRef = null2unknown((String) fields.get("vpc_MerchTxnRef"));
        String transactionNo = null2unknown((String) fields.get("vpc_TransactionNo"));
        String txnResponseCode = null2unknown((String) fields.get("vpc_TxnResponseCode"));
    
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String dateStr = (dateFormat.format(date));

        Map<String, String> map_result = new HashMap<String, String>() {
            {
                put("transactionNo", transactionNo);
                put("amount", amount);
                put("orderInfo", orderInfo);
                put("paymentTime", dateStr);
                put("paymentMethod", "Onepay");
            }
        };
        String transStatus;
        if (hashValidated.equals("CORRECT") && txnResponseCode.equals("0")) {
            transStatus = "success";
        } else if (hashValidated.equals("INVALID HASH") && txnResponseCode.equals("0")) {
            transStatus = "fail";
        } else {
            transStatus = "pending";
        }
        map_result.put("paymentStatus", transStatus);
        return map_result;
    }
    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
