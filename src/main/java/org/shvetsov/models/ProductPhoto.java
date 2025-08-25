package org.shvetsov.models;

import jakarta.persistence.*;
import lombok.*;
import org.shvetsov.annotation.GeneratedPosition;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_photos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPhoto {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    @Column(name = "file_name" ,nullable = false)
    private String fileName;

    @Column(name = "path")
    private String path;

    @Column(name = "size")
    private Long size;

    @GeneratedPosition
    @Column(name = "position")
    private Integer position;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @PrePersist
    public void prePersist() {
        if (this.position == null) {
            this.position = 0;
        }
    }
}

