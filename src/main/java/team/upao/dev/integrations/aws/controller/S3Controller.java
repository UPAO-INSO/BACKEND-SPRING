package team.upao.dev.integrations.aws.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.upao.dev.integrations.aws.service.S3Service;

import java.io.IOException;

@RestController
@RequestMapping("/aws")
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping("/create-bucket")
    public ResponseEntity<String> createBucket(@RequestParam("bucketName") String bucketName) {
        return ResponseEntity.ok(s3Service.createBucket(bucketName));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        s3Service.uploadFile(file);
        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) throws IOException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(s3Service.downloadFile(filename));
    }
}
