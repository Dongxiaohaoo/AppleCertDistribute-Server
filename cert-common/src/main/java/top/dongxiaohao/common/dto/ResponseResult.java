package top.dongxiaohao.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Dongxiaohao
 * @Date: 2024/10/30 22:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> {
    private boolean success;
    private int code;
    private String msg;
    private T data;
}
