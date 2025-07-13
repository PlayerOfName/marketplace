package org.shvetsov.controllers;

import org.shvetsov.models.DTO.CommentRQ;
import org.shvetsov.models.Comment;
import org.shvetsov.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public ResponseEntity<Comment> createComment(@RequestBody CommentRQ commentRQ) {
        return ResponseEntity.ok(commentService.createComment(commentRQ));
    }
}
