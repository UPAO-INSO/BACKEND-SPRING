package team.upao.dev.integrations.aws.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import team.upao.dev.integrations.aws.service.S3Service;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    @Value("${spring.destination.folder}")
    private String destinationFolder;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Override
    public String createBucket(String bucketName) {
        return "Bucket create into " + s3Client.createBucket(builder -> builder.bucket(bucketName)).location();
    }

    @Override
    public String checkBucketExists(String bucketName) {
        return Optional.ofNullable(s3Client.headBucket(builder -> builder.bucket(bucketName)))
                .map(response -> "Bucket already exists")
                .orElseThrow(() -> new RuntimeException("Bucket not exists"));
    }

    @Override
    public List<String> getAllBuckets() {
        return Optional.ofNullable(s3Client.listBuckets().buckets())
                .orElseThrow(() -> new RuntimeException("Buckets not found"))
                .stream()
                .map(Bucket::name)
                .toList();
    }

    @Override
    public void uploadFile(MultipartFile file) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getOriginalFilename())
                .build();

        Optional.ofNullable(
                s3Client
                        .putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes())))
                .orElseThrow(() -> new RuntimeException("Upload file failed"));
    }

    @Override
    public byte[] downloadFile(String fileName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        return Optional.ofNullable(s3Client.getObjectAsBytes(getObjectRequest))
                .orElseThrow(() -> new RuntimeException("Download file failed"))
                .asByteArray();
    }

    @Override
    public String generatePresignedUrl(String bucketName, String fileName, Duration expiration) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(expiration)
                .getObjectRequest(builder -> builder.bucket(bucketName).key(fileName))
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }

    @Override
    public String generatePresignedDownloadUrl(String bucketName, String fileName, Duration expiration) {
        return generatePresignedUrl(bucketName, fileName, expiration);
    }

    @Override
    public List<String> listObjectKeys(String prefix) {
        List<String> keys = new java.util.ArrayList<>();
        String continuationToken = null;

        do {
            ListObjectsV2Request.Builder builder = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefix);

            if (continuationToken != null) {
                builder.continuationToken(continuationToken);
            }

            ListObjectsV2Response response = s3Client.listObjectsV2(builder.build());

            response.contents().stream()
                    .map(S3Object::key)
                    .forEach(keys::add);

            continuationToken = response.isTruncated() ? response.nextContinuationToken() : null;

        } while (continuationToken != null);

        return keys;
    }
}
