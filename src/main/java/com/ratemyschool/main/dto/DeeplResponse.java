package com.ratemyschool.main.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Data
public class DeeplResponse {
    private Translation[] translations;

    public String getFullReviewInEnglish() {
        return translations == null ? "" : StringUtils.join(Arrays.stream(translations).map(Translation::getText).toArray(), " ");
    }
}

@Data
class Translation {
    String detectedSourceLanguage;
    String text;
}
