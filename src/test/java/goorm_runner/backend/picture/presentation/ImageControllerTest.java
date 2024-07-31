package goorm_runner.backend.picture.presentation;

import goorm_runner.backend.picture.application.S3Service;
import goorm_runner.backend.picture.application.UpdateMemberService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@SpringBootTest
//@AutoConfigureMockMvc
class ImageControllerTest {
//    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private S3Service s3Service;

    @MockBean
    private UpdateMemberService updateMemberService;

//    @Test
    public void testUploadImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes());

        when(s3Service.uploadFile(any(MultipartFile.class), any(Long.class)))
                .thenReturn("https://groom-runner-bucket.s3.region.amazonaws.com/member/1-test.jpg");

        mockMvc.perform(multipart("/api/picture/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("https://groom-runner-bucket.s3.region.amazonaws.com/member/1-test.jpg")));

        verify(s3Service, times(1)).uploadFile(any(MultipartFile.class), eq(1L));
        verify(updateMemberService, times(1)).updateMemberProfileImage(eq(1L), any(String.class));
    }

//    @Test
    public void testDeleteImage() throws Exception {
        String fileUrl = "https://groom-runner-bucket.s3.region.amazonaws.com/member/1-test.jpg";

        mockMvc.perform(delete("/api/picture/delete")
                        .param("fileUrl", fileUrl))
                .andExpect(status().isOk());

        verify(s3Service, times(1)).deleteFile(fileUrl);
        verify(updateMemberService, times(1)).removeMemberProfileImage(eq(1L));
    }
}