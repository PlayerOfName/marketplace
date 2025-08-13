package org.shvetsov.service;

import org.shvetsov.requestApi.ProductPhotoRQ;
import org.shvetsov.requestApi.UploadFileRQ;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "storage-service", url = "${storage.service.url}")
public interface FileStorageServiceClient {
    @PostMapping(value = "files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductPhotoRQ uploadFile(@RequestParam UUID productId, @RequestParam UUID userId, @RequestParam MultipartFile file);

    @DeleteMapping("files/delete")
    public void deleteFile(@RequestParam UUID productId, @RequestParam String filename);

    @GetMapping("files/getfiles")
    public List<ProductPhotoRQ> getFiles(@RequestParam UUID productId) throws Exception;

}
