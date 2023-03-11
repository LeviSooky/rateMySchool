package com.ratemyschool.main.service;

import com.ratemyschool.main.dto.TeacherReview;
import com.ratemyschool.main.enums.EntityStatus;
import com.ratemyschool.main.exception.RmsRuntimeException;
import com.ratemyschool.main.model.PageResult;
import com.ratemyschool.main.model.TeacherReviewData;
import com.ratemyschool.main.repo.TeacherReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherReviewService {
    private final TeacherReviewRepository reviewRepository;

    public void addReview(TeacherReviewData review) {
//        TokenizerAnnotator tokenizerAnnotator = new TokenizerAnnotator("hu");
//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//        pipeline.addAnnotator(tokenizerAnnotator);
//        CoreDocument coreDocument = pipeline.processToCoreDocument("Nagyon szép vagy ma drága barátom");
//        Annotation document = new Annotation("Nagyon szép vagy ma drága barátom");
//        pipeline.annotate(coreDocument);
//        List<CoreSentence> sentences = coreDocument.sentences();
//        sentences.forEach(System.out::println);

        //SentimentAnnotator sentimentAnnotator = new SentimentAnnotator(tokenizerAnnotator.toString(),null);
    }

    public List<TeacherReviewData> getPendingReviews() {
        return reviewRepository.findAllByStatusOrderByCreationDate(EntityStatus.PENDING);
    }

    public void activateReviewById(UUID reviewId, Boolean shouldActivate) {
        TeacherReviewData review = reviewRepository.findById(reviewId).orElseThrow(RmsRuntimeException::new);
        review.setStatus(shouldActivate ? EntityStatus.ACTIVE : EntityStatus.DELETED);
        reviewRepository.save(review);
    }

    public List<TeacherReviewData> getFailedReviews() {
        return reviewRepository.findAllByStatusIn(List.of(EntityStatus.SENTIMENT_FAILED, EntityStatus.TRANSLATION_FAILED));
    }

    void saveAll(List<TeacherReviewData> result) {
        reviewRepository.saveAll(result);
    }

    void deleteDeletedComments(LocalDateTime current, LocalDateTime from) {
        reviewRepository.deleteAllByLastModifiedBetweenAndStatus(current, from, EntityStatus.DELETED);
    }

    public PageResult<TeacherReviewData, TeacherReview> findAllActiveBy(UUID teacherId, Pageable pageable) {
        return new PageResult<>(reviewRepository.findAllActive(teacherId, pageable));
    }

    public PageResult<TeacherReviewData, TeacherReview> findAllBy(UUID teacherId, Pageable pageable) {
        return new PageResult<>(reviewRepository.findAllByTeacherId(teacherId, pageable));
    }
}
