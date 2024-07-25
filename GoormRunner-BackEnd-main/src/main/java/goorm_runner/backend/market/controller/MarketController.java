package goorm_runner.backend.market.controller;

import goorm_runner.backend.market.dto.MarketDto;
import goorm_runner.backend.market.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {
    private final MarketService marketService;

    @GetMapping("/write")
    public String writeForm() {
        return "write";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute MarketDto marketDto){
        System.out.println("marketDTO = "+ marketDto);
        marketService.write(marketDto);

        return "index";
    }
}
