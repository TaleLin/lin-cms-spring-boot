package io.github.talelin.latticy.dto.admin;

import lombok.Data;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
@Data
public class UpdateUserInfoDTO {

    private List<@Min(1) Integer> groupIds;
}
