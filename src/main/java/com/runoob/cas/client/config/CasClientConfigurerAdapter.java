package com.runoob.cas.client.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

/**
 * 配置或自定义CAS过滤器接口适配器
 *
 * @author 赵秀非 E-mail:zhaoxiufei@gmail.com
 * @version 创建时间：2017/7/6 18:28
 * @see CasClientConfigurer
 * @since 1.0.0
 */
public class CasClientConfigurerAdapter implements CasClientConfigurer {
    @Override
    public void configureAuthenticationFilter(FilterRegistrationBean authenticationFilter) {
    }

    @Override
    public void configureValidationFilter(FilterRegistrationBean validationFilter) {
    }

    @Override
    public void configureHttpServletRequestWrapperFilter(FilterRegistrationBean httpServletRequestWrapperFilter) {
    }

    @Override
    public void configureAssertionThreadLocalFilter(FilterRegistrationBean assertionThreadLocalFilter) {
    }

    @Override
    public void configureSingleSignOutFilter(FilterRegistrationBean singleSignOutFilter) {
    }
}
