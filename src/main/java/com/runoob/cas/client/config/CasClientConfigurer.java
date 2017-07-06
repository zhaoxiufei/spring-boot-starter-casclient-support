package com.runoob.cas.client.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

/**
 * 配置或自定义CAS过滤器接口
 *
 * @author 赵秀非 E-mail:zhaoxiufei@gmail.com
 * @version 创建时间：2017/7/6 18:28
 * @since 1.0.0
 */
public interface CasClientConfigurer {

    /**
     * 配置或自定义CAS认证过滤器
     *
     * @param authenticationFilter
     */
    void configureAuthenticationFilter(FilterRegistrationBean authenticationFilter);

    /**
     * 配置或自定义CAS验证过滤器。
     *
     * @param validationFilter
     */
    void configureValidationFilter(FilterRegistrationBean validationFilter);

    /**
     * 配置或自定义CAS http servlet包装过滤器。
     *
     * @param httpServletRequestWrapperFilter
     */
    void configureHttpServletRequestWrapperFilter(FilterRegistrationBean httpServletRequestWrapperFilter);

    /**
     * 配置或自定义CAS断言线程本地过滤器。
     *
     * @param assertionThreadLocalFilter
     */
    void configureAssertionThreadLocalFilter(FilterRegistrationBean assertionThreadLocalFilter);

    /**
     * 配置或自定义CAS单点退出过滤器。
     *
     * @param singleSignOutFilter
     */
    void configureSingleSignOutFilter(FilterRegistrationBean singleSignOutFilter);
}
