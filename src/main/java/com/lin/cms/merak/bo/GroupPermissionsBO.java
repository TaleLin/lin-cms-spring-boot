package com.lin.cms.merak.bo;

import cn.hutool.core.bean.BeanUtil;
import com.lin.cms.merak.model.GroupDO;
import com.lin.cms.merak.model.PermissionDO;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupPermissionsBO {
    private Long id;

    private String name;

    private String info;

    private List permissions;

    public GroupPermissionsBO(GroupDO group, List<PermissionDO> permissions) {
        BeanUtil.copyProperties(group, this);
        this.permissions = permissions;
    }
}
