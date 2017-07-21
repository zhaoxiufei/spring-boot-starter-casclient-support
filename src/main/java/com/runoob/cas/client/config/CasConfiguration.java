package com.runoob.cas.client.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.runoob.cas.client.adapter.CasConfigurer;
import com.runoob.cas.client.autoconfig.CasProperties;
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
@EnableConfigurationProperties(CasProperties.class)
public class CasConfiguration {

    @Autowired
    private CasProperties casProperties;

    private CasConfigurer casConfigurer;
    private static boolean casEnabled = true;

    public CasConfiguration() {
    }

    @Bean
    public CasProperties getSpringCasAutoconfig() {
        return new CasProperties();
    }


    @Autowired(required = false)
    private void setConfigurers(Collection<CasConfigurer> configurers) {
        if (CollectionUtils.isEmpty(configurers)) {
            return;
        }
        if (configurers.size() > 1) {
            throw new IllegalStateException(configurers.size() + " [CasClientConfigurer] 实现类只能有一个");
        }
        this.casConfigurer = configurers.iterator().next();
    }


    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener = new ServletListenerRegistrationBean<>();
        listener.setEnabled(casEnabled);
        listener.setListener(new SingleSignOutHttpSessionListener());
        listener.setOrder(1);
        return listener;
    }

    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        FilterRegistrationBean singleSignOutFilter = new FilterRegistrationBean();
        Map<String, String> map = Maps.newHashMap();
        map.put("casServerUrlPrefix", casProperties.getCasServerUrlPrefix());
        initFilter(singleSignOutFilter, new SingleSignOutFilter(), 2, map, casProperties.getSignOutFilters());
        if (casConfigurer != null) {
            casConfigurer.configureSingleSignOutFilter(singleSignOutFilter);
        }
        return singleSignOutFilter;
    }

    @Bean
    public FilterRegistrationBean sessionTimeOutFilter() {
        FilterRegistrationBean sessionTimeOutFilter = new FilterRegistrationBean();
        SessionTimeOutFilter filter = new SessionTimeOutFilter();
        filter.setCasProperties(casProperties);
        initFilter(sessionTimeOutFilter, filter, 3, Maps.newHashMap(), Lists.newArrayList());
        if (casConfigurer != null) {
            casConfigurer.configureSessionTimeOutFilter(sessionTimeOutFilter);
        }
        return sessionTimeOutFilter;
    }


    @Bean
    public FilterRegistrationBean authenticationFilter() {
        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        Map<String, String> map = Maps.newHashMap();
        map.put("casServerLoginUrl", casProperties.getCasServerLoginUrl());
        map.put("serverName", casProperties.getServerName());
        map.put("useSession", casProperties.isUseSession() + "");
        map.put("redirectAfterValidation", casProperties.isRedirectAfterValidation() + "");
        map.put("ignorePattern", casProperties.getIgnorePattern());
        initFilter(authenticationFilter, new AuthenticationFilter(), 4, map, casProperties.getAuthFilters());
        if (casConfigurer != null) {
            casConfigurer.configureAuthenticationFilter(authenticationFilter);
        }
        return authenticationFilter;
    }

    @Bean
    public FilterRegistrationBean cas20ProxyReceivingTicketValidationFilter() {
        FilterRegistrationBean validationFilter = new FilterRegistrationBean();
        Map<String, String> map = Maps.newHashMap();
        map.put("casServerUrlPrefix", casProperties.getCasServerUrlPrefix());
        map.put("serverName", casProperties.getServerName());
        initFilter(validationFilter, new Cas20ProxyReceivingTicketValidationFilter(), 5, map, casProperties.getAuthFilters());
        if (casConfigurer != null) {
            casConfigurer.configureValidationFilter(validationFilter);
        }
        return validationFilter;
    }

    @Bean
    public FilterRegistrationBean httpServletRequestWrapperFilter() {
        FilterRegistrationBean httpServletRequestWrapperFilter = new FilterRegistrationBean();
        initFilter(httpServletRequestWrapperFilter, new HttpServletRequestWrapperFilter(), 6, Maps.newHashMap(), Lists.newArrayList());
        if (casConfigurer != null) {
            casConfigurer.configureHttpServletRequestWrapperFilter(httpServletRequestWrapperFilter);
        }
        return httpServletRequestWrapperFilter;
    }


    @Bean
    public FilterRegistrationBean assertionThreadLocalFilter() {
        FilterRegistrationBean assertionThreadLocalFilter = new FilterRegistrationBean();
        initFilter(assertionThreadLocalFilter, new AssertionThreadLocalFilter(), 7, Maps.newHashMap(), casProperties.getAssertionFilters());
        if (casConfigurer != null) {
            casConfigurer.configureAssertionThreadLocalFilter(assertionThreadLocalFilter);
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
