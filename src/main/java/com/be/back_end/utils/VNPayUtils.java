package com.be.back_end.utils;

import com.be.back_end.config.VNPayConfig;
import com.be.back_end.enums.TransactionStatusEnum;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class VNPayUtils {

    private final VNPayConfig vnPayConfig;
    private static final SecureRandom random = new SecureRandom();

    @Autowired
    public VNPayUtils(VNPayConfig vnPayConfig) {
        this.vnPayConfig = vnPayConfig;
    }

    public  String generatePaymentUrl(String amount, String orderInfo, String orderType, HttpServletRequest request) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_CurrCode = "VND";
        String vnp_Locale = "vn";
        String vnp_TxnRef = getRandomNumber(8);
        String vnp_IpAddr = getIpAddress(request);
        String vnp_BankCode="VNBANK";
        int newAmount= Integer.parseInt(amount)*100;

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", vnp_Version);
        params.put("vnp_Command", vnp_Command);
        params.put("vnp_TmnCode", vnPayConfig.getVnp_TmnCode());
        params.put("vnp_Amount", String.valueOf(newAmount));
        params.put("vnp_BankCode",vnp_BankCode);
        params.put("vnp_CurrCode", vnp_CurrCode);
        params.put("vnp_IpAddr", vnp_IpAddr);
        params.put("vnp_Locale", vnp_Locale);
        params.put("vnp_OrderInfo", orderInfo);
        params.put("vnp_OrderType", orderType);
        params.put("vnp_ReturnUrl", vnPayConfig.getVnp_ReturnUrl());
        params.put("vnp_CreateDate", vnp_CreateDate);
        params.put("vnp_ExpireDate", vnp_ExpireDate);
        params.put("vnp_TxnRef", vnp_TxnRef);

        //Build data to hash and querystring
        List fieldNames = new ArrayList(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        // Generate HMAC-SHA256 hash
        String queryUrl = query.toString();
        String vnp_SecureHash = hmacSHA512(vnPayConfig.getVnp_HashSecret(), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
    }

    private static String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    private static String getIpAddress(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // Handle multiple IPs (in case of proxies)
        if (ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    private static String getRandomNumber(int len){
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    public static String encodeBookingCode(String bookingCode) {
        String randomPart = generateRandomString(3);  // Generate a random 3 character string
        String combined = randomPart + bookingCode;  // Prepend the random string to the bookingCode
        return combined;  // Return the combined result without encoding (as per your example)
    }
    public  String decodeBookingCode(String encodedString) {
        // Assuming the random part is always of the same length (3 in this case)
        int randomPartLength = 3;

        // Extract the booking code by removing the random part
        return encodedString.substring(randomPartLength);
    }
    public String generatePaymentUrlWithBookingId(String amount, String orderInfo, String orderType,
                                                  HttpServletRequest request, String bookingCode,
                                                  TransactionStatusEnum transactionType) throws UnsupportedEncodingException {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_CurrCode = "VND";
        String vnp_Locale = "vn";
        String vnp_TxnRef = encodeBookingCode(bookingCode);
        String vnp_IpAddr = getIpAddress(request);
        String vnp_BankCode = "VNBANK";
        int newAmount = new BigDecimal(amount).multiply(BigDecimal.valueOf(100)).intValue();

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());


        // Parameter mapping
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", vnp_Version);
        params.put("vnp_Command", vnp_Command);
        params.put("vnp_TmnCode", vnPayConfig.getVnp_TmnCode());
        params.put("vnp_Amount", String.valueOf(newAmount));
        params.put("vnp_BankCode", vnp_BankCode);
        params.put("vnp_CurrCode", vnp_CurrCode);
        params.put("vnp_IpAddr", vnp_IpAddr);
        params.put("vnp_Locale", vnp_Locale);
        params.put("vnp_OrderInfo", orderInfo);
        params.put("vnp_OrderType", orderType);
        params.put("vnp_ReturnUrl", vnPayConfig.getVnp_ReturnUrl() + "/" + transactionType.toString().toLowerCase());
        params.put("vnp_CreateDate", vnp_CreateDate);
        params.put("vnp_ExpireDate", vnp_ExpireDate);
        params.put("vnp_TxnRef", vnp_TxnRef);

        // Build data to hash and querystring
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        // Generate HMAC-SHA256 hash
        String queryUrl = query.toString();
        String vnp_SecureHash = hmacSHA512(vnPayConfig.getVnp_HashSecret(), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
    }

}
