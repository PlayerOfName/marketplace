package org.shvetsov.service;


import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.shvetsov.models.DTO.CommentRQ;
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
    private final EntityManager entityManager;

    @Transactional
    public Comment createComment(CommentRQ commentRQ) {
        Product product = productRepository.findById(commentRQ.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        Comment comment = Comment.builder()
                .rating(commentRQ.getRating())
                .text(commentRQ.getText())
                .product(product)
                .authorId(commentRQ.getAuthorId())
                .build();
        commentRepository.save(comment);
        return comment;
    }
}
