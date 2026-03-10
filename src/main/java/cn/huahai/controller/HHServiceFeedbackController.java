package cn.huahai.controller;

import cn.huahai.entity.HHRegion;
import cn.huahai.entity.HHServiceFeedback;
import cn.huahai.service.HHRegionService;
import cn.huahai.service.HHServiceFeedbackService;
import cn.huahai.common.Result;
import cn.huahai.vo.HHServiceFeedbackVO; // 新增：引入包含图片的VO类
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 服务反馈 控制器（完整版：新增/编辑/删除/详情/列表 + 省市区地址自动拼接 + 分页查询 + 状态管理）
 */
@RestController
@RequestMapping("/api/service-feedback")
public class HHServiceFeedbackController {

    @Autowired
    private HHServiceFeedbackService serviceFeedbackService;

    // 注入地址服务，用于拼接省市区地址
    @Autowired
    private HHRegionService regionService;

    /**
     * 新增反馈（自动生成反馈单号、默认状态、自动填充时间、拼接完整地址）
     */
    @PostMapping("/add")
    public Result add(@RequestBody HHServiceFeedback serviceFeedback) {
        // 1. 必填项校验（避免空值入库）
        if (serviceFeedback.getContactName() == null || serviceFeedback.getContactName().trim().isEmpty()) {
            return Result.error("联系人姓名不能为空");
        }
        if (serviceFeedback.getContactPhone() == null || serviceFeedback.getContactPhone().trim().isEmpty()) {
            return Result.error("联系电话不能为空");
        }
        if (serviceFeedback.getIdentityCode() == null || serviceFeedback.getIdentityCode().trim().isEmpty()) {
            return Result.error("联系身份编码不能为空");
        }

        // 2. 自动生成反馈单号：FB_20260228_随机数
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        serviceFeedback.setFeedbackNo("FB_" + date + "_" + random);

        // 3. 默认状态：待处理（pending）
        if (serviceFeedback.getStatus() == null || serviceFeedback.getStatus().isEmpty()) {
            serviceFeedback.setStatus("pending");
        }

        // 4. 自动填充时间（创建+更新）
        serviceFeedback.setCreateTime(LocalDateTime.now());
        serviceFeedback.setUpdateTime(LocalDateTime.now());

        // 5. 核心：根据regionCode拼接省市区+详细地址，填充full_address
        String regionCode = serviceFeedback.getRegionCode();
        String detailAddress = serviceFeedback.getDetailAddress();
        if (regionCode != null && !regionCode.isEmpty()) {
            String fullRegionName = getFullRegionName(regionCode);
            String fullAddress = fullRegionName + (detailAddress != null ? "-" + detailAddress : "");
            serviceFeedback.setFullAddress(fullAddress);
        }

        boolean save = serviceFeedbackService.save(serviceFeedback);
        // 返回反馈ID（便于前端关联图片上传）
        return save ? Result.success("新增反馈成功", serviceFeedback.getFeedbackId()) : Result.error("新增反馈失败");
    }

    /**
     * 根据ID删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        boolean remove = serviceFeedbackService.removeById(id);
        return remove ? Result.success("删除反馈成功") : Result.error("删除反馈失败");
    }

    /**
     * 修改反馈（自动填充更新时间）
     */
    @PutMapping("/update")
    public Result update(@RequestBody HHServiceFeedback serviceFeedback) {
        // 自动填充更新时间
        serviceFeedback.setUpdateTime(LocalDateTime.now());
        boolean update = serviceFeedbackService.updateById(serviceFeedback);
        return update ? Result.success("修改反馈成功") : Result.error("修改反馈失败");
    }

