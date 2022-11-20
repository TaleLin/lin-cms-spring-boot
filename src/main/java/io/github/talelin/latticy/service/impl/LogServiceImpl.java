package io.github.talelin.latticy.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.common.mybatis.Page;
import io.github.talelin.latticy.mapper.LogMapper;
import io.github.talelin.latticy.model.LogDO;
import io.github.talelin.latticy.service.LogService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * 日志服务实现类
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, LogDO> implements LogService {

    @Override
    public IPage<LogDO> getLogPage(Integer page, Integer count, String name, Date start, Date end) {
        Page<LogDO> pager = new Page<>(page, count);
        return this.baseMapper.findLogsByUsernameAndRange(pager, name, start, end);
    }

    @Override
    public IPage<LogDO> searchLogPage(Integer page, Integer count, String name, String keyword, Date start, Date end) {
        Page<LogDO> pager = new Page<>(page, count);
        return this.baseMapper.searchLogsByUsernameAndKeywordAndRange(pager, name, "%" + keyword + "%", start, end);
    }

    @Override
    public IPage<String> getUserNamePage(Integer page, Integer count) {
        Page<LogDO> pager = new Page<>(page, count);
        return this.baseMapper.getUserNames(pager);
    }

    @Override
    public boolean createLog(String message, String permission, Integer userId, String username, String method, String path, Integer status) {
        LogDO record = LogDO.builder()
                .message(message)
                .userId(userId)
                .username(username)
                .statusCode(status)
                .method(method)
                .path(path)
                .build();
        if (permission != null) {
            record.setPermission(permission);
        }
        return this.baseMapper.insert(record) > 0;
    }
}
