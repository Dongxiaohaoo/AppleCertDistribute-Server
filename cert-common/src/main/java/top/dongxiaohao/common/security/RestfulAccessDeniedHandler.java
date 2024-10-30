package top.dongxiaohao.common.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import top.dongxiaohao.common.dto.ResponseResult;
import top.dongxiaohao.common.enums.ResponseEnum;
import top.dongxiaohao.common.interceptor.CommonRestInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义返回结果：没有权限访问时
 * Created by macro on 2018/4/26.
 */
@Component
public class RestfulAccessDeniedHandler extends CommonRestInterceptor implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        respond(new ResponseResult(false,
                ResponseEnum.NO_PERMISSION.getCode(),
                ResponseEnum.NO_PERMISSION.getMsg(),
                null
        ), response);
    }
}
