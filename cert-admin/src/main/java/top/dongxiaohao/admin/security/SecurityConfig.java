package top.dongxiaohao.admin.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import top.dongxiaohao.admin.constant.SysUrlCtl;
import top.dongxiaohao.admin.security.filter.UserAuthenticationTokenFilter;
import top.dongxiaohao.admin.security.handler.UserLogoutSuccessHandler;
import top.dongxiaohao.common.constant.DefaultUri;
import top.dongxiaohao.common.security.AbstractSecurityConfig;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends AbstractSecurityConfig {

    @Override
    public String logoutUrl() {
        return DefaultUri.ADMIN_API + SysUrlCtl.SYS_USER_API + "/logout.htm";
    }

    @Override
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new UserLogoutSuccessHandler();
    }

    @Override
    public Filter jwtAuthenticationTokenFilter() {
        return new UserAuthenticationTokenFilter();
    }
}
