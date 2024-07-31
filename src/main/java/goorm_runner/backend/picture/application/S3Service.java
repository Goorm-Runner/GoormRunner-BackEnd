package goorm_runner.backend.picture.application;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;
    private final String bucketName = "groom-runner-bucket";

    public String uploadFile(MultipartFile file, Long memberId) throws IOException {
        try {
            String fileExtension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
            if (!isValidFileExtension(fileExtension)) {
                throw new IllegalArgumentException("Invalid file extension");
            }
            String fileName = "member/" + memberId + "-" + UUID.randomUUID() + "." + fileExtension;
            log.debug("Uploading file {}", fileName);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            amazonS3.putObject(bucketName, fileName, file.getInputStream(), objectMetadata);
            URL fileUrl = amazonS3.getUrl(bucketName, fileName);
            log.debug("Generated file URL: {}", fileUrl);
            if (fileUrl == null) {
                throw new IllegalStateException("The file URL is null");
            }
            return fileUrl.toString();
        } catch (AmazonServiceException e) {
            log.error("AmazonServiceException: {}", e.getErrorMessage());
            throw e;
        } catch (SdkClientException e) {
            log.error("SdkClientException: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
            amazonS3.deleteObject(bucketName, fileName);
        } catch (AmazonServiceException e) {
            log.error("AmazonServiceException: {}", e.getErrorMessage());
            throw e;
        } catch (SdkClientException e) {
            log.error("SdkClientException: {}", e.getMessage());
            throw e;
        }
    }

    public String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private boolean isValidFileExtension(String extension) {
        List<String> validExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");
        return validExtensions.contains(extension.toLowerCase());
    }
}
