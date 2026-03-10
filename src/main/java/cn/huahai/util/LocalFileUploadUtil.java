package cn.huahai.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class LocalFileUploadUtil {

    @Value("${upload.local-path}")
    private String localPath;

    @Value("${upload.base-url}")
    private String baseUrl;

    public String[] upload(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("上传文件不能为空");
            }

            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (!suffix.matches("\\.(jpg|png|jpeg)$")) {
                throw new RuntimeException("仅支持jpg/png/jpeg格式图片");
            }

//            String fileName = UUID.randomUUID().toString() + suffix;
//            File destFile = new File(localPath + fileName);
            String fileName = UUID.randomUUID().toString() + suffix;
            File destFile = new File(localPath + fileName); // 注意：localPath 末尾最好带 /

            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }

            file.transferTo(destFile);
            String imageUrl = baseUrl + fileName;
            log.info("图片上传成功，本地路径：{}，访问URL：{}", destFile.getAbsolutePath(), imageUrl);

            return new String[]{imageUrl, fileName};
        } catch (IOException e) {
            log.error("图片保存失败", e);
            throw new RuntimeException("图片上传失败，请重试");
        }
    }
}