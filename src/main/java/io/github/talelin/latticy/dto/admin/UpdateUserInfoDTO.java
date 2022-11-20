package io.github.talelin.latticy.dto.admin;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * 用户信息更新数据传输对象
 */
@Data
public class UpdateUserInfoDTO {

    @NotEmpty(message = "{group.ids.not-empty}")
    private List<@Min(1) Integer> groupIds;

}
