package com.runoob.cas.client.autoconfig;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 配置或自定义CAS过滤器接口适配器
 *
 * @author 赵秀非 E-mail:zhaoxiufei@gmail.com
 * @version 创建时间：2017/7/6 16:28
 * @since 1.0.0
 */
@Data
@Validated
@ConfigurationProperties(prefix = "spring.cas")
public class CasClientAutoConfigurationProperties {

    /**
     * cas server 前缀
     * 例如:http://www.exmple.com:8443/
     */

    @NotNull
    private String casServerUrlPrefix;
    /**
     * cas server 登录地址
     * http://www.exmple.com:8443/cas/login
     */
    @NotNull
    private String casServerLoginUrl;
    /**
     * 客户端server名称
     * 例如:http://www.client.com:8080
     */
    @NotNull
    private String serverName;

    /**
     * 忽略的请求正则匹配
     * 例如:(/whiteList|/notFilter|.js|.css)
     */
    private String ignorePattern;
    private List<String> validateFilters = Lists.newArrayList();
    private List<String> signOutFilters = Lists.newArrayList();
    private List<String> authFilters = Lists.newArrayList();
    private List<String> assertionFilters = Lists.newArrayList();
    private List<String> requestWrapperFilters = Lists.newArrayList();
    @NotNull
    private List<String> sessionTimeOutIgnoreUrls;
    private boolean useSession = true;
    private boolean redirectAfterValidation = true;

}
