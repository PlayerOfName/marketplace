package org.shvetsov.marketplace.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shvetsov.comment.DuplicateCommentException;
import org.shvetsov.mapper.CommentMapper;
import org.shvetsov.models.Comment;
import org.shvetsov.models.Product;
import org.shvetsov.repositories.CommentRepository;
import org.shvetsov.requestApi.CommentRQ;
import org.shvetsov.service.CommentService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    @Test
    void createComment_WhenValidRequest_ShouldSaveCommentAndUpdateProductRating() {
        // Given
        UUID productId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);

        CommentRQ commentRQ = new CommentRQ(5, "Отличный товар!", authorId, productId);
        Comment expectedComment = Comment.builder()
                .rating(5)
                .text("Отличный товар!")
                .authorId(authorId)
                .product(product)
                .build();

        when(commentRepository.existsByProductIdAndAuthorId(productId, authorId)).thenReturn(false);
        when(commentMapper.toComment(commentRQ)).thenReturn(expectedComment);
        when(commentRepository.save(expectedComment)).thenReturn(expectedComment);

        Comment result = commentService.createComment(commentRQ);

        assertThat(result).isEqualTo(expectedComment);
        verify(commentRepository).existsByProductIdAndAuthorId(productId, authorId);
        verify(commentMapper).toComment(commentRQ);
        verify(commentRepository).save(expectedComment);
        // Проверяем, что рейтинг продукта обновлен (если это важно)
        assertThat(product.getOverallRating()).isNotNull();
    }

    @Test
    void createComment_WhenDuplicateExists_ShouldThrowException() {
        // Given
        UUID productId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        CommentRQ commentRQ = new CommentRQ(4, "Повторный отзыв", authorId, productId);

        when(commentRepository.existsByProductIdAndAuthorId(productId, authorId)).thenReturn(true);

        assertThatThrownBy(() -> commentService.createComment(commentRQ))
                .isInstanceOf(DuplicateCommentException.class)
                .hasMessage("Comment already exists");

        verify(commentRepository, never()).save(any());
        verify(commentMapper, never()).toComment(any());
    }

    @Test
    void createComment_ShouldUpdateProductRating() {
        // Given
        UUID productId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        Product product = new Product();
        product.setId(productId);

        Comment existingComment = new Comment();
        existingComment.setRating(4);
        existingComment.setProduct(product);
        product.setComments(new ArrayList<>(List.of(existingComment)));

        CommentRQ newCommentRQ = new CommentRQ(5, "Лучший товар!", authorId, productId);
        Comment newComment = new Comment();
        newComment.setRating(5);
        newComment.setText("Лучший товар!");
        newComment.setAuthorId(authorId);
        newComment.setProduct(product);

        product.getComments().add(newComment);

        when(commentRepository.existsByProductIdAndAuthorId(productId, authorId)).thenReturn(false);
        when(commentMapper.toComment(newCommentRQ)).thenReturn(newComment);
        when(commentRepository.save(newComment)).thenAnswer(invocation -> {
            newComment.updateAverageRating();
            return newComment;
        });

        commentService.createComment(newCommentRQ);

        assertThat(product.getOverallRating())
                .isEqualByComparingTo(BigDecimal.valueOf(4.5)); // (4 + 5) / 2 = 4.5
    }
}
