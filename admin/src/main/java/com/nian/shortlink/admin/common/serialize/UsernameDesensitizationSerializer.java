package com.nian.shortlink.admin.common.serialize;

import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 用户真实姓名脱敏反序列化
 */
public class UsernameDesensitizationSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String username, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String usernameDesensitization = DesensitizedUtil.chineseName(username);
        jsonGenerator.writeString(usernameDesensitization);
    }
}
