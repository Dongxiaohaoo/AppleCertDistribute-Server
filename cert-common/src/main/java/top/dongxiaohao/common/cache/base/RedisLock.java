package top.dongxiaohao.common.cache.base;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author dongxiaohao
 */
@Component
public class RedisLock {
    private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final long LOCK_TRY_TIMEOUT = 10000L;
    private static final long LOCK_TRY_INTERVAL = 100L;
    private static final long LOCK_EXPIRE = 15000L;
    private static final boolean WAIT = true;
    private static final Long SUCCESS = 1L;
    private static final DefaultRedisScript<Long> LOCK_SCRIPT = new DefaultRedisScript<>("local success = redis.call('set', KEYS[1], ARGV[1], 'NX', 'PX', ARGV[2]) if (success) then return 1 else return 0 end", Long.class);
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT = new DefaultRedisScript<>("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end", Long.class);

    public String tryLock(String lock) {
        return getLock(lock, LOCK_TRY_TIMEOUT, LOCK_TRY_INTERVAL, LOCK_EXPIRE, WAIT);
    }

    public String tryLock(String lock, boolean wait) {
        return getLock(lock, LOCK_TRY_TIMEOUT, LOCK_TRY_INTERVAL, LOCK_EXPIRE, wait);
    }

    public String tryLock(String lock, long timeout, boolean wait) {
        return getLock(lock, timeout, LOCK_TRY_INTERVAL, LOCK_EXPIRE, wait);
    }

    public String tryLock(String lock, long timeout, long tryInterval, boolean wait) {
        return getLock(lock, timeout, tryInterval, LOCK_EXPIRE, wait);
    }

    public String tryLock(String lock, long timeout, long tryInterval, long lockExpireTime, boolean wait) {
        return getLock(lock, timeout, tryInterval, lockExpireTime, wait);
    }

    public long getCurrentRedisTime() {
        Long result = this.redisTemplate.execute(RedisServerCommands::time);
        return Objects.requireNonNull(result);
    }

    private String getLock(String lock, long timeout, long tryInterval, long lockExpireTime, boolean wait) {
        try {
            if (StrUtil.isBlank(lock)) {
                return null;
            }
            long totalTime;
            long startTime = getCurrentRedisTime();
            String token = UUID.randomUUID().toString();
            do {
                List<String> keys = Collections.singletonList(lock);
                Long status = this.redisTemplate.execute(LOCK_SCRIPT, keys, token, lockExpireTime);
                if (Objects.equals(SUCCESS, status)) {
                    return token;
                }
                if (!wait) {
                    return null;
                }
                Thread.sleep(tryInterval);
                totalTime = getCurrentRedisTime() - startTime;
            } while (totalTime <= timeout);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
        return null;
    }

    public void tryUnLock(String lock, String token) {
        if (lock != null && !StrUtil.isBlank(lock)) {
            List<String> keys = Collections.singletonList(lock);
            this.redisTemplate.execute(UNLOCK_SCRIPT, keys, token);
        }
    }

}
