package goorm_runner.backend.picture.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateMemberRequestDTO {
    private MultipartFile file;
}
