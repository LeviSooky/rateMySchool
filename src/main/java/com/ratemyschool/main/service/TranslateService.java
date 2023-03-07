package com.ratemyschool.main.service;

import com.ratemyschool.main.model.DeeplResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Service
public class TranslateService {

    @Value("${deepl-api-secret-key}")
    private String API_KEY;
    private final String DEEPL_URL = "https://api-free.deepl.com/v2/translate";

    ResponseEntity<DeeplResponse> getDeeplApiCallResponse(String review) {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(DEEPL_URL)
                .queryParam("auth_key", API_KEY)
                .toUriString();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = buildHeaders();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("target_lang", "EN-US");
        map.add("text", review);
        headers.setContentLength(map.size());
        HttpEntity<MultiValueMap<String, String>> requestBody = new HttpEntity<>(map, headers);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<DeeplResponse> deeplResponse = restTemplate.exchange(urlTemplate, HttpMethod.POST, requestBody, DeeplResponse.class);
        return deeplResponse;
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("DeepL Auth-Key " + API_KEY);
        headers.setHost(InetSocketAddress.createUnresolved("api-free.deepl.com", 0));
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        return headers;
    }
}