    /**
     * 新增：更新反馈状态（专门用于状态管理）
     */
    @PutMapping("/update-status")
    public Result updateStatus(@RequestBody UpdateStatusDTO dto) {
        // 1. 参数校验
        if (dto.getFeedbackId() == null) {
            return Result.error("反馈ID不能为空");
        }
        if (dto.getStatus() == null || dto.getStatus().trim().isEmpty()) {
            return Result.error("处理状态不能为空");
        }
        // 校验状态值是否合法（仅支持pending/processing/completed）
        if (!"pending".equals(dto.getStatus()) && !"processing".equals(dto.getStatus()) && !"completed".equals(dto.getStatus())) {
            return Result.error("无效的状态值，仅支持：pending(待处理)、processing(处理中)、completed(已完成)");
        }

        // 2. 查询原记录
        HHServiceFeedback feedback = serviceFeedbackService.getById(dto.getFeedbackId());
        if (feedback == null) {
            return Result.error("反馈记录不存在");
        }

        // 3. 状态未变更时直接返回成功
        if (dto.getStatus().equals(feedback.getStatus())) {
            return Result.success("状态未变更，无需更新");
        }

        // 4. 更新状态和更新时间
        feedback.setStatus(dto.getStatus());
        feedback.setUpdateTime(LocalDateTime.now());
        boolean updated = serviceFeedbackService.updateById(feedback);

        return updated ? Result.success("状态更新成功") : Result.error("状态更新失败");
    }

