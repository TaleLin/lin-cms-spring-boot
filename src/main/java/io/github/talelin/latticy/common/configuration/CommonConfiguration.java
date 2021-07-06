package io.github.talelin.latticy.common.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.github.talelin.autoconfigure.bean.PermissionMetaCollector;
import io.github.talelin.latticy.common.interceptor.RequestLogInterceptor;
import io.github.talelin.latticy.module.log.MDCAccessServletFilter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pedro@TaleLin
 * @author colorful@TaleLin
 */
@Configuration(proxyBeanMethods = false)
public class CommonConfiguration {

    @Bean
    public RequestLogInterceptor requestLogInterceptor() {
        return new RequestLogInterceptor();
    }

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则
     * 需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     * 参考链接：https://mp.baomidou.com/guide/interceptor.html
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 参考链接：https://mp.baomidou.com/guide/interceptor.html
     */
    @Bean
    @SuppressWarnings("deprecation")
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }

    @Bean
    public ISqlInjector sqlInjector() {
        return new DefaultSqlInjector();
    }

    /**
     * 记录每个被 @PermissionMeta 记录的信息，在beans的后置调用
     *
     * @return PermissionMetaCollector
     */
    @Bean
    public PermissionMetaCollector postProcessBeans() {
        return new PermissionMetaCollector();
    }


    /**
     * 接口中，自动转换的有：驼峰转换为下划线，空值输出null
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customJackson() {
        return jacksonObjectMapperBuilder -> {
            // jacksonObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
            jacksonObjectMapperBuilder.failOnUnknownProperties(false);
            jacksonObjectMapperBuilder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        };
    }

    /**
     * 用于将 request 相关信息（如请求 url）放入 MDC 中供日志使用
     *
     * @return Logback 的 MDCInsertingServletFilter
     */
    @Bean
    public FilterRegistrationBean<MDCAccessServletFilter> mdcInsertingServletFilter() {
        FilterRegistrationBean<MDCAccessServletFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        MDCAccessServletFilter mdcAccessServletFilter = new MDCAccessServletFilter();
        filterRegistrationBean.setFilter(mdcAccessServletFilter);
        filterRegistrationBean.setName("mdc-access-servlet-filter");
        return filterRegistrationBean;
    }
}
