package top.dongxiaohao.common.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import top.dongxiaohao.common.dto.ResponseResult;
import top.dongxiaohao.common.enums.ResponseEnum;
import top.dongxiaohao.common.interceptor.CommonRestInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义返回结果：未登录或登录过期
 * Created by macro on 2018/5/14.
 */
@Component
public class RestAuthenticationEntryPoint extends CommonRestInterceptor implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //ApiResult result = new ApiResult();
        if (response.getStatus() == 9999) {
            ResponseResult result = new ResponseResult();
            result.setSuccess(false);
            result.setMsg(ResponseEnum.UNAUTHORIZED.getMsg());
            result.setCode(ResponseEnum.UNAUTHORIZED.getCode());
            Object error = request.getSession().getAttribute("error");
            result.setMsg(error.toString());
            respond(result, response);
        } else {
            respond(new ResponseResult(false,
                    ResponseEnum.UNAUTHORIZED.getCode(),
                    ResponseEnum.UNAUTHORIZED.getMsg(),
                    null
            ), response);
        }//respond(result,response);
    }
}
