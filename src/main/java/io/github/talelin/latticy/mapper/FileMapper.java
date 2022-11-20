package io.github.talelin.latticy.mapper;

import io.github.talelin.latticy.model.FileDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author pedro@TaleLin
 * 文件mapper接口
 */
@Repository
public interface FileMapper extends BaseMapper<FileDO> {

    /**
     * 根据文件md5查询文件对象
     * @param md5 文件md5
     * @return 文件数据对象
     */
    FileDO selectByMd5(@Param("md5") String md5);

    /**
     * 根据文件md5查询文件数量
     * @param md5 文件md5
     * @return 文件数据传输对象
     */
    int selectCountByMd5(@Param("md5") String md5);
}
