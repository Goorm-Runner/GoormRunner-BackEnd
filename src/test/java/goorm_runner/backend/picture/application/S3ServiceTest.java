package goorm_runner.backend.picture.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@SpringBootTest
class S3ServiceTest {
    @Mock
    private AmazonS3 amazonS3;

    @InjectMocks
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
    void uploadFile_success() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

        doNothing().when(amazonS3).putObject(anyString(), anyString(), any(ByteArrayInputStream.class), any(ObjectMetadata.class));

        URL mockUrl = new URL("http://example.com/test.jpg");
        when(amazonS3.getUrl(anyString(), anyString())).thenReturn(mockUrl);

        String fileUrl = s3Service.uploadFile(file, 1L);
        assertNotNull(fileUrl);
        assertTrue(fileUrl.contains("http://example.com/test.jpg"));
    }

//    @Test
    void deleteFile_success() {
        String fileUrl = "http://example.com/test.jpg";
        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);

        doNothing().when(amazonS3).deleteObject(anyString(), eq(fileName));

        s3Service.deleteFile(fileUrl);

        verify(amazonS3, times(1)).deleteObject(anyString(), eq(fileName));
    }

//    @Test
    void getFileExtension_test() {
        String extension = s3Service.getFileExtension("test.jpg");
        assertEquals("jpg", extension);
    }
}