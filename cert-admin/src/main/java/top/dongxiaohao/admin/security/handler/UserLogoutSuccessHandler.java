package top.dongxiaohao.admin.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import top.dongxiaohao.common.util.JwtTokenUtil;
import top.dongxiaohao.common.cache.UserTokenCache;
import top.dongxiaohao.common.dto.ResponseResult;
import top.dongxiaohao.common.enums.ResponseEnum;
import top.dongxiaohao.common.interceptor.CommonRestInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
public class UserLogoutSuccessHandler extends CommonRestInterceptor implements LogoutSuccessHandler {
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserTokenCache userTokenCache;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String token = jwtTokenUtil.getToken(request);
        if (Objects.nonNull(token)) {
            userTokenCache.deleteUserToken(token);
        }
        SecurityContextHolder.clearContext();
        respond(new ResponseResult<String>(true, ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), null), response);
    }
}
