package com.altamar.shop.controllers.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v2/notification/feedback")
public class NotificationController {

//    private final FeedbackService feedbackService;
//
//    public FeedbackController(FeedbackService feedbackService) {
//        this.feedbackService = feedbackService;
//    }
//
//    /**
//     * @return status ok after sending notification by email from request body
//     */
//    @PostMapping
//    public ResponseEntity<Response<?>> feedback(@RequestBody Message message) {
//        log.info("[FeedbackController] : Sending notification");
//        feedbackService.getFeedback(message);
//        return ResponseEntity.ok(Response.ok("Ok"));
//    }

}
