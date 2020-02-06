package io.github.talelin.merak.common.configure;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.github.talelin.autoconfigure.beans.RouteMetaCollector;
import io.github.talelin.merak.extensions.file.FileProperties;
import io.github.talelin.merak.common.interceptor.RequestLogInterceptor;
import io.github.talelin.merak.model.PermissionDO;
import io.github.talelin.merak.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(FileProperties.class)
public class CommonConfig {

    @Autowired
    private PermissionService permissionService;

    @Bean
    public RequestLogInterceptor requestLogInterceptor() {
        return new RequestLogInterceptor();
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public ISqlInjector sqlInjector() {
        return new DefaultSqlInjector();
    }


    /**
     * 记录每个被 @RouteMeta 记录的信息，在beans的后置调用
     *
     * @return RouteMetaCollector
     */
    @Bean
    public RouteMetaCollector postProcessBeans() {
        return new RouteMetaCollector(meta -> {
            if (meta.mount()) {
                String module = meta.module();
                String permission = meta.permission();
                QueryWrapper<PermissionDO> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(PermissionDO::getName, permission).eq(PermissionDO::getModule, module);
                PermissionDO one = permissionService.getOne(wrapper);
                if (one == null) {
                    permissionService.save(PermissionDO.builder().module(module).name(permission).build());
                }
            }
        });
    }


    /**
     * 接口中，自动转换的有：驼峰转换为下划线，空值输出null
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customJackson() {
        return jacksonObjectMapperBuilder -> {
            // jacksonObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
            jacksonObjectMapperBuilder.failOnUnknownProperties(false);
            jacksonObjectMapperBuilder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        };
    }
}
