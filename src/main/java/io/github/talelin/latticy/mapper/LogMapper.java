package io.github.talelin.latticy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.latticy.common.mybatis.LinPage;
import io.github.talelin.latticy.model.LogDO;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author pedro@TaleLin
 * 日志mapper接口
 */
@Repository
public interface LogMapper extends BaseMapper<LogDO> {

    /**
     * 查询日志
     *
     * @param pager 分页对象
     * @param name  用户名
     * @param start 开始日期
     * @param end   结束日期
     * @return 日志分页对象
     */
    IPage<LogDO> findLogsByUsernameAndRange(LinPage<LogDO> pager, String name, Date start, Date end);

    /**
     * 查询日志
     *
     * @param pager    分页对象
     * @param name     用户名
     * @param keyword  查询关键字
     * @param start    开始日期
     * @param end      结束日期
     * @return 日志分页对象
     */
    IPage<LogDO> searchLogsByUsernameAndKeywordAndRange(LinPage<LogDO> pager, String name, String keyword, Date start, Date end);

    /**
     * 查询用户名分页列表
     *
     * @param pager Page
     * @return 用户名分页对象
     */
    IPage<String> getUserNames(LinPage<LogDO> pager);

}
