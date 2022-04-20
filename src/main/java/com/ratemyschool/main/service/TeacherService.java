package com.ratemyschool.main.service;

import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.ratemyschool.main.enums.RMSConstants;
import com.ratemyschool.main.model.DeeplResponse;
import com.ratemyschool.main.model.Review;
import com.ratemyschool.main.model.Teacher;
import com.ratemyschool.main.repo.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.ratemyschool.main.enums.RMSConstants.ACTIVE;
import static com.ratemyschool.main.enums.RMSConstants.PENDING;

@Service
@RequiredArgsConstructor()
public class TeacherService {

    @Value("${deepl-api-secret-key}")
    private String API_KEY;
    private final String DEEPL_URL = "https://api-free.deepl.com/v2/translate";
    private final TeacherRepository teacherRepository;
    // private final ModelMapper modelMapper; //TODO move the bean

    public void addTeacher(Teacher teacher) {
        teacher.setStatus(ACTIVE);
        teacher.setId(UUID.randomUUID());
        teacherRepository.save(teacher);
    }
    public void deleteTeacher(Teacher teacher) {
        teacher.setStatus(RMSConstants.DELETED);
        teacherRepository.save(teacher);
    }

    public List<Teacher> getTeachers(Pageable pageable) {

        return teacherRepository.findAllByStatusEquals(ACTIVE, pageable)
                //.map(teacher -> modelMapper.map(teacher, TeacherDTO.class))
                .toList();
    }
    public Teacher getTeacherById(UUID id) {
        return teacherRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<Teacher> getTeachersBySchoolId(UUID schoolId, Pageable pageable) {
        return teacherRepository.findAllBySchoolId(schoolId, pageable).toList();
    }

    public boolean addReview(UUID teacherId, Review review) {

        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(RuntimeException::new);
        review.setId(UUID.randomUUID());
        ResponseEntity<DeeplResponse> deeplResponse = getDeeplApiCallResponse(review);
        if(deeplResponse.getStatusCode().equals(HttpStatus.OK)) {
            String reviewStatus = deeplResponse.getBody().getFullReviewInEnglish();
            try (LanguageServiceClient language = LanguageServiceClient.create()) {
                Document doc = Document.newBuilder().setContent(reviewStatus).setType(Document.Type.PLAIN_TEXT).build();
                AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
                Sentiment sentiment = response.getDocumentSentiment();
                if (sentiment == null) {
                    review.setStatusFlag(PENDING);
                } else {
                    review.setStars(calculateStars(sentiment.getScore())); //TODO
                }

                if (review.getStars() == -1) {
                    return false;
                } else {
                    review.setStatusFlag(review.getStars() > 0 ? ACTIVE : PENDING);
                    teacher.addReview(review);
                    teacherRepository.save(teacher);
                }
            } catch (IOException e) {
                review.setStatusFlag(PENDING);
                teacher.addReview(review);
                teacherRepository.save(teacher);
            }
        } else {
            review.setStatusFlag(PENDING);
            teacher.addReview(review);
            teacherRepository.save(teacher);
        }

        return true;
    }

    private ResponseEntity<DeeplResponse> getDeeplApiCallResponse(Review review) {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(DEEPL_URL)
                .queryParam("auth_key", API_KEY)
                .toUriString();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = buildHeaders();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("target_lang", "EN-US");
        map.add("text", review.getContent());
        headers.setContentLength(map.size());
        HttpEntity<MultiValueMap<String, String>> requestBody = new HttpEntity<>(map, headers);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<DeeplResponse> deeplResponse = restTemplate.exchange(urlTemplate, HttpMethod.POST, requestBody, DeeplResponse.class);
        return deeplResponse;
    }

    private byte calculateStars(float score) {
        if(score < -0.8) {
            return -1;
        }
        if(score < -0.6) {
            return 0;
        }
        if(score <= -0.6) {
            return 1;
        }
        if(score <= -0.2) {
            return 2;
        }
        if(score <= 0.2) {
            return 3;
        }
        if(score <= 0.6) {
            return 4;
        }
        if(score <= 1) {
            return 5;
        }
        return 0;
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

    public void activateTeacherById(UUID teacherId, Boolean shouldActivate) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(RuntimeException::new);
        teacher.setStatus(shouldActivate ? ACTIVE : RMSConstants.DELETED);
        teacherRepository.save(teacher);

    }

}
