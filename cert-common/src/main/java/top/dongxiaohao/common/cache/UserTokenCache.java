package top.dongxiaohao.common.cache;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.dongxiaohao.common.JwtTokenUtil;
import top.dongxiaohao.common.cache.base.AbstractRedisCache;
import top.dongxiaohao.common.constant.RedisKey;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Dongxiaohao
 * @Date: 2024/10/30 22:54
 */
@Component
@RequiredArgsConstructor
public class UserTokenCache extends AbstractRedisCache {

    private static final String key = RedisKey.USER_TOKEN;
    private final JwtTokenUtil jwtTokenUtil;
    private final static long timeOut = 1000 * 60 * 1440L; //15分钟不操作，强制退出

    @Override
    public void initCache() {

    }

    private String cacheKey(String... values) {
        StringBuilder builder = new StringBuilder();
        builder.append(key);
        for (String value : values) {
            builder.append(":");
            builder.append(value);
        }
        return builder.toString().toLowerCase();
    }

    public String createUserToken(Integer userId) {
        if (userId == null)
            return null;
        String uuid = IdUtil.simpleUUID();
        String token = jwtTokenUtil.createToken(uuid);
        setValue(cacheKey(uuid), userId, timeOut);
        return token;
    }

    public void deleteUserToken(String token) {
        String uuid = jwtTokenUtil.getSubjectFromToken(token);
        if (StringUtils.isNotBlank(uuid))
            deleteValue(cacheKey(uuid));
    }

    public Integer getUserId(String token) {
        String uuid = jwtTokenUtil.getSubjectFromToken(token);
        if (StringUtils.isNotBlank(uuid)) {
            Integer userId = getValue(cacheKey(uuid));
            if (userId != null)
                return userId;
        }

        return null;
    }

    public void refreshTokenExpireTime(String token) {
        String uuid = jwtTokenUtil.getSubjectFromToken(token);
        if (StringUtils.isNotBlank(uuid))
            this.refreshKey(cacheKey(uuid), timeOut, TimeUnit.MINUTES.toMillis(10));
    }


}
