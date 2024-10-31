package top.dongxiaohao.common.dto;

import top.dongxiaohao.common.enums.ResponseEnum;

/**
 * @Author: Dongxiaohao
 * @Date: 2024/10/31 23:34
 */
public class BaseRestController {
    public ResponseResult<String> success() {
        return new ResponseResult<>(true, ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), null);
    }

    public <T> ResponseResult<T> success(String message) {
        return new ResponseResult<>(true, ResponseEnum.SUCCESS.getCode(), message, null);
    }

    public <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(true, ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), data);
    }

    public <T> ResponseResult<T> error() {
        return new ResponseResult<>(false, ResponseEnum.FAIL.getCode(), ResponseEnum.FAIL.getMsg(), null);
    }

    public <T> ResponseResult<T> error(String msg) {
        return new ResponseResult<>(false, ResponseEnum.FAIL.getCode(), msg, null);
    }

    public <T> ResponseResult<T> error(T data) {
        return new ResponseResult<>(false, ResponseEnum.FAIL.getCode(), ResponseEnum.FAIL.getMsg(), data);
    }
}
