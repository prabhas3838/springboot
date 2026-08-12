package com.example.awsmicroservice.controller;

import com.example.awsmicroservice.service.S3Service;
import com.example.awsmicroservice.repository.ProductRepository;
import com.example.awsmicroservice.entity.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.awsmicroservice.service.RekognitionService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final S3Service s3Service;
    private final ProductRepository productRepository;
    private final RekognitionService rekognitionService;

    public ProductController(S3Service s3Service, ProductRepository productRepository, RekognitionService rekognitionService) {
        this.s3Service = s3Service;
        this.productRepository = productRepository;
        this.rekognitionService = rekognitionService;
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadProductImage(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("name") String name,
                                                     @RequestParam("description") String description) {
        try {
            // 1. Upload image to S3
            String fileName = s3Service.uploadFile(file);
            
            // 2. Ask AI to analyze the image
            String aiTags = rekognitionService.detectLabels(fileName);
            
            // 3. Save product info and image URL to MySQL database
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setImageUrl(fileName); 
            product.setAiTags(aiTags); // Save the AI tags!
            
            productRepository.save(product); 

            return ResponseEntity.ok("Product saved successfully! AI detected: [" + aiTags + "]");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to save product: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            byte[] data = s3Service.downloadFile(fileName);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
