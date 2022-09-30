package com.zhongqijia.pay.common.util;

import com.zhongqijia.pay.common.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @author 陌上丶天琊
 * 描述： redis分布式锁工具类
 */
@Component
@Slf4j
public class RedisLock {
    @Resource
    private RedisTemplate redisTemplate;

    private static final String UNLOCK_LUA;

    // 默认超时时间 60s
    private static long EXPIRE_TIME = 60;

    // baokim还款回调接口限制 90s
    private static long COLLECTION_EXPIRE_TIME = 90;

    /**
     * 释放锁脚本，原子操作
     */
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    /**
     * 获取分布式锁，原子操作
     *
     * @param lockKey
     * @param requestId 唯一ID, 可以使用UUID.randomUUID().toString();
     * @param expire
     * @param timeUnit
     * @return
     */
    private boolean tryLock(String lockKey, String requestId, long expire, TimeUnit timeUnit) {
        try {
            RedisCallback<Boolean> callback = connection -> connection.set(
                    lockKey.getBytes(Charset.forName("UTF-8")),
                    requestId.getBytes(Charset.forName("UTF-8")),
                    Expiration.seconds(timeUnit.toSeconds(expire)),
                    RedisStringCommands.SetOption.SET_IF_ABSENT
            );
            return (Boolean) redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("redis lock error.", e);
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param lockKey
     * @param requestId 唯一ID
     * @return
     */
    private boolean releaseLock(String lockKey, String requestId) {
        RedisCallback<Boolean> callback = connection -> connection.eval(
                UNLOCK_LUA.getBytes(),
                ReturnType.BOOLEAN,
                1,
                lockKey.getBytes(Charset.forName("UTF-8")),
                requestId.getBytes(Charset.forName("UTF-8"))
        );
        return (Boolean) redisTemplate.execute(callback);
    }

    /**
     * 获取Redis锁的value值
     *
     * @param lockKey
     * @return
     */
    public String get(String lockKey) {
        try {
            RedisCallback<String> callback =
                    connection -> new String(connection.get(lockKey.getBytes()), Charset.forName("UTF-8"));
            return (String) redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("get redis occurred an exception", e);
        }
        return null;
    }

    /**
     * 审核结果处理
     * @param userGid
     * @return
     */
    public boolean tryUpdateAuditLock(String userGid) {
        String lockKey = RedisConstant.UPDATE_AUDIT_LOCK_KEY + userGid;
        return tryLock(lockKey, userGid, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 释放审核处理结果锁
     * @param userGid
     * @return
     */
    public boolean releaseAuditLock(String userGid){
        String lockKey = RedisConstant.UPDATE_AUDIT_LOCK_KEY + userGid;
        return releaseLock(lockKey,userGid);
    }

    /**
     * 审核结果处理
     * @param taskId
     * @return
     */
    public boolean tryRobotInfoAuditNotifyLock(String taskId) {
        String lockKey = RedisConstant.ROBOT_INFO_AUDIT_NOTIFY_LOCK_KEY + taskId;
        return tryLock(lockKey, taskId, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 释放审核处理结果锁
     * @param taskId
     * @return
     */
    public boolean releaseRobotInfoAuditNotifyLock(String taskId){
        String lockKey = RedisConstant.ROBOT_INFO_AUDIT_NOTIFY_LOCK_KEY + taskId;
        return releaseLock(lockKey,taskId);
    }
}
