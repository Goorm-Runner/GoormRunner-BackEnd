package goorm_runner.backend.picture.presentation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.security.SecurityMember;
import goorm_runner.backend.picture.application.S3Service;
import goorm_runner.backend.picture.application.UpdateMemberService;
import goorm_runner.backend.picture.dto.UpdateMemberRequestDTO;
import goorm_runner.backend.picture.dto.UpdateMemberResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    /**
     * 회원이 프로필 사진을 업로드합니다.
     */
    @PostMapping("/upload")
    public ResponseEntity<UpdateMemberResponseDTO> uploadImage(@ModelAttribute UpdateMemberRequestDTO requestDTO,
                                                               @AuthenticationPrincipal SecurityMember securityMember) {
        UpdateMemberResponseDTO responseDTO = new UpdateMemberResponseDTO();
        try {
            String loginId = securityMember.getUsername();
            log.info("Uploading image for username: {}", loginId);
            Long memberId = updateMemberService.getMemberIdByLoginId(loginId);
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

    /**
     *  회원이 프로필 사진을 삭제합니다.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<UpdateMemberResponseDTO> deleteImage(@RequestParam("fileUrl") String fileUrl,
                                                               @AuthenticationPrincipal SecurityMember securityMember) {
        UpdateMemberResponseDTO responseDTO = new UpdateMemberResponseDTO();
        try {
            String loginId = securityMember.getUsername();
            log.info("Deleting image for username: {}", loginId);
            Long memberId = updateMemberService.getMemberIdByLoginId(loginId);

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

    /**
     *  프로필 사진을 조회합니다.
     */
    @GetMapping("/profile-picture")
    public ResponseEntity<UpdateMemberResponseDTO> getProfilePictureUrl(
            @AuthenticationPrincipal SecurityMember securityMember
    ) {
        UpdateMemberResponseDTO responseDTO = new UpdateMemberResponseDTO();
        try {
            String loginId = securityMember.getUsername();
            log.info("Fetching profile picture for username: {}", loginId);

            Long memberId = updateMemberService.getMemberIdByLoginId(loginId);
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

}