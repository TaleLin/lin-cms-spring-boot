package io.github.talelin.latticy.controller.cms;

import io.github.talelin.latticy.model.LogDO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 日志 view object
 *
 * @author colorful@TaleLin
 */
@Data
@NoArgsConstructor
public class LogVO {

    private Integer id;

    private String message;

    private Integer userId;

    private String username;

    private Integer statusCode;

    private String method;

    private String path;

    private String permission;

    private Date time;

    public LogVO(LogDO logDO) {
        BeanUtils.copyProperties(logDO, this);
        this.time = logDO.getCreateTime();
    }

}
