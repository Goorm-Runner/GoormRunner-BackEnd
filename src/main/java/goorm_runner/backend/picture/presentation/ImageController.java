package goorm_runner.backend.picture.presentation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import goorm_runner.backend.member.domain.Member;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/picture")
public class ImageController {
    private final S3Service s3Service;
    private final UpdateMemberService updateMemberService;

    @PostMapping("/upload")
    public ResponseEntity<UpdateMemberResponseDTO> uploadImage(@ModelAttribute UpdateMemberRequestDTO requestDTO) {
        UpdateMemberResponseDTO responseDTO = new UpdateMemberResponseDTO();
        try {
            String username = getCurrentMemberUsername();
            log.info("Uploading image for username: {}", username);
            Long memberId = updateMemberService.getMemberIdByUsername(username);
            log.info("Found memberId: {}", memberId);
            String fileUrl = s3Service.uploadFile(requestDTO.getFile(), memberId);
            updateMemberService.updateMemberProfileImage(memberId, fileUrl);

            responseDTO.setStatus("success");
            responseDTO.setMessage("Image uploaded successfully");
            Map<String, Object> data = new HashMap<>();
            data.put("fileUrl", fileUrl);
            responseDTO.setData(data);

            return ResponseEntity.ok(responseDTO);
        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
            responseDTO.setStatus("error");
            responseDTO.setMessage("Error uploading file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        } catch (IllegalArgumentException e) {
            log.error("Invalid file extension: {}", e.getMessage());
            responseDTO.setStatus("error");
            responseDTO.setMessage("Invalid file extension");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        } catch (RuntimeException e) {
            log.error("Error updating member profile image: {}", e.getMessage());
            responseDTO.setStatus("error");
            responseDTO.setMessage("Member not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<UpdateMemberResponseDTO> deleteImage(@RequestParam("fileUrl") String fileUrl) {
        UpdateMemberResponseDTO responseDTO = new UpdateMemberResponseDTO();
        try {
            String username = getCurrentMemberUsername();
            log.info("Deleting image for username: {}", username);
            Long memberId = updateMemberService.getMemberIdByUsername(username);

            s3Service.deleteFile(fileUrl);
            updateMemberService.removeMemberProfileImage(memberId);

            responseDTO.setStatus("success");
            responseDTO.setMessage("File deleted successfully");

            return ResponseEntity.ok(responseDTO);
        } catch (AmazonServiceException e) {
            log.error("AmazonServiceException: {}", e.getErrorMessage());

            responseDTO.setStatus("error");
            responseDTO.setMessage("Error deleting file from S3");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        } catch (SdkClientException e) {
            log.error("SdkClientException: {}", e.getMessage());

            responseDTO.setStatus("error");
            responseDTO.setMessage("Error communicating with S3");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        } catch (RuntimeException e) {
            log.error("Error removing member profile image: {}", e.getMessage());

            responseDTO.setStatus("error");
            responseDTO.setMessage("Member not found");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        }
    }

    @GetMapping("/profile-picture")
    public ResponseEntity<UpdateMemberResponseDTO> getProfilePictureUrl() {
        UpdateMemberResponseDTO responseDTO = new UpdateMemberResponseDTO();
        try {
            String username = getCurrentMemberUsername();
            log.info("Fetching profile picture for username: {}", username);

            Long memberId = updateMemberService.getMemberIdByUsername(username);
            Member member = updateMemberService.getMemberById(memberId);
            String fileUrl = member.getProfilePictureUrl();

            if (fileUrl == null) {
                responseDTO.setStatus("error");
                responseDTO.setMessage("Profile picture not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
            }

            responseDTO.setStatus("success");
            responseDTO.setMessage("Profile picture fetched successfully");
            Map<String, Object> data = new HashMap<>();
            data.put("fileUrl", fileUrl);
            responseDTO.setData(data);

            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            log.error("Error fetching profile picture: {}", e.getMessage());

            responseDTO.setStatus("error");
            responseDTO.setMessage("Member not found");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        }
    }

    private String getCurrentMemberUsername() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            log.info("Current member username: {}", userDetails.getUsername());

            return userDetails.getUsername();
        } catch (Exception e) {
            log.error("Error getting current member username: {}", e.getMessage());
            throw new SecurityException("Invalid or expired JWT token");
        }
    }
}