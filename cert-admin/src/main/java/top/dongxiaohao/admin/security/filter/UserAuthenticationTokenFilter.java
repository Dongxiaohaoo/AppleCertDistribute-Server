package top.dongxiaohao.admin.security.filter;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import top.dongxiaohao.common.util.JwtTokenUtil;
import top.dongxiaohao.common.cache.UserTokenCache;
import top.dongxiaohao.common.entity.SysUser;
import top.dongxiaohao.common.enums.SysUserEnum;
import top.dongxiaohao.common.service.CommonSysUserService;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Objects;

public class UserAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserTokenCache userTokenCache;
    @Resource
    private CommonSysUserService commonSysUserService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept,Authorization,token");

        String token = jwtTokenUtil.getToken(request);
        if (Objects.nonNull(token)) {
            Integer userId = userTokenCache.getUserId(token);
            if (userId != null) {
                QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>(
                        new SysUser().setUserId(userId).setDisable(SysUserEnum.Status.ENABLE.getCode()));
                SysUser queryUser = commonSysUserService.getOne(queryWrapper);
                if (Objects.nonNull(queryUser)) {
                    userTokenCache.refreshTokenExpireTime(token); // 刷新过期时间
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(queryUser, AuthorityUtils.createAuthorityList(), queryUser.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}