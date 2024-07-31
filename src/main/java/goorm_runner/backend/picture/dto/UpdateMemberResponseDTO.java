package goorm_runner.backend.picture.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class UpdateMemberResponseDTO {
    private String status;
    private String message;
    private Map<String, Object> data;
}
