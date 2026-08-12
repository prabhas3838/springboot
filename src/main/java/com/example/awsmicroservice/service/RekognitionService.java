package com.example.awsmicroservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.S3Object;
import software.amazon.awssdk.services.rekognition.model.Label;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RekognitionService {

    private final RekognitionClient rekognitionClient;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public RekognitionService(RekognitionClient rekognitionClient) {
        this.rekognitionClient = rekognitionClient;
    }

    public String detectLabels(String fileName) {
        S3Object s3Object = S3Object.builder()
                .bucket(bucketName)
                .name(fileName)
                .build();

        Image image = Image.builder()
                .s3Object(s3Object)
                .build();

        DetectLabelsRequest request = DetectLabelsRequest.builder()
                .image(image)
                .maxLabels(5)
                .minConfidence(85F)
                .build();

        DetectLabelsResponse response = rekognitionClient.detectLabels(request);

        List<Label> labels = response.labels();

        // Convert the list of labels into a single comma-separated string
        return labels.stream()
                .map(Label::name)
                .collect(Collectors.joining(", "));
    }
}
