package io.github.talelin.merak.common.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.talelin.autoconfigure.beans.RouteMetaCollector;
import io.github.talelin.core.annotation.RouteMeta;
import io.github.talelin.merak.extensions.message.Message;
import io.github.talelin.merak.extensions.message.MessageInfoCollector;
import io.github.talelin.merak.model.PermissionDO;
import io.github.talelin.merak.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PermissionHandleListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RouteMetaCollector metaCollector;

    @Autowired
    private MessageInfoCollector messageInfoCollector;

    public static String messageModule = "消息推送";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        addNewPermissions();
        removeUnusedPermissions();
    }

    private void addNewPermissions() {
        metaCollector.getMetaMap().values().forEach(meta -> {
            if (meta.mount()) {
                String module = meta.module();
                String permission = meta.permission();
                createPermissionIfNotExist(permission, module);
            }
        });
        messageInfoCollector.getMessageMap().values().forEach(message -> {
            String event = message.event();
            createPermissionIfNotExist(event, messageModule);
        });
    }

    private void removeUnusedPermissions() {
        List<PermissionDO> allPermissions = permissionService.list();
        Map<String, RouteMeta> metaMap = metaCollector.getMetaMap();
        Map<String, Message> messageMap = messageInfoCollector.getMessageMap();
        for (PermissionDO permission : allPermissions) {
            boolean stayedInMeta = metaMap
                    .values()
                    .stream()
                    .anyMatch(meta -> meta.mount() && meta.module().equals(permission.getModule())
                            && meta.permission().equals(permission.getName()));
            boolean stayedInMessage = messageMap
                    .values()
                    .stream()
                    .anyMatch(message -> message.event().equals(permission.getName())
                            && permission.getModule().equals(messageModule));
            if (!stayedInMeta && !stayedInMessage) {
                permissionService.removeById(permission.getId());
            }
        }
    }

    private void createPermissionIfNotExist(String name, String module) {
        QueryWrapper<PermissionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PermissionDO::getName, name).eq(PermissionDO::getModule, module);
        PermissionDO one = permissionService.getOne(wrapper);
        if (one == null) {
            permissionService.save(PermissionDO.builder().module(module).name(name).build());
        }
    }
}
