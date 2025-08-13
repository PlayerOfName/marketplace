package org.shvetsov.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.shvetsov.comment.DuplicateCommentException;
import org.shvetsov.mapper.CommentMapper;
import org.shvetsov.requestApi.CommentRQ;
import org.shvetsov.models.Comment;
import org.shvetsov.repositories.CommentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public Comment createComment(CommentRQ commentRQ) {
        if (commentRepository.existsByProductIdAndAuthorId(
                commentRQ.getProductId(),
                commentRQ.getAuthorId())) {
            throw new DuplicateCommentException("Comment already exists");
        }
        Comment comment = commentMapper.toComment(commentRQ);
        commentRepository.save(comment);
        return comment;
    }
}
