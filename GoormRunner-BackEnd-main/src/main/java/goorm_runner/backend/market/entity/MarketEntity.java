package goorm_runner.backend.market.entity;

import goorm_runner.backend.market.dto.MarketDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Market")
public class MarketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer price;

    @Column(length = 50, nullable = false)
    private String category;

    @Column(length = 50, nullable = false)
    private String status;

    @Column(length = 50, nullable = false)
    private String delivery;

    @Column(name = "image_url")
    private String imageUrl;

    public static MarketEntity toSaveEntity(MarketDto marketDto){
        MarketEntity marketEntity = new MarketEntity();
        marketEntity.setMemberId(marketDto.getMemberId());
        marketEntity.setTitle(marketDto.getTitle());
        marketEntity.setContent(marketDto.getContent());
        marketEntity.setPrice(marketDto.getPrice());
        marketEntity.setCategory(marketDto.getCategory());
        marketEntity.setStatus(marketDto.getStatus());
        marketEntity.setDelivery(marketDto.getDelivery());
        marketEntity.setImageUrl(marketDto.getImageUrl());
        return marketEntity;

    }
}
