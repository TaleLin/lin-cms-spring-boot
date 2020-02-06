package io.github.talelin.merak.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.merak.common.mybatis.Page;
import io.github.talelin.merak.model.LogDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author pedro
 * @since 2019-11-30
 */
@Repository
public interface LogMapper extends BaseMapper<LogDO> {

    IPage<LogDO> findLogsByUsernameAndRange(Page<LogDO> pager, String name, Date start, Date end);

    IPage<LogDO> searchLogsByUsernameAndKeywordAndRange(Page<LogDO> pager, String name, String keyword, Date start, Date end);

    IPage<String> getUserNames(Page<LogDO> pager);
}
