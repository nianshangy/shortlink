package com.nian.shortlink.project.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Sentinel的初始化限流配置
 */
@Component
public class SentinelRuleConfig implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        List<FlowRule> ruleList = new ArrayList<>();
        FlowRule createLinkRule = new FlowRule();
        createLinkRule.setResource("create_short-link");
        createLinkRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //每秒QPS为1
        createLinkRule.setCount(1);
        ruleList.add(createLinkRule);
        FlowRuleManager.loadRules(ruleList);
    }
}
