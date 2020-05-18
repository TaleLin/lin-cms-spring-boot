package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pedro@TaleLin
 */
@Data
@Builder
@TableName("lin_group")
@NoArgsConstructor
@AllArgsConstructor
public class GroupDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分组名称，例如：搬砖者
     */
    private String name;

    /**
     * 分组信息：例如：搬砖的人
     */
    private String info;

    /**
     * 分组级别（root、guest、user，其中 root、guest 分组只能存在一个）
     */
    @TableField(value = "`level`")
    private String level;


    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date updateTime;

    @JsonIgnore
    @TableLogic
    private Date deleteTime;
}
