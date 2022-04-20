package com.ratemyschool.main.service;

import com.ratemyschool.main.enums.RMSConstants;
import com.ratemyschool.main.model.Review;
import com.ratemyschool.main.repo.ReviewRepository;
import edu.stanford.nlp.pipeline.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public void addReview(Review review) {
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

    public List<Review> getPendingReviews() {
        return reviewRepository.findAllByStatusFlagOrderByCreationDate(RMSConstants.PENDING);
    }

    public void activateReviewById(UUID reviewId, Boolean isOk) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(RuntimeException::new);
        review.setStatusFlag(isOk ? RMSConstants.ACTIVE : RMSConstants.DELETED);
        reviewRepository.save(review);
    }
}
