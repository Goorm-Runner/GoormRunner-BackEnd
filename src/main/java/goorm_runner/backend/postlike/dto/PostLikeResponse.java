package goorm_runner.backend.postlike.dto;

public record PostLikeResponse(boolean success) {
    public static PostLikeResponse succeed() {
        return new PostLikeResponse(true);
    }
}
