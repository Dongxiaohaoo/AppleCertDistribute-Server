package top.dongxiaohao.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.dongxiaohao.common.dto.ResponseResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Dongxiaohao
 * @Date: 2024/10/30 22:28
 */
public class CommonRestInterceptor extends HandlerInterceptorAdapter {
    protected void respond(ResponseResult result, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        ObjectMapper Obj = new ObjectMapper();
        response.getWriter().write(Obj.writeValueAsString(result));
    }
}
