package goorm_runner.backend.market.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarketDto {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private Integer price;
    private String category;
    private String status;
    private String delivery;
    private String imageUrl;

}
