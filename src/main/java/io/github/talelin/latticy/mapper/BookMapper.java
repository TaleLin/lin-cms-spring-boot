package io.github.talelin.latticy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.talelin.latticy.model.BookDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author pedro@TaleLin
 * 图书mapper接口
 */
@Repository
public interface BookMapper extends BaseMapper<BookDO> {

    /**
     * 根据标题模糊查询图书列表
     * @param q 查询关键字
     * @return 图书数据对象列表
     */
    List<BookDO> selectByTitleLikeKeyword(@Param("q") String q);

    /**
     * 根据标题查询图书列表
     * @param title 图书标题
     * @return 图书数据对象列表
     */
    List<BookDO> selectByTitle(@Param("title") String title);
}
