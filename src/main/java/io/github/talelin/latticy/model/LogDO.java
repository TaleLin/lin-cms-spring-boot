package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("lin_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String message;

    private Long userId;

    private String username;

    private Integer statusCode;

    private String method;

    private String path;

    private String permission;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date updateTime;

    @TableLogic
    @JsonIgnore
    private Date deleteTime;

}
