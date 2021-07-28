package io.github.talelin.latticy.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 24273
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModulePermissionBO {

    private String module;

    private String permission;

}