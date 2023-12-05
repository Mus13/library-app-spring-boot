package com.library.Libraryapp.Controllers;

import com.library.Libraryapp.RequestModels.ReviewRequest;
import com.library.Libraryapp.Service.ReviewService;
import com.library.Libraryapp.Utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/secure/user/book")
    public boolean reviewBookByUser(@RequestHeader(value = "Authorization") String token,@RequestParam Long bookId) throws Exception {
        String userEmail= ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        if (userEmail==null)
            throw new Exception("User email is missing.");
        return reviewService.userReviewListed(userEmail,bookId);
    }

    @PostMapping("/secure")
    public void postReview(@RequestHeader(value = "Authorization") String token, @RequestBody ReviewRequest reviewRequest) throws Exception{
        String userEmail= ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        if (userEmail==null)
            throw new Exception("User email is missing.");
        reviewService.postReview(userEmail,reviewRequest);
    }
}
