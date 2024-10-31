package top.dongxiaohao.admin.controller;

import com.dtflys.forest.annotation.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dongxiaohao.common.cache.UserTokenCache;
import top.dongxiaohao.common.dto.BaseRestController;
import top.dongxiaohao.common.dto.ResponseResult;
import top.dongxiaohao.common.service.CommonSysUserService;
import top.dongxiaohao.common.util.JwtTokenUtil;

import javax.annotation.Resource;

/**
 * @Author: Dongxiaohao
 * @Date: 2024/10/31 23:30
 */

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController extends BaseRestController {
    private final CommonSysUserService commonSysUserService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserTokenCache userTokenCache;

    @RequestMapping("/test")
    public ResponseResult<String> test(){
        return success("你好");
    }
}
