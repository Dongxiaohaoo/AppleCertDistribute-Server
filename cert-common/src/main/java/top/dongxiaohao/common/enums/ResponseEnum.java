package top.dongxiaohao.common.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @Author: Dongxiaohao
 * @Date: 2024/10/30 22:26
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {
    SUCCESS(200, "成功"),
    FAIL(500, "失败"),
    UNAUTHORIZED(401, "未登录"),
    NOT_FOUND(404, "请求路径不存在"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    PARAM_ERROR(400, "参数错误"),
    NO_PERMISSION(403, "没有权限"),
    ;

    private final int code;
    private final String msg;
}
