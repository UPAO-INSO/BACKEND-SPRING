package team.upao.dev.integrations.aws.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public interface S3Service {
    String createBucket(String bucketName);

    String checkBucketExists(String bucketName);

    List<String> getAllBuckets();

    void uploadFile(MultipartFile file) throws IOException;

    byte[] downloadFile(String fileName);

    String generatePresignedUrl(String bucketName, String fileName, Duration expiration);

    String generatePresignedDownloadUrl(String bucketName, String fileName, Duration expiration);

    /** Lista las claves (keys) de objetos bajo un prefijo dado. */
    List<String> listObjectKeys(String prefix);
}
