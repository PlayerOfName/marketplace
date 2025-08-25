package org.shvetsov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.shvetsov.requestApi.CommentRQ;
import org.shvetsov.models.Comment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    @Mapping(target = "product", ignore = true)
    Comment toComment(CommentRQ commentRQ);
}
