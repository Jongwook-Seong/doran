package com.sjw.doran.itemservice.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sjw.doran.itemservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AwsS3UploadService {

    private final AmazonS3Client amazonS3Client;
    private final MessageUtil messageUtil;
    private static final List<String> ALLOWED_CONTENT_TYPE = Arrays.asList("image/png", "image/jpg", "image/jpeg");

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String saveFile(String encodingImage, String itemUuid) {
        String[] arr = encodingImage.split(",");
        String data = arr[0]; // data:image/png;base64
        String encodingData = arr[1]; // 인코딩 데이터
        String contentType = data.substring(data.indexOf(":") + 1, data.indexOf(";"));
        if (!ALLOWED_CONTENT_TYPE.contains(contentType.toLowerCase())) {
            throw new UnsupportedMediaTypeStatusException(messageUtil.getUnsupportedFileTypeErrorMessage());
        }

        byte[] decodedImage = Base64.decodeBase64(encodingData);
        InputStream bis = new ByteArrayInputStream(decodedImage);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(decodedImage.length);
        metadata.setContentType(contentType);

        String fileName = "item/" + itemUuid;

        if (amazonS3Client.doesObjectExist(bucketName, fileName))
            amazonS3Client.deleteObject(bucketName, fileName);

        amazonS3Client.putObject(bucketName, fileName, bis, metadata);
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }
}
