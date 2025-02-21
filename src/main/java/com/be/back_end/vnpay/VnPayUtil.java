package com.be.back_end.vnpay;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public class VnPayUtil {
    public static String generatePaymentUrl(String vnp_TmnCode, String vnp_HashSecret, String vnp_PayUrl, String vnp_ReturnUrl, long amount, String orderInfo) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_CurrCode = "VND";
        String vnp_Locale = "vn";
        String vnp_TxnRef = UUID.randomUUID().toString().replace("-", "");

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", vnp_Version);
        params.put("vnp_Command", vnp_Command);
        params.put("vnp_TmnCode", vnp_TmnCode);
        params.put("vnp_Amount", String.valueOf(amount * 100));  // Amount in VNPay is in smallest unit (e.g., 100,000 = 10000000)
        params.put("vnp_CurrCode", vnp_CurrCode);
        params.put("vnp_TxnRef", vnp_TxnRef);
        params.put("vnp_OrderInfo", orderInfo);
        params.put("vnp_Locale", vnp_Locale);
        params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        params.put("vnp_CreateDate", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        // Sort parameters alphabetically
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                // Create hash data
                if (hashData.length() > 0) {
                    hashData.append("&");
                }
                hashData.append(fieldName).append("=").append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                // Create query string
                if (query.length() > 0) {
                    query.append("&");
                }
                query.append(fieldName).append("=").append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
            }
        }

        // Generate HMAC-SHA256 hash
        String secureHash = hmacSHA256(vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        return vnp_PayUrl + "?" + query.toString();
    }

    private static String hmacSHA256(String key, String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest((key + data).getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error while hashing data", ex);
        }
    }
}
