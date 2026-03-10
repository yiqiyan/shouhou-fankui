package cn.huahai.controller;

import cn.huahai.entity.HHFeedbackImage;
import cn.huahai.service.HHFeedbackImageService;
import cn.huahai.util.LocalFileUploadUtil;
import cn.huahai.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 反馈图片 控制器（最终适配版，完全匹配实体类字段）
 */
@Slf4j
@RestController
@RequestMapping("/api/feedback-image")
public class HHFeedbackImageController {

    @Autowired
    private HHFeedbackImageService feedbackImageService;

    // 注入本地文件上传工具类
    @Autowired
    private LocalFileUploadUtil localFileUploadUtil;

    /**
     * 图片上传核心接口（仅保留实体类存在的字段）
     * @param file 上传的图片文件
     * @param feedbackId 关联的反馈主表ID
     * @return 上传结果+图片访问URL
     */
    @PostMapping("/upload")
    public Result upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("feedbackId") Long feedbackId
    ) {
        try {
            // 1. 基础参数校验
            if (file.isEmpty()) {
                return Result.error("请选择要上传的图片文件");
            }
            if (feedbackId == null || feedbackId <= 0) {
                return Result.error("反馈主表ID不能为空且必须为正整数");
            }

            // 2. 图片格式校验（仅允许jpg/png/jpeg）
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                return Result.error("文件格式无效，请上传jpg/png/jpeg格式图片");
            }
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            if (!suffix.matches("\\.(jpg|png|jpeg)$")) {
                return Result.error("仅支持jpg/png/jpeg格式图片，当前文件格式：" + suffix);
            }

            // 3. 调用工具类上传图片到本地服务器
            String[] uploadResult = localFileUploadUtil.upload(file);
            String imageUrl = uploadResult[0]; // 图片访问URL（核心）

            // 4. 构建实体类（仅使用实体类存在的字段）
            HHFeedbackImage feedbackImage = new HHFeedbackImage();
            feedbackImage.setFeedbackId(feedbackId);    // 关联反馈主表ID（必传）
            feedbackImage.setImageUrl(imageUrl);        // 图片访问URL（核心）
            feedbackImage.setUploadTime(LocalDateTime.now()); // 上传时间（替换原createTime）

            // 5. 保存到数据库
            boolean saveSuccess = feedbackImageService.save(feedbackImage);
            if (!saveSuccess) {
                return Result.error("图片上传成功，但数据库保存失败");
            }

            log.info("图片上传成功，feedbackId：{}，图片访问URL：{}", feedbackId, imageUrl);
            return Result.success("图片上传成功", imageUrl);
        } catch (Exception e) {
            log.error("图片上传失败", e);
            return Result.error("图片上传失败：" + e.getMessage());
        }
    }

    /**
     * 🔴 新增：根据反馈ID查询图片列表（适配详情页，仅返回当前反馈的图片）
     * 解决大量图片下前端筛选卡顿的问题，数据库层面过滤，性能最优
     */
    @GetMapping("/list/{feedbackId}")
    public Result listByFeedbackId(@PathVariable Long feedbackId) {
        try {
            // 基础参数校验
            if (feedbackId == null || feedbackId <= 0) {
                return Result.error("反馈主表ID不能为空且必须为正整数");
            }

            // 仅查询当前反馈ID关联的图片，而非全表查询
            List<HHFeedbackImage> imageList = feedbackImageService.lambdaQuery()
                    .eq(HHFeedbackImage::getFeedbackId, feedbackId)
                    .list();

            log.info("查询反馈ID{}的图片，共{}张", feedbackId, imageList.size());
            return Result.success(imageList);
        } catch (Exception e) {
            log.error("根据反馈ID查询图片失败", e);
            return Result.error("查询图片失败：" + e.getMessage());
        }
    }

    /**
     * 新增（手动录入图片信息，非上传）
     */
    @PostMapping("/add")
    public Result add(@RequestBody HHFeedbackImage feedbackImage) {
        boolean save = feedbackImageService.save(feedbackImage);
        return save ? Result.success("新增成功") : Result.error("新增失败");
    }

    /**
     * 根据ID删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        boolean remove = feedbackImageService.removeById(id);
        return remove ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody HHFeedbackImage feedbackImage) {
        boolean update = feedbackImageService.updateById(feedbackImage);
        return update ? Result.success("修改成功") : Result.error("修改失败");
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id) {
        HHFeedbackImage feedbackImage = feedbackImageService.getById(id);
        return Result.success(feedbackImage);
    }

    /**
     * 查询所有
     */
    @GetMapping("/list")
    public Result list() {
        List<HHFeedbackImage> list = feedbackImageService.list();
        return Result.success(list);
    }
}