    /**
     * 根据ID查询（详情/编辑回显）- 核心修改：返回包含图片的VO对象
     */
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id) {
        // 替换原方法：调用关联查询图片的方法
        HHServiceFeedbackVO serviceFeedback = serviceFeedbackService.getDetailWithImages(id);
        // 空值判断，提升接口健壮性
        if (serviceFeedback == null) {
            return Result.error("反馈记录不存在");
        }
        return Result.success(serviceFeedback);
    }

    /**
     * 分页查询列表（支持搜索条件）
     * @param pageNum 当前页码（默认1）
     * @param pageSize 每页条数（默认10）
     * @param keyword 搜索关键词（联系人/产品名称/反馈单号）
     * @param faultCode 故障类型编码
     * @param status 处理状态
     * @return 分页结果（包含当前页数据和总条数）
     */
    @GetMapping("/list")
    public Result list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String faultCode,
            @RequestParam(required = false) String status) {

        // 1. 创建分页对象
        Page<HHServiceFeedback> page = new Page<>(pageNum, pageSize);

        // 2. 构建查询条件
        QueryWrapper<HHServiceFeedback> wrapper = new QueryWrapper<>();

        // 搜索关键词：模糊匹配联系人、产品名称、反馈单号
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like("contact_name", keyword)
                    .or().like("product_name", keyword)
                    .or().like("feedback_no", keyword));
        }

        // 故障类型编码：精确匹配
        if (faultCode != null && !faultCode.trim().isEmpty()) {
            wrapper.eq("fault_code", faultCode);
        }

        // 处理状态：精确匹配
        if (status != null && !status.trim().isEmpty()) {
            wrapper.eq("status", status);
        }

        // 按创建时间降序排列（最新的在前面）
        wrapper.orderByDesc("create_time");

        // 3. 执行分页查询（调用MyBatis-Plus的分页方法）
        Page<HHServiceFeedback> resultPage = serviceFeedbackService.page(page, wrapper);

        // 4. 构造分页返回结果（适配前端格式）
        // 关键修复：添加Long -> int的类型转换，解决参数类型不匹配问题
        return Result.success(new PageResult<>(
                resultPage.getRecords(),                          // 当前页数据列表
                resultPage.getTotal(),                            // 总记录数 (long类型，无需转换)
                (int) resultPage.getCurrent(),                     // 当前页码 (Long -> int 强制转换)
                (int) resultPage.getSize()                         // 每页条数 (Long -> int 强制转换)
        ));
    }

    /**
     * 私有辅助方法：递归获取省市区完整名称（如：北京市-市辖区-朝阳区）
     * 仅在当前控制器内部使用，不对外暴露
     */
    private String getFullRegionName(String nodeCode) {
        // 1. 空值校验
        if (nodeCode == null || nodeCode.trim().isEmpty()) {
            System.out.println("地址编码为空，终止递归");
            return "";
        }

        // 2. 查询当前地址节点
        HHRegion current = regionService.getByNodeCode(nodeCode);
        if (current == null) {
            System.out.println("未找到地址编码对应的节点：" + nodeCode);
            return "";
        }
        System.out.println("查询到地址节点 → 编码：" + nodeCode + "，名称：" + current.getNodeName() + "，父编码：" + current.getCodeParent());

        // 3. 递归查询父节点（终止条件：父编码为空/为0/等于自身）
        String parentName = "";
        if (current.getCodeParent() != null
                && !current.getCodeParent().trim().isEmpty()
                && !current.getCodeParent().equals(nodeCode)
                && !current.getCodeParent().equals("0")) {
            parentName = getFullRegionName(current.getCodeParent());
        }

        // 4. 拼接省市区名称
        return parentName.isEmpty() ? current.getNodeName() : parentName + "-" + current.getNodeName();
    }

    /**
     * 分页结果封装类（适配前端格式）
     */
    static class PageResult<T> {
        private List<T> records;  // 当前页数据
        private long total;       // 总记录数
        private int current;      // 当前页码
        private int size;         // 每页条数

        // 构造器：参数类型严格匹配（long total, int current, int size）
        public PageResult(List<T> records, long total, int current, int size) {
            this.records = records;
            this.total = total;
            this.current = current;
            this.size = size;
        }

        // Getter方法（前端需要读取这些字段，必须添加）
        public List<T> getRecords() {
            return records;
        }

        public long getTotal() {
            return total;
        }

        public int getCurrent() {
            return current;
        }

        public int getSize() {
            return size;
        }
    }

    /**
     * 新增：状态更新DTO类（接收前端传递的状态更新参数）
     */
    static class UpdateStatusDTO {
        private Long feedbackId;  // 反馈ID
        private String status;    // 新状态（pending/processing/completed）

        // Getter & Setter 方法（必须添加，否则无法接收前端参数）
        public Long getFeedbackId() {
            return feedbackId;
        }

        public void setFeedbackId(Long feedbackId) {
            this.feedbackId = feedbackId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

//package cn.huahai.controller;
//
//import cn.huahai.entity.HHRegion;
//import cn.huahai.entity.HHServiceFeedback;
//import cn.huahai.service.HHRegionService;
//import cn.huahai.service.HHServiceFeedbackService;
//import cn.huahai.common.Result;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.UUID;
//
///**
// * 服务反馈 控制器（完整版：新增/编辑/删除/详情/列表 + 省市区地址自动拼接 + 分页查询 + 状态管理）
// */
//@RestController
//@RequestMapping("/api/service-feedback")
//public class HHServiceFeedbackController {
//
//    @Autowired
//    private HHServiceFeedbackService serviceFeedbackService;
//
//    // 注入地址服务，用于拼接省市区地址
//    @Autowired
//    private HHRegionService regionService;
//
//    /**
//     * 新增反馈（自动生成反馈单号、默认状态、自动填充时间、拼接完整地址）
//     */
//    @PostMapping("/add")
//    public Result add(@RequestBody HHServiceFeedback serviceFeedback) {
//        // 1. 必填项校验（避免空值入库）
//        if (serviceFeedback.getContactName() == null || serviceFeedback.getContactName().trim().isEmpty()) {
//            return Result.error("联系人姓名不能为空");
//        }
//        if (serviceFeedback.getContactPhone() == null || serviceFeedback.getContactPhone().trim().isEmpty()) {
//            return Result.error("联系电话不能为空");
//        }
//        if (serviceFeedback.getIdentityCode() == null || serviceFeedback.getIdentityCode().trim().isEmpty()) {
//            return Result.error("联系身份编码不能为空");
//        }
//
//        // 2. 自动生成反馈单号：FB_20260228_随机数
//        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        String random = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
//        serviceFeedback.setFeedbackNo("FB_" + date + "_" + random);
//
//        // 3. 默认状态：待处理（pending）
//        if (serviceFeedback.getStatus() == null || serviceFeedback.getStatus().isEmpty()) {
//            serviceFeedback.setStatus("pending");
//        }
//
//        // 4. 自动填充时间（创建+更新）
//        serviceFeedback.setCreateTime(LocalDateTime.now());
//        serviceFeedback.setUpdateTime(LocalDateTime.now());
//
//        // 5. 核心：根据regionCode拼接省市区+详细地址，填充full_address
//        String regionCode = serviceFeedback.getRegionCode();
//        String detailAddress = serviceFeedback.getDetailAddress();
//        if (regionCode != null && !regionCode.isEmpty()) {
//            String fullRegionName = getFullRegionName(regionCode);
//            String fullAddress = fullRegionName + (detailAddress != null ? "-" + detailAddress : "");
//            serviceFeedback.setFullAddress(fullAddress);
//        }
//
//        boolean save = serviceFeedbackService.save(serviceFeedback);
//        // 返回反馈ID（便于前端关联图片上传）
//        return save ? Result.success("新增反馈成功", serviceFeedback.getFeedbackId()) : Result.error("新增反馈失败");
//    }
//
//    /**
//     * 根据ID删除
//     */
//    @DeleteMapping("/delete/{id}")
//    public Result delete(@PathVariable Long id) {
//        boolean remove = serviceFeedbackService.removeById(id);
//        return remove ? Result.success("删除反馈成功") : Result.error("删除反馈失败");
//    }
//
//    /**
//     * 修改反馈（自动填充更新时间）
//     */
//    @PutMapping("/update")
//    public Result update(@RequestBody HHServiceFeedback serviceFeedback) {
//        // 自动填充更新时间
//        serviceFeedback.setUpdateTime(LocalDateTime.now());
//        boolean update = serviceFeedbackService.updateById(serviceFeedback);
//        return update ? Result.success("修改反馈成功") : Result.error("修改反馈失败");
//    }
//
//    /**
//     * 新增：更新反馈状态（专门用于状态管理）
//     */
//    @PutMapping("/update-status")
//    public Result updateStatus(@RequestBody UpdateStatusDTO dto) {
//        // 1. 参数校验
//        if (dto.getFeedbackId() == null) {
//            return Result.error("反馈ID不能为空");
//        }
//        if (dto.getStatus() == null || dto.getStatus().trim().isEmpty()) {
//            return Result.error("处理状态不能为空");
//        }
//        // 校验状态值是否合法（仅支持pending/processing/completed）
//        if (!"pending".equals(dto.getStatus()) && !"processing".equals(dto.getStatus()) && !"completed".equals(dto.getStatus())) {
//            return Result.error("无效的状态值，仅支持：pending(待处理)、processing(处理中)、completed(已完成)");
//        }
//
//        // 2. 查询原记录
//        HHServiceFeedback feedback = serviceFeedbackService.getById(dto.getFeedbackId());
//        if (feedback == null) {
//            return Result.error("反馈记录不存在");
//        }
//
//        // 3. 状态未变更时直接返回成功
//        if (dto.getStatus().equals(feedback.getStatus())) {
//            return Result.success("状态未变更，无需更新");
//        }
//
//        // 4. 更新状态和更新时间
//        feedback.setStatus(dto.getStatus());
//        feedback.setUpdateTime(LocalDateTime.now());
//        boolean updated = serviceFeedbackService.updateById(feedback);
//
//        return updated ? Result.success("状态更新成功") : Result.error("状态更新失败");
//    }
//
//    /**
//     * 根据ID查询（详情/编辑回显）
//     */
//    @GetMapping("/get/{id}")
//    public Result getById(@PathVariable Long id) {
//        HHServiceFeedback serviceFeedback = serviceFeedbackService.getById(id);
//        return Result.success(serviceFeedback);
//    }
//
//    /**
//     * 分页查询列表（支持搜索条件）
//     * @param pageNum 当前页码（默认1）
//     * @param pageSize 每页条数（默认10）
//     * @param keyword 搜索关键词（联系人/产品名称/反馈单号）
//     * @param faultCode 故障类型编码
//     * @param status 处理状态
//     * @return 分页结果（包含当前页数据和总条数）
//     */
//    @GetMapping("/list")
//    public Result list(
//            @RequestParam(defaultValue = "1") Integer pageNum,
//            @RequestParam(defaultValue = "10") Integer pageSize,
//            @RequestParam(required = false) String keyword,
//            @RequestParam(required = false) String faultCode,
//            @RequestParam(required = false) String status) {
//
//        // 1. 创建分页对象
//        Page<HHServiceFeedback> page = new Page<>(pageNum, pageSize);
//
//        // 2. 构建查询条件
//        QueryWrapper<HHServiceFeedback> wrapper = new QueryWrapper<>();
//
//        // 搜索关键词：模糊匹配联系人、产品名称、反馈单号
//        if (keyword != null && !keyword.trim().isEmpty()) {
//            wrapper.and(w -> w.like("contact_name", keyword)
//                    .or().like("product_name", keyword)
//                    .or().like("feedback_no", keyword));
//        }
//
//        // 故障类型编码：精确匹配
//        if (faultCode != null && !faultCode.trim().isEmpty()) {
//            wrapper.eq("fault_code", faultCode);
//        }
//
//        // 处理状态：精确匹配
//        if (status != null && !status.trim().isEmpty()) {
//            wrapper.eq("status", status);
//        }
//
//        // 按创建时间降序排列（最新的在前面）
//        wrapper.orderByDesc("create_time");
//
//        // 3. 执行分页查询（调用MyBatis-Plus的分页方法）
//        Page<HHServiceFeedback> resultPage = serviceFeedbackService.page(page, wrapper);
//
//        // 4. 构造分页返回结果（适配前端格式）
//        // 关键修复：添加Long -> int的类型转换，解决参数类型不匹配问题
//        return Result.success(new PageResult<>(
//                resultPage.getRecords(),                          // 当前页数据列表
//                resultPage.getTotal(),                            // 总记录数 (long类型，无需转换)
//                (int) resultPage.getCurrent(),                     // 当前页码 (Long -> int 强制转换)
//                (int) resultPage.getSize()                         // 每页条数 (Long -> int 强制转换)
//        ));
//    }
//
//    /**
//     * 私有辅助方法：递归获取省市区完整名称（如：北京市-市辖区-朝阳区）
//     * 仅在当前控制器内部使用，不对外暴露
//     */
//    private String getFullRegionName(String nodeCode) {
//        // 1. 空值校验
//        if (nodeCode == null || nodeCode.trim().isEmpty()) {
//            System.out.println("地址编码为空，终止递归");
//            return "";
//        }
//
//        // 2. 查询当前地址节点
//        HHRegion current = regionService.getByNodeCode(nodeCode);
//        if (current == null) {
//            System.out.println("未找到地址编码对应的节点：" + nodeCode);
//            return "";
//        }
//        System.out.println("查询到地址节点 → 编码：" + nodeCode + "，名称：" + current.getNodeName() + "，父编码：" + current.getCodeParent());
//
//        // 3. 递归查询父节点（终止条件：父编码为空/为0/等于自身）
//        String parentName = "";
//        if (current.getCodeParent() != null
//                && !current.getCodeParent().trim().isEmpty()
//                && !current.getCodeParent().equals(nodeCode)
//                && !current.getCodeParent().equals("0")) {
//            parentName = getFullRegionName(current.getCodeParent());
//        }
//
//        // 4. 拼接省市区名称
//        return parentName.isEmpty() ? current.getNodeName() : parentName + "-" + current.getNodeName();
//    }
//
//    /**
//     * 分页结果封装类（适配前端格式）
//     */
//    static class PageResult<T> {
//        private List<T> records;  // 当前页数据
//        private long total;       // 总记录数
//        private int current;      // 当前页码
//        private int size;         // 每页条数
//
//        // 构造器：参数类型严格匹配（long total, int current, int size）
//        public PageResult(List<T> records, long total, int current, int size) {
//            this.records = records;
//            this.total = total;
//            this.current = current;
//            this.size = size;
//        }
//
//        // Getter方法（前端需要读取这些字段，必须添加）
//        public List<T> getRecords() {
//            return records;
//        }
//
//        public long getTotal() {
//            return total;
//        }
//
//        public int getCurrent() {
//            return current;
//        }
//
//        public int getSize() {
//            return size;
//        }
//    }
//
//    /**
//     * 新增：状态更新DTO类（接收前端传递的状态更新参数）
//     */
//    static class UpdateStatusDTO {
//        private Long feedbackId;  // 反馈ID
//        private String status;    // 新状态（pending/processing/completed）
//
//        // Getter & Setter 方法（必须添加，否则无法接收前端参数）
//        public Long getFeedbackId() {
//            return feedbackId;
//        }
//
//        public void setFeedbackId(Long feedbackId) {
//            this.feedbackId = feedbackId;
//        }
//
//        public String getStatus() {
//            return status;
//        }
//
//        public void setStatus(String status) {
//            this.status = status;
//        }
//    }
//}