package org.shvetsov.models.DTO;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRS {
    private Integer rating;
    private String text;
}
