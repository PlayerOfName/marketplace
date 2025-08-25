package org.shvetsov.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.shvetsov.mapper.ProductPhotoMapper;
import org.shvetsov.models.Product;
import org.shvetsov.models.ProductPhoto;
import org.shvetsov.product.NotFoundProductException;
import org.shvetsov.repositories.ProductPhotoRepository;
import org.shvetsov.repositories.ProductRepository;
import org.shvetsov.requestApi.ProductPhotoRQ;
import org.shvetsov.requestApi.ProductPhotoRS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductPhotoService {
    private final ProductPhotoRepository productPhotoRepository;
    @Value("${app.photos.max-per-product}")
    private int maxPosition;
    private final FileStorageServiceClient fileStorageServiceClient;
    private final ProductRepository productRepository;
    private final ProductPhotoRepository photoRepository;
    private final ProductPhotoMapper productPhotoMapper;

    @Transactional
    public ProductPhotoRS uploadFileFromProduct(UUID productId, MultipartFile file) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundProductException("Product not found"));
        ProductPhoto productPhoto = productPhotoMapper.toProductPhoto(file);
        productPhoto.setProduct(product);
        productPhotoRepository.save(productPhoto);
        if (productPhoto.getId() != null) {
            ProductPhotoRS query = fileStorageServiceClient.uploadFile(productId, productPhoto.getId(), file);
            productPhoto.setPath(query.getPath());

            if (productPhoto.getPosition() == 0) {
                long count = photoRepository.countByProduct(productPhoto.getProduct());
                if (count >= maxPosition) {
                    throw new IllegalStateException("Максимум " + maxPosition + " фото на товар");
                }

                productPhoto.setPosition((int) count + 1); // Новая позиция
            }
            if (product.getPhotos() == null) {
                product.setPhotos(new ArrayList<>());
            }
            product.getPhotos().add(productPhoto);
            photoRepository.save(productPhoto);
            return query;
        }else {
            System.out.println(productPhoto.getId());
            throw new RuntimeException("Фото не загружено");
        }
    }

    @Transactional
    public void deletePhoto(UUID productId, Integer position) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundProductException("Product not found"));

        ProductPhoto photo = photoRepository.findByProductAndPosition(product, position)
                .orElseThrow(() -> new RuntimeException("Фото не найдено"));

        fileStorageServiceClient.deleteFile(photo.getPath());
        photoRepository.delete(photo);

        // Обновляем позиции оставшихся фото
        updatePhotoPositionsAfterDeletion(product, position);
    }

    private void updatePhotoPositionsAfterDeletion(Product product, Integer deletedPosition) {
        // Получаем все фото продукта, начиная с позиции после удаленной
        List<ProductPhoto> photosToUpdate = photoRepository.findByProductAndPositionGreaterThanOrderByPositionAsc(product, deletedPosition);

        // Смещаем позиции на 1 назад
        for (int i = 0; i < photosToUpdate.size(); i++) {
            ProductPhoto photo = photosToUpdate.get(i);
            photo.setPosition(deletedPosition + i);
        }

        photoRepository.saveAll(photosToUpdate);
    }

    @Transactional
    public void changePhotoPositionOptimized(UUID productId, UUID photoId, Integer newPosition) {
        // 1. Находим продукт и фотографию
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        ProductPhoto photo = productPhotoRepository.findById(photoId)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found with id: " + photoId));

        if (!photo.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Photo does not belong to this product");
        }

        // 2. Получаем все фотографии продукта
        List<ProductPhoto> photos = productPhotoRepository.findByProductIdOrderByPositionAsc(productId);

        // 3. Проверяем валидность позиции
        if (newPosition < 0 || newPosition >= photos.size()) {
            throw new IllegalArgumentException("Invalid position: " + newPosition +
                    ". Available positions: 0 to " + (photos.size() - 1));
        }

        // 4. Удаляем фотографию из текущей позиции
        photos.remove(photo);

        // 5. Вставляем фотографию на новую позицию
        photos.add(newPosition, photo);

        // 6. Обновляем позиции всех фотографий
        for (int i = 0; i < photos.size(); i++) {
            ProductPhoto currentPhoto = photos.get(i);
            if (!currentPhoto.getPosition().equals(i)) {
                currentPhoto.setPosition(i);
                productPhotoRepository.save(currentPhoto);
            }
        }
    }

}
