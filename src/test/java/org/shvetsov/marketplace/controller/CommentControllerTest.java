package org.shvetsov.marketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.shvetsov.comment.DuplicateCommentException;
import org.shvetsov.controllers.CommentController;

import org.shvetsov.models.Comment;
import org.shvetsov.models.Product;
import org.shvetsov.requestApi.CommentRQ;
import org.shvetsov.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Disabled
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateComment_Success() throws Exception {
        UUID authorId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);

        CommentRQ commentRQ = new CommentRQ(5, "Отличный товар!", authorId, productId);
        Comment comment = new Comment();
        comment.setRating(5);
        comment.setText("Отличный товар!");
        comment.setAuthorId(authorId);
        comment.setProduct(product);

        Mockito.when(commentService.createComment(any(CommentRQ.class))).thenReturn(comment);

        mockMvc.perform(post("/comment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRQ)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.text").value("Отличный товар!"))
                .andExpect(jsonPath("$.authorId").value(authorId.toString()))
                .andExpect(jsonPath("$.product.id").value(productId.toString()));;
    }

    @Test
    void testCreateComment_Duplicate() throws Exception {
        CommentRQ commentRQ = new CommentRQ(4, "Уже писал", UUID.randomUUID(), UUID.randomUUID());

        Mockito.when(commentService.createComment(any(CommentRQ.class)))
                .thenThrow(new DuplicateCommentException("Comment already exists"));

        mockMvc.perform(post("/comment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRQ)))
                .andExpect(status().isBadRequest());
    }
}

