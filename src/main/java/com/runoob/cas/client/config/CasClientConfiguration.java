package com.runoob.cas.client.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.runoob.cas.client.adapter.CasClientConfigurer;
import com.runoob.cas.client.autoconfig.CasClientAutoConfigurationProperties;
import com.runoob.cas.client.filter.SessionTimeOutFilter;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.servlet.Filter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 配置或自定义CAS过滤器接口适配器
 *
 * @author 赵秀非 E-mail:zhaoxiufei@gmail.com
 * @version 创建时间：2017/7/6 16:28
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(CasClientAutoConfigurationProperties.class)
public class CasClientConfiguration {

    @Autowired
    private CasClientAutoConfigurationProperties casClientAutoConfigurationProperties;

    private CasClientConfigurer casClientConfigurer;
    private static boolean casEnabled = true;

    public CasClientConfiguration() {
    }

    @Bean
    public CasClientAutoConfigurationProperties getSpringCasAutoconfig() {
        return new CasClientAutoConfigurationProperties();
    }


    @Autowired(required = false)
    private void setConfigurers(Collection<CasClientConfigurer> configurers) {
        if (CollectionUtils.isEmpty(configurers)) {
            return;
        }
        if (configurers.size() > 1) {
            throw new IllegalStateException(configurers.size() + " [CasClientConfigurer] 实现类只能有一个");
        }
        this.casClientConfigurer = configurers.iterator().next();
    }
    @Bean
    public FilterRegistrationBean sessionTimeOutFilter() {
        FilterRegistrationBean sessionTimeOutFilter = new FilterRegistrationBean();
        SessionTimeOutFilter filter = new SessionTimeOutFilter();
        filter.setCasClientAutoConfigurationProperties(casClientAutoConfigurationProperties);
        initFilter(sessionTimeOutFilter, filter, 1, Maps.newHashMap(), Lists.newArrayList());
        if (casClientConfigurer != null) {
            casClientConfigurer.configureSessionTimeOutFilter(sessionTimeOutFilter);
        }
        return sessionTimeOutFilter;
    }
    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener = new ServletListenerRegistrationBean<>();
        listener.setEnabled(casEnabled);
        listener.setListener(new SingleSignOutHttpSessionListener());
        listener.setOrder(2);
        return listener;
    }

    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        FilterRegistrationBean singleSignOutFilter = new FilterRegistrationBean();
        Map<String, String> map = Maps.newHashMap();
        map.put("casServerUrlPrefix", casClientAutoConfigurationProperties.getCasServerUrlPrefix());
        initFilter(singleSignOutFilter, new SingleSignOutFilter(), 3, map, casClientAutoConfigurationProperties.getSignOutFilters());
        if (casClientConfigurer != null) {
            casClientConfigurer.configureSingleSignOutFilter(singleSignOutFilter);
        }
        return singleSignOutFilter;
    }

    @Bean
    public FilterRegistrationBean authenticationFilter() {
        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        Map<String, String> map = Maps.newHashMap();
        map.put("casServerLoginUrl", casClientAutoConfigurationProperties.getCasServerLoginUrl());
        map.put("serverName", casClientAutoConfigurationProperties.getServerName());
        map.put("useSession", casClientAutoConfigurationProperties.isUseSession() + "");
        map.put("redirectAfterValidation", casClientAutoConfigurationProperties.isRedirectAfterValidation() + "");
        map.put("ignorePattern", casClientAutoConfigurationProperties.getIgnorePattern());
        initFilter(authenticationFilter, new AuthenticationFilter(), 4, map, casClientAutoConfigurationProperties.getAuthFilters());
        if (casClientConfigurer != null) {
            casClientConfigurer.configureAuthenticationFilter(authenticationFilter);
        }
        return authenticationFilter;
    }

    @Bean
    public FilterRegistrationBean cas20ProxyReceivingTicketValidationFilter() {
        FilterRegistrationBean validationFilter = new FilterRegistrationBean();
        Map<String, String> map = Maps.newHashMap();
        map.put("casServerUrlPrefix", casClientAutoConfigurationProperties.getCasServerUrlPrefix());
        map.put("serverName", casClientAutoConfigurationProperties.getServerName());
        initFilter(validationFilter, new Cas20ProxyReceivingTicketValidationFilter(), 5, map, casClientAutoConfigurationProperties.getAuthFilters());
        if (casClientConfigurer != null) {
            casClientConfigurer.configureValidationFilter(validationFilter);
        }
        return validationFilter;
    }

    @Bean
    public FilterRegistrationBean httpServletRequestWrapperFilter() {
        FilterRegistrationBean httpServletRequestWrapperFilter = new FilterRegistrationBean();
        initFilter(httpServletRequestWrapperFilter, new HttpServletRequestWrapperFilter(), 6, Maps.newHashMap(), Lists.newArrayList());
        if (casClientConfigurer != null) {
            casClientConfigurer.configureHttpServletRequestWrapperFilter(httpServletRequestWrapperFilter);
        }
        return httpServletRequestWrapperFilter;
    }


    @Bean
    public FilterRegistrationBean assertionThreadLocalFilter() {
        FilterRegistrationBean assertionThreadLocalFilter = new FilterRegistrationBean();
        initFilter(assertionThreadLocalFilter, new AssertionThreadLocalFilter(), 7, Maps.newHashMap(), casClientAutoConfigurationProperties.getAssertionFilters());
        if (casClientConfigurer != null) {
            casClientConfigurer.configureAssertionThreadLocalFilter(assertionThreadLocalFilter);
        }
        return assertionThreadLocalFilter;
    }

    private void initFilter(final FilterRegistrationBean filterRegistrationBean,
                            final Filter filter,
                            int filterOrder,
                            final Map<String, String> initParams,
                            List<String> urlPatterns) {

        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setEnabled(casEnabled);
        filterRegistrationBean.setOrder(filterOrder);
        if (initParams.size() > 0) {
            filterRegistrationBean.setInitParameters(initParams);
        }
        if (urlPatterns.size() > 0) {
            filterRegistrationBean.setUrlPatterns(urlPatterns);
        } else {
            filterRegistrationBean.addUrlPatterns("/*");
        }
    }

}
