package com.be.back_end.service.GoogleService;

import java.util.Map;

public interface IGoogleService {
    String generateGoogleAuthUrl();
    String exchangeCodeForAccessToken(String code);
    Map<String, Object> fetchGoogleUserInfo(String accessToken);
}
