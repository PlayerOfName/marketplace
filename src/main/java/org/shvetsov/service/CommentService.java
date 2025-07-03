package org.shvetsov.service;


import jakarta.persistence.LockModeType;
import org.modelmapper.ModelMapper;
import org.shvetsov.DTO.CommentRQ;
import org.shvetsov.mapper.CommentMapper;
import org.shvetsov.models.Comment;
import org.shvetsov.models.Product;
import org.shvetsov.repositories.CommentRepository;
import org.shvetsov.repositories.ProductRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final ProductService productService;

    public CommentService(CommentRepository commentRepository, ModelMapper modelMapper, ProductRepository productRepository, CommentMapper commentMapper, ModelMapper modelMapper1, ProductService productService) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper1;
        this.productService = productService;
    }

    public Comment createComment(CommentRQ commentRQ) {
        Comment comment = Comment.builder()
                .rating(commentRQ.getRating())
                .text(commentRQ.getText())
                .productId(commentRQ.getProductId())
                .authorId(commentRQ.getAuthorId())
                .build();
        commentRepository.save(comment);
        productService.updateOverallRating(commentRQ.getProductId());
        return comment;
    }
}
