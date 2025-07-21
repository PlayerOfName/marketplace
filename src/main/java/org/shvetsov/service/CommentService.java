package org.shvetsov.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.shvetsov.mapper.CommentMapper;
import org.shvetsov.requestApi.CommentRQ;
import org.shvetsov.models.Comment;
import org.shvetsov.models.Product;
import org.shvetsov.repositories.CommentRepository;
import org.shvetsov.repositories.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public Comment createComment(CommentRQ commentRQ) {
        Product product = productRepository.findById(commentRQ.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        Comment comment = commentMapper.toComment(commentRQ);
        commentRepository.save(comment);
        return comment;
    }
}
