package top.dongxiaohao.common.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author: Dongxiaohao
 * @Date: 2024/10/31 22:53
 */
@Data
@Configuration
@ConfigurationProperties("secure")
public class IgnoreUrlsConfig {
    private List<String> ignoreUrls;
}
