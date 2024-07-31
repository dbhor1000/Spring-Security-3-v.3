package JC.Spring.Security3.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    private final OAuth2AuthorizedClientService authorizedClientService;

    public CustomLogoutHandler(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication == null) {
            return;
        }

        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient("github", authentication.getName());
        if (authorizedClient != null) {
            String token = authorizedClient.getAccessToken().getTokenValue();
            System.out.println(token);
            revokeGitHubToken(token);
            authorizedClientService.removeAuthorizedClient("github", authentication.getName());
        }
    }

    private void revokeGitHubToken(String token) {
        String url = "https://api.github.com/applications/" + clientId + "/grant";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // Add Basic Authentication header
        headers.setBasicAuth(clientId, clientSecret);
        String requestBody = "{\"access_token\":\"" + token + "\"}";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        restTemplate.exchange(url, HttpMethod.DELETE, request, String.class, clientId);
    }
}