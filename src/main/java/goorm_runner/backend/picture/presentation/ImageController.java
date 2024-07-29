package goorm_runner.backend.picture.presentation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import goorm_runner.backend.picture.application.S3Service;
import goorm_runner.backend.picture.application.UpdateMemberService;
import goorm_runner.backend.picture.dto.UpdateMemberRequestDTO;
import goorm_runner.backend.picture.dto.UpdateMemberResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/picture")
public class ImageController {
    private final S3Service s3Service;
    private final UpdateMemberService updateMemberService;

    @PostMapping("/upload")
    public ResponseEntity<UpdateMemberResponseDTO> uploadImage(@ModelAttribute UpdateMemberRequestDTO requestDTO) {
        try {
            String loginId = getCurrentMemberLoginId();
            Long memberId = updateMemberService.getMemberIdByLoginId(loginId);
            String fileUrl = s3Service.uploadFile(requestDTO.getFile(), memberId);
            updateMemberService.updateMemberProfileImage(memberId, fileUrl);

            UpdateMemberResponseDTO responseDTO = new UpdateMemberResponseDTO();
            responseDTO.setFileUrl(fileUrl);
            return ResponseEntity.ok(responseDTO);
        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IllegalArgumentException e) {
            log.error("Invalid file extension: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteImage(@RequestParam("fileUrl") String fileUrl) {
        String loginId = getCurrentMemberLoginId();
        Long memberId = updateMemberService.getMemberIdByLoginId(loginId);
        try {
            s3Service.deleteFile(fileUrl);
            updateMemberService.removeMemberProfileImage(memberId);
            return ResponseEntity.ok("File deleted successfully");
        } catch (AmazonServiceException e) {
            log.error("AmazonServiceException: {}", e.getErrorMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting file from S3");
        } catch (SdkClientException e) {
            log.error("SdkClientException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error communicating with S3");
        } catch (Exception e) {
            log.error("Error deleting file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }

    private String getCurrentMemberLoginId() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userDetails.getUsername();
        } catch (Exception e) {
            log.error("Error getting current member login ID: {}", e.getMessage());
            throw new SecurityException("Invalid or expired JWT token");
        }
    }
}
