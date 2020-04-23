package io.github.talelin.merak.bo;

import cn.hutool.core.bean.BeanUtil;
import io.github.talelin.merak.model.GroupDO;
import io.github.talelin.merak.model.PermissionDO;
import lombok.*;

import java.util.List;

/**
 * @author pedro@TaleLin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupPermissionBO {
    private Long id;

    private String name;

    private String info;

    private List permissions;

    public GroupPermissionBO(GroupDO group, List<PermissionDO> permissions) {
        BeanUtil.copyProperties(group, this);
        this.permissions = permissions;
    }
}
