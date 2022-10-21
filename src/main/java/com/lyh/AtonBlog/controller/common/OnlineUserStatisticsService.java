package com.lyh.AtonBlog.controller.common;

import io.swagger.annotations.Api;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 *
 *
 * 在线用户统计
 *
 * @author Administrator
 *
 */

@Component
public class OnlineUserStatisticsService {

        private static final String ONLINE_USERS = "onlie_users";
        
        @Resource
        private StringRedisTemplate stringRedisTemplate;
    
    
    /**
     * 重置时间 也就是score
     */
        public void resetScore(String IP){
            online(IP);
        }
        
        /**
         * 添加用户在线信息
         * @param userId 因为我没有做多用户 所以用 sessionId代替
         * @return
         */
        public Boolean online(String userId) {
            return this.stringRedisTemplate.opsForZSet().add(ONLINE_USERS, userId, Instant.now().toEpochMilli());
        }
        
        /**
         * 获取一定时间内，在线的用户数量
         * @param duration
         * @return
         */
        public Long count(Duration duration) {
            LocalDateTime now = LocalDateTime.now();
            return this.stringRedisTemplate.opsForZSet().count(ONLINE_USERS,
                    now.minus(duration).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        
        /**
         * 获取所有在线过的用户数量，不论时间
         * @return
         */
        public Long count() {
            return this.stringRedisTemplate.opsForZSet().zCard(ONLINE_USERS);
        }
        
        /**
         * 清除超过一定时间没在线的用户数据
         * @param duration
         * @return
         */
        public Long clear(Duration duration) {
            return this.stringRedisTemplate.opsForZSet().removeRangeByScore(ONLINE_USERS, 0,
                    LocalDateTime.now().minus(duration).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
}
