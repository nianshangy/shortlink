package com.nian.shortlink.admin.common.biz.user;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSON;
import com.nian.shortlink.admin.common.convention.exception.ClientException;
import com.nian.shortlink.admin.common.convention.result.ResultUtils;
import com.nian.shortlink.admin.config.UserFlowRiskControlConfiguration;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static com.nian.shortlink.admin.common.convention.errorcode.BaseErrorCode.FLOW_LIMIT_ERROR;

/**
 * 用户操作后管项目流量风控过滤器
 */
@Slf4j
@RequiredArgsConstructor
public class UserFlowRiskControlFilter implements Filter {

    private final StringRedisTemplate stringRedisTemplate;
    private final UserFlowRiskControlConfiguration userFlowRiskControlConfiguration;

    private static final String USER_FLOW_RISK_CONTROL_LUA_SCRIPT_PATH = "lua/user_flow_risk_control.lua";


    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //通过 Redis increment 命令对一个数据进行递增，如果超过 x 次就会返回失败。这里有个细节就是我们的这个周期是 x 秒，需要对 Redis 的 Key 设置 x 秒有效期。
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(USER_FLOW_RISK_CONTROL_LUA_SCRIPT_PATH)));
        redisScript.setResultType(Long.class);
        //获取用户名，当登录与注册时获取不到用户名则设置成other
        String username = Optional.ofNullable(UserContext.getUsername()).orElse("other");
        Long result = null;
        try{
            result = stringRedisTemplate.execute(redisScript, ListUtil.toList(username), userFlowRiskControlConfiguration.getTimeWindow());
        }catch (Throwable ex){
            log.error("用户操作后管项目流量风控执行lua脚本出错",ex);
            returnJson((HttpServletResponse) servletResponse, JSON.toJSONString(ResultUtils.failure(new ClientException(FLOW_LIMIT_ERROR))));
        }
        if(result == null || result > userFlowRiskControlConfiguration.getMaxAccessCount()){
            returnJson((HttpServletResponse) servletResponse, JSON.toJSONString(ResultUtils.failure(new ClientException(FLOW_LIMIT_ERROR))));
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    /*返回客户端数据*/
    private void returnJson(HttpServletResponse response, String json) throws Exception{
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
