package top.dongxiaohao.common.constant;

/**
 * @Author: Dongxiaohao
 * @Date: 2024/10/30 22:55
 */
public interface RedisKey {
    String CACHE_KEY_NAMESPACE = "apple_cert";
    String USER_TOKEN = CACHE_KEY_NAMESPACE + ":" + "user_token";
}
