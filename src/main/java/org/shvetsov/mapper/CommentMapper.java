package org.shvetsov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.shvetsov.DTO.CommentRQ;
import org.shvetsov.models.Comment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    Comment toComment(CommentRQ commentRQ);
}
