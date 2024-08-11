package goorm_runner.backend.post.domain.model;

public enum Category {
    GENERAL("자유게시판"),
    TIP("꿀팁게시판"),
    RESTAURANT("맛집게시판"),
    LIVE("실시간 직관 모집");


    private final String title;

    Category(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}