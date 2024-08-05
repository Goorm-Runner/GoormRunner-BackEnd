package goorm_runner.backend.postlike.dto;

public record PostLikeResponse(boolean success) {
    public static PostLikeResponse successResponse() {
        return new PostLikeResponse(true);
    }
}
