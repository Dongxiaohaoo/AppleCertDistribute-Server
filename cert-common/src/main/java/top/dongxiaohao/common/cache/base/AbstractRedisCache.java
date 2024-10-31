package top.dongxiaohao.common.cache.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class AbstractRedisCache {

    private static final Long MUTEX_EXPIRE = 10000L;
    @Autowired
    public RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisLock redisLock;
    protected String cacheKeyNameSpace = "apple_cert";

    protected <T> T getValue(String key, CacheCallback<T> callback) {
        ValueOperations ops = this.redisTemplate.opsForValue();
        Object value = ops.get(key);
        if (!Objects.isNull(value))
            return (T) value;

        if (callback == null)
            return null;

        String mutexKey = "lock." + key;
        String locked = this.redisLock.tryLock(mutexKey, MUTEX_EXPIRE, true);
        if (locked != null) {
            try {
                value = ops.get(key);
                if (!Objects.isNull(value))
                    return (T) value;

                value = callback.call();
                ops.set(key, value);
                return (T) value;
            } finally {
                this.redisLock.tryUnLock(mutexKey, locked);
            }
        }
        return null;
    }

    protected boolean exists(String key){
        return this.redisTemplate.hasKey(key);
    }

    protected <T> T getValue(String key) {
        return (T) getValue(key, null);
    }

    protected <T> void setValue(String key, T t) {
        setValue(key, t, -1L);
    }

    protected <T> void setValue(String key, T t, long timeout) {
        ValueOperations ops = this.redisTemplate.opsForValue();
        if (timeout < 0L)
            ops.set(key, t);
        else
            ops.set(key, t, timeout, TimeUnit.MILLISECONDS);
    }

    protected <T> void setValue(String key, T t, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, t, timeout, timeUnit);
    }

    /**
     * 设置map的值
     *
     * @param key     redis Map的key
     * @param map     具体的map对象(对象id,对象)
     * @param timeOut 过期时间
     * @param <T>
     */
    protected <T> void setMapValue(String key, Map<String, T> map, Long timeOut) {
        if (null == map || map.size() == 0) return;
        this.redisTemplate.boundHashOps(key).putAll(map);
        this.redisTemplate.boundHashOps(key).expire(timeOut, TimeUnit.SECONDS);
    }
    /**
     * 设置map的值
     *
     * @param key     redis Map的key
     * @param map     具体的map对象(对象id,对象)
     * @param timeOut 过期时间
     * @param <T>
     */
    protected <T> void setMapValue(String key, Map<String, T> map, Long timeOut,TimeUnit timeUnit) {
        if (null == map || map.size() == 0) return;
        this.redisTemplate.boundHashOps(key).putAll(map);
        this.redisTemplate.boundHashOps(key).expire(timeOut, timeUnit);
    }

    /**
     * 根据map的idList获取value
     *
     * @param key    redis的key
     * @param idList map的idList
     * @return
     */
    protected <T> List<T> getMapValue(String key, List<String> idList) {
        List<T> result = new ArrayList<>();
        for (String id : idList) {
            T value = getMapValue(key, id);
            if (null != value) {
                result.add(value);
            }
        }
        if (result.isEmpty()){
            return null;
        }
        return result;
    }

    /**
     * 根据redis的key 和对象map的key 获取该对象
     *
     * @param key redisKey
     * @param id  map的key
     * @param <T> 对象
     * @return
     */
    protected <T> T getMapValue(String key, String id) {
        return (T) redisTemplate.<String, Object>opsForHash().get(key, id);
    }

    /**
     * 根据redis的key 获取整个map的value
     *
     * @param key
     * @return
     */
    protected List<Object> getMapAllValue(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * 根据redis的key 和map的keyList 删除map里面的value
     *
     * @param key
     * @param idList
     * @return
     */
    protected Long deleteMapValue(String key, List<String> idList) {
        return redisTemplate.opsForHash().delete(key, idList.toArray());
    }

    /**
     * 删除一个值
     *
     * @param key redis key
     * @param id  map key
     * @return
     */
    protected Long deleteMapValue(String key, String id) {
        return redisTemplate.opsForHash().delete(key, id);
    }

    /**
     * 删除整个map
     *
     * @param key
     * @return
     */
    protected void deleteMap(String key) {
         redisTemplate.opsForHash().getOperations().delete(key);
    }


    /**
     * 设置list
     *
     * @param key
     * @param list
     * @param timeOut
     */
    //protected <T> void setList(String key, List<T> list, Long timeOut) {
    //    redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
    //    redisTemplate.opsForList().leftPushAll(key, list);
    //}

    ///**
    // * 删除整个list
    // *
    // * @param key
    // * @param <T>
    // */
    //protected <T> void deleteList(String key) {
    //    redisTemplate.opsForList().getOperations().delete(key);
    //}

    ///**
    // * 删除list中一个值
    // *
    // * @param key list的key
    // * @param t   value
    // * @param <T>
    // */
    //protected <T> void deleteListOne(String key, T t) {
    //    redisTemplate.opsForList().remove(key, 0, t);
    //}

    ///**
    // * 获取整个list
    // */
    //protected <T> List<T> listAll(String key) {
    //    return (List<T>) redisTemplate.opsForList().range(key, 0, -1);
    //}

    /**
     * 模糊删除
     *
     * @param key
     */
    protected void batchDeleteValue(String key) {
        Set<String> keys = this.redisTemplate.keys(key + "*");
        this.redisTemplate.delete(keys);
    }

    /**
     * 模糊查询
     *
     * @param key
     * @param <T>
     * @return
     */
    protected <T> List<T> listKeys(String key) {
        Set<String> keys = this.redisTemplate.keys(key + "*");
        List<T> result = new ArrayList<>();
        Iterator<String> keysIterator = keys.iterator();
        while (keysIterator.hasNext()) {
            result.add(getValue(keysIterator.next()));
        }
        return result;
    }

    protected void updateExpireTime(String key, long timeout) {
        this.redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    protected void deleteValue(String key) {
        this.redisTemplate.delete(key);
    }

    protected <T> void setValues(final Map<String, T> map, final long timeout) {
        this.redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            RedisSerializer keySerializer = AbstractRedisCache.this.redisTemplate.getKeySerializer();
            RedisSerializer valueSerializer = AbstractRedisCache.this.redisTemplate.getValueSerializer();
            for (Map.Entry<String, T> entry : map.entrySet()) {
                connection.set(keySerializer.serialize(entry.getKey()), valueSerializer
                                .serialize(entry.getValue()),
                        Expiration.from(timeout, TimeUnit.MILLISECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT);
            }
            return null;
        });
    }

    protected <T> void setValues(final Map<String, T> map, final Map<String, Long> timeoutMap) {
        this.redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            RedisSerializer keySerializer = AbstractRedisCache.this.redisTemplate.getKeySerializer();
            RedisSerializer valueSerializer = AbstractRedisCache.this.redisTemplate.getValueSerializer();
            for (Map.Entry<String, T> entry : map.entrySet()) {
                String key = entry.getKey();
                connection.set(keySerializer.serialize(key), valueSerializer
                                .serialize(entry.getValue()),
                        Expiration.from(timeoutMap.get(key).longValue(), TimeUnit.MILLISECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT);
            }
            return null;
        });
    }

    protected <T> Set<T> getSetValue(String key) {
        SetOperations ops = this.redisTemplate.opsForSet();
        return ops.members(key);
    }

    protected <T> void unionSetValue(String key, Set<T> set) {
        if (CollectionUtils.isEmpty(set))
            return;
        SetOperations<String, Object> ops = this.redisTemplate.opsForSet();
        ops.add(key, set.toArray());
    }

    public abstract void initCache();

    /**
     * 刷新键
     *
     * @param key        关键
     * @param timeOut    时间 毫秒
     * @param expireTime 小于 【expireTime】刷新
     */
    public void refreshKey(String key,long timeOut,long expireTime) {
        Long expire = this.redisTemplate.getExpire(key,TimeUnit.MILLISECONDS);
        Objects.requireNonNull(expire, key + " key 不存在或已过期");
        if (expire.compareTo(expireTime) <= 0) {
            redisTemplate.expire(key, timeOut, TimeUnit.MILLISECONDS);
        }
    }

}
