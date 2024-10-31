package top.dongxiaohao.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Dongxiaohao
 * @Date: 2024/10/31 22:28
 */
public class SysUserEnum {

    @Getter
    @AllArgsConstructor
    public enum Status {
        ENABLE(0, "启用"),
        DISABLE(1, "禁用");

        private Integer code;
        private String desc;
    }
}
