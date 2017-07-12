# spring-boot项目cas客户端
    
    默认使用CAS2协议协议,如需其他实现,请自行更改源码
---
##当前版本

    <version>1.0.RELEASE</version>


##项目使用

1. 下载本项目编译,打jar包
1. pom.xml 配置 

    ```bash
     <dependency>
        <groupId>com.runoob</groupId>
        <artifactId>spring-boot-starter-cas-client-support</artifactId>
        <version>${current-version}</version>
     </dependency>
    ```
1. application.properties or application.yml中配置

##主要配置项

- 必须项
     
     ```bash
     spring.cas.cas-server-url-prefix=http://www.cas-server.com:8443 cas认证服务器地址
     spring.cas.cas-server-login-url=${spring.cas.cas-server-url-prefix}/login 登录认证服务器地址
     spring.cas.server-name=http://www.client.com:8080 你的项目地址
     ```
- 可选项

    ```bash
    spring.cas.sign-out-filters (默认值:/*)
    spring.cas.auth-filters (默认值:/*)
    spring.cas.validate-filters (默认值:/*)
    spring.cas.request-wrapper-filters (默认值:/*)
    spring.cas.assertion-filters (默认值:/*)
    spring.cas.ignore-pattern 要忽略的资源,使用正则匹配
    spring.cas.use-session (默认值:true)
    spring.cas.redirect-after-validation (默认值:true)
    spring.cas.session-time-out-ignore-urls= (默认:/*,一般只需要:/,/logout即可,按照实际业务设置)
    ```
##高级配置及扩展

    ```bash
    class CasProtectedApplication extends CasClientConfigurerAdapter { 
        @Override
        void configureValidationFilter(FilterRegistrationBean validationFilter) {           
            validationFilter.getInitParameters().put("millisBetweenCleanUps", "120000");
        }        
        @Override
        void configureAuthenticationFilter(FilterRegistrationBean authenticationFilter) {
            authenticationFilter.getInitParameters().put("artifactParameterName", "casTicket");
            authenticationFilter.getInitParameters().put("serviceParameterName", "targetService");
        }   
    }
    ```
    
 