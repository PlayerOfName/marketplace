package org.shvetsov.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.shvetsov.requestApi.ProductPhotoRQ;
import org.shvetsov.requestApi.ProductPhotoRS;
import org.shvetsov.service.ProductPhotoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final ProductPhotoService productPhotoService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductPhotoRS> createProductAndCharacteristics(@RequestParam UUID productId, @RequestPart MultipartFile file) {
        return ResponseEntity.ok(productPhotoService.uploadFileFromProduct(productId, file));
    }

    @DeleteMapping("/delete")
    public void deleteFile(@RequestParam UUID productId, @RequestParam Integer position) {
        productPhotoService.deletePhoto(productId, position);
    }

    @PostMapping("/{productId}/photos/{photoId}/position")
    public ResponseEntity<Void> changePhotoPosition(
            @PathVariable UUID productId,
            @PathVariable UUID photoId,
            @RequestParam Integer newPosition) {

        productPhotoService.changePhotoPositionOptimized(productId, photoId, newPosition);
        return ResponseEntity.ok().build();
    }
}
