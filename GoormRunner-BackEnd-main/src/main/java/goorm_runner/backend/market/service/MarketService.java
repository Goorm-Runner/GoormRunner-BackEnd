package goorm_runner.backend.market.service;

import goorm_runner.backend.market.dto.MarketDto;
import goorm_runner.backend.market.entity.MarketEntity;
import goorm_runner.backend.market.repository.MarketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketService {

    private final MarketRepository marketRepository;
    public void write(MarketDto marketDto) {
        MarketEntity marketEntity = MarketEntity.toSaveEntity(marketDto);
        marketRepository.save(marketEntity);
    }
}
