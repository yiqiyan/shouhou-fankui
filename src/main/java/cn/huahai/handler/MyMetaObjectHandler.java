package cn.huahai.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    // 新增时只填充创建时间（如果需要），完全移除 faultCode 填充
    @Override
    public void insertFill(MetaObject metaObject) {
        // 如果你需要自动填充创建时间，添加这行；不需要就留空
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        // 👇 删掉下面这些生成 faultCode 的代码 👇
        // String maxCode = "000";
        // int newNum = Integer.parseInt(maxCode) + 1;
        // String newCode = String.format("FT_%03d", newNum);
        // this.setFieldValByName("faultCode", newCode, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {}
}