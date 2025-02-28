package com.be.back_end.service.GoogleService;
import com.be.back_end.config.GoogleOAuthConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class GoogleService implements  IGoogleService{
    private final GoogleOAuthConfig googleOAuthConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public GoogleService(GoogleOAuthConfig googleOAuthConfig) {
        this.googleOAuthConfig = googleOAuthConfig;
    }
    @Override
    public String generateGoogleAuthUrl() {
        return UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/auth")
                .queryParam("client_id", googleOAuthConfig.getClientId())
                .queryParam("redirect_uri", googleOAuthConfig.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", "openid email profile")
                .toUriString();
    }

    @Override
    public Map<String, Object> fetchGoogleUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> userEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo", HttpMethod.GET, userEntity, Map.class);

        return response.getBody();
    }
    @Override
    public String exchangeCodeForAccessToken(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = UriComponentsBuilder.fromUriString("")
                .queryParam("code", code)
                .queryParam("client_id", googleOAuthConfig.getClientId())
                .queryParam("client_secret", googleOAuthConfig.getClientSecret())
                .queryParam("redirect_uri", googleOAuthConfig.getRedirectUri())
                .queryParam("grant_type", "authorization_code")
                .toUriString().substring(1);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, Map.class);

        return (String) response.getBody().get("access_token");
    }
}