package io.github.talelin.latticy.mapper;

import io.github.talelin.latticy.model.UserIdentityDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author pedro@TaleLin
 * 用户身份标识mapper接口
 */
@Repository
public interface UserIdentityMapper extends BaseMapper<UserIdentityDO> {

}
