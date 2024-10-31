package top.dongxiaohao.admin.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.dongxiaohao.common.entity.SysUser;

import java.util.Objects;
import java.util.Optional;


public class SecurityUtils {

    public static SysUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = Optional.ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        return Optional.ofNullable(principal)
                .map(item -> (SysUser) item).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    public static Integer getUserId() {
        SysUser currentUser = getCurrentUser();
        if (Objects.isNull(currentUser)) {
            return null;
        }
        return currentUser.getUserId();
    }

}
