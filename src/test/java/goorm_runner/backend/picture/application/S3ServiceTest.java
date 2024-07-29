package goorm_runner.backend.picture.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class S3ServiceTest {
    @MockBean
    private AmazonS3 amazonS3;

    @Autowired
    private S3Service s3Service;

    @Test
    void testUploadFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        String expectedFileName = "member/1-test.jpg";
        URL mockUrl = new URL("https://groom-runner-bucket.s3.region.amazonaws.com/" + expectedFileName);

        when(amazonS3.getUrl(anyString(), anyString())).thenReturn(mockUrl);

        String url = s3Service.uploadFile(file, 1L);
        assertNotNull(url);

        verify(amazonS3, times(1)).putObject(
                eq("groom-runner-bucket"),
                eq(expectedFileName),
                any(InputStream.class),
                any(ObjectMetadata.class)
        );
    }

    @Test
    void testDeleteFile() {
        String fileUrl = "https://groom-runner-bucket.s3.region.amazonaws.com/member/1-test.jpg";
        s3Service.deleteFile(fileUrl);
        verify(amazonS3, times(1)).deleteObject(anyString(), anyString());
    }
}