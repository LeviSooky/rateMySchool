package com.ratemyschool.main.service;

import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SentimentService {

    public float calculateSentimentScore(String reviewInEnglish) {
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder().setContent(reviewInEnglish).setType(Document.Type.PLAIN_TEXT).build();
            AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
            Sentiment sentiment = response.getDocumentSentiment();
            return sentiment.getScore();
        } catch (IOException e) {
            return Float.MIN_VALUE;
        }
    }

    public int calculateStars(float score) {
        if (score < -0.8) {
            return -1;
        }
        if (score < -0.6) {
            return 0;
        }
        if (score <= -0.6) {
            return 1;
        }
        if (score <= -0.2) {
            return 2;
        }
        if (score <= 0.2) {
            return 3;
        }
        if (score <= 0.6) {
            return 4;
        }
        if (score <= 1) {
            return 5;
        }
        return 0;
    }
}
