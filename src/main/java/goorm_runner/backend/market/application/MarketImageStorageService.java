package goorm_runner.backend.market.application;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class MarketImageStorageService {

    private static final String IMAGE_DIRECTORY = "uploaded-images/";

    public String saveImage(MultipartFile image) throws IOException {
        File directory = new File(IMAGE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 파일 이름에 UUID 추가하여 중복 방지
        String filename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        File saveFile = new File(IMAGE_DIRECTORY + filename);

        // 파일 저장
        image.transferTo(saveFile);

        return saveFile.getPath();
    }
}
