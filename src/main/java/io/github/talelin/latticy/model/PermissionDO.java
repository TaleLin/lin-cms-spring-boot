package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
@Data
@Builder
@TableName("lin_permission")
@EqualsAndHashCode(callSuper = true)
public class PermissionDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = -2400022443732120128L;

    /**
     * 权限名称，例如：访问首页
     */
    private String name;

    /**
     * 权限所属模块，例如：人员管理
     */
    private String module;
}
