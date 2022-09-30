package com.zhongqijia.pay.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 陌上丶天琊 on 2020/4/3 15:12
 * 描述： redis操作类
 */
@Slf4j
@Component
public class RedisUtil {

    private RedisTemplate redisTemplate;
    private ValueOperations string;
    private ListOperations list;
    private HashOperations hash;
    private SetOperations set;
    private ZSetOperations zSet;

    @Autowired
    private void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        string = this.redisTemplate.opsForValue();
        list = this.redisTemplate.opsForList();
        hash = this.redisTemplate.opsForHash();
        set = this.redisTemplate.opsForSet();
        zSet = this.redisTemplate.opsForZSet();
    }

    // region -- 读操作
    // 字符串
    public Object get(String key) {
        Object obj = null;
        try {
            obj = string.get(key);
        } catch (Exception e) {
            log.error("get: {}", e);
        }
        return obj;
    }

    // Hash值
    public Object hashGet(String key, Object hashKey) {
        Object obj = null;
        try {
            obj = hash.get(key, hashKey);
        } catch (Exception e) {
            log.error("hashGet: {}", e);
        }
        return obj;
    }

    public List multiGet(String key, Collection fields) {
        List obj = null;
        try {
            obj = hash.multiGet(key, fields);
        } catch (Exception e) {
            log.error("multiGet: {}", e);
        }
        return obj;
    }

    // 列表
    public Object leftPop(String k) {
        Object obj = null;
        try {
            obj = list.leftPop(k);
        } catch (Exception e) {
            log.error("leftPop: {}", e);
        }
        return obj;
    }

    // 列表
    public List<Object> listRange(String k, long l, long l1) {
        List<Object> obj = null;
        try {
            obj = list.range(k, l, l1);
        } catch (Exception e) {
            log.error("listRange: {}", e);
        }
        return obj;
    }

    // 集合
    public Set<Object> setMembers(String key) {
        Set<Object> obj = null;
        try {
            obj = set.members(key);
        } catch (Exception e) {
            log.error("setMembers: {}", e);
        }
        return obj;
    }

    // 有序集合
    public Set<Object> setRangeByScore(String key, double source, double source1) {
        Set<Object> obj = null;
        try {
            obj = zSet.rangeByScore(key, source, source1);
        } catch (Exception e) {
            log.error("setRangeByScore: {}", e);
        }
        return obj;
    }
    // endregion

    // region -- 写操作
    // 字符串
    public boolean set(String key, Object value) {
        boolean result = false;
        try {
            string.set(key, value);
            result = true;
        } catch (Exception e) {
            log.error("set: {}", e);
        }
        return result;
    }

    // 字符串
    @SuppressWarnings("unchecked")
    public boolean set(String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            string.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error("set: {}", e);
        }
        return result;
    }
    
    //指定失效时间类型
    public boolean set(String key, Object value, int expireTime,TimeUnit timeUnit) {
        boolean result = false;
        try {
            string.set(key, value);
            redisTemplate.expire(key, expireTime, timeUnit);
            result = true;
        } catch (Exception e) {
            log.error("set: {}", e);
        }
        return result;
    }
    
    // Hash
    public void hashPut(String key, Object hashKey, Object value) {
        try {
            hash.put(key, hashKey, value);
        } catch (Exception e) {
            log.error("hashPut: {}", e);
        }
    }

    // 列表
    public void listLeftPush(String k, Object v) {
        try {
            list.leftPush(k, v);
        } catch (Exception e) {
            log.error("listLeftPush: {}", e);
        }
    }

    // 集合
    public void setAdd(String key, Object value) {
        try {
            set.add(key, value);
        } catch (Exception e) {
            log.error("setAdd: {}", e);
        }
    }

    // 有序集合
    public void zSetAdd(String key, Object value, double source) {
        try {
            zSet.add(key, value, source);
        } catch (Exception e) {
            log.error("zSetAdd: {}", e);
        }
    }
    // endregion

    // region -- 删操作
    // 删 单个key
    @SuppressWarnings("unchecked")
    public void delete(String key) {
        Boolean flag = hasKey(key);
        if (flag != null && flag) {
            try {
                redisTemplate.delete(key);
            } catch (Exception e) {
                log.error("delete: {}", e);
            }
        }
    }

    // 删 多个key
    public void delete(String... keys) {
        for (String key : keys) {
            delete(key);
        }
    }

    // 删 正则匹配
    @SuppressWarnings("unchecked")
    public Long deleteByPattern(String pattern) {
        Long result = 0L;
        try {
            Set<Serializable> keys = redisTemplate.keys(pattern);
            if (keys != null && keys.size() > 0) {
                result = redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            log.error("deleteByPattern: {}", e);
        }
        return result;
    }

    // 删 Hash键
    public void hashDelete(String masterKey, Object... hashKey) {
        try {
            hash.delete(masterKey, hashKey);
        } catch (Exception e) {
            log.error("hashDelete: {}", e);
        }
    }
    // endregion

    // region -- 其他操作

    /**
     * 判断key是否存在
     *
     * @param key
     * @return redis报错返回null
     */
    @SuppressWarnings("unchecked")
    public Boolean hasKey(String key) {
        Boolean flag = null;
        try {
            flag = redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("hasKey: {}", e);
        }
        return flag;
    }

    // 读 keys
    @SuppressWarnings("unchecked")
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 获取Hash键集合
     *
     * @param key hash键
     * @return redis异常返回null，如果key不存在返回空集合
     */
    public Set<Object> hashKeys(String key) {
        Set<Object> obj = null;
        try {
            obj = hash.keys(key);
        } catch (Exception e) {
            log.error("hashKeys: {}", e);
        }
        return obj;
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout
     * @return redis异常返回null
     */
    @SuppressWarnings("unchecked")
    public Boolean expire(String key, long timeout) {
        Boolean flag = null;
        try {
            flag = redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("expire: {}", e);
        }
        return flag;
    }

    /**
     * 获取剩余过期时间
     *
     * @param key
     * @return redis异常或没有过期时间返回-1
     */
    public long getExpire(String key) {
        long flag = -1;
        try {
            flag = redisTemplate.getExpire(key).longValue();
        } catch (Exception e) {
            log.error("getExpire: {}", e);
        }
        return flag;
    }

    /**
     * 值增加
     * @param redisKey
     * @param value
     * @return
     */
    public Long increment(String redisKey, int value) {
        return redisTemplate.opsForValue().increment(redisKey, value);
    }

    public <T> T execute(RedisCallback<T> callback) {
        return (T) redisTemplate.execute(callback);
    }
    // endregion
}
