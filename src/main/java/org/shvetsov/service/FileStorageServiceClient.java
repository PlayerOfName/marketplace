package org.shvetsov.service;


import org.shvetsov.requestApi.ProductPhotoRS;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@FeignClient(name = "storage-service", url = "${storage.service.url}")
public interface FileStorageServiceClient {
    @PostMapping(value = "files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductPhotoRS uploadFile(@RequestParam UUID productId, @RequestParam UUID fileId, @RequestPart MultipartFile file);

    @DeleteMapping("files/delete")
    public void deleteFile(@RequestParam String path);
}
