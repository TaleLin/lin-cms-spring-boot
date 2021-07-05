package ${package.Controller};


import io.github.talelin.autoconfigure.exception.NotFoundException;
<#if package.Controller?split(".")?last == "cms">
import io.github.talelin.core.annotation.LoginRequired;
</#if>
import io.github.talelin.latticy.common.mybatis.Page;
import io.github.talelin.latticy.common.util.PageUtil;
import io.github.talelin.latticy.dto.query.BasePageDTO;
import ${package.Service}.${table.serviceName};
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ${package.Entity}.${entity};
import io.github.talelin.latticy.vo.CreatedVO;
import io.github.talelin.latticy.vo.DeletedVO;
import io.github.talelin.latticy.vo.PageResponseVO;
import io.github.talelin.latticy.vo.UpdatedVO;

import javax.validation.constraints.Positive;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
<#if table.comment != "">
 * ${table.comment!}前端控制器
 * 注意：该代码生成器只实现了针对单表的新增、更新、删除、查询单个实体对象和分页查询多个实体对象的方法，如果业务较为复杂，不建议使用代码生成器！
 *
</#if>
* @author ${author}
* @since ${date}
*/
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("/${package.Controller?split(".")?last}<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-do", "")}<#else>${table.entityPath?replace("DO", "")}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Autowired
    private ${table.serviceName} ${table.serviceName?uncap_first};

    <#if package.Controller?split(".")?last == "cms">
    @LoginRequired
    </#if>
    @PostMapping("")
    public CreatedVO create(@RequestBody ${entity} dto) {
        ${entity} ${entity?uncap_first} = new ${entity}();
        BeanUtils.copyProperties(dto, ${entity?uncap_first});
        ${table.serviceName?uncap_first}.save(${entity?uncap_first});
        return new CreatedVO();
    }

    <#if package.Controller?split(".")?last == "cms">
    @LoginRequired
    </#if>
    @PutMapping("/{id}")
    public UpdatedVO update(
            @PathVariable @Positive(message = "{id.positive}") Integer id,
            @RequestBody ${entity} dto
    ) {
        get(id);
        dto.setId(id);
        ${table.serviceName?uncap_first}.updateById(dto);
        return new UpdatedVO();
    }

    <#if package.Controller?split(".")?last == "cms">
    @LoginRequired
    </#if>
    @DeleteMapping("/{id}")
    public DeletedVO delete(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        ${entity} ${entity?uncap_first} = get(id);
        ${table.serviceName?uncap_first}.removeById(${entity?uncap_first}.getId());
        return new DeletedVO();
    }

    @GetMapping("/{id}")
    <#if package.Controller?split(".")?last == "cms">
    @LoginRequired
    </#if>
    public ${entity} get(@PathVariable(value = "id") @Positive(message = "{id.positive}") Integer id) {
        ${entity} ${entity?uncap_first} = ${table.serviceName?uncap_first}.getById(id);
        if (${entity?uncap_first} == null) {
            throw new NotFoundException();
        }
        return ${entity?uncap_first};
    }

    @GetMapping("/page")
    <#if package.Controller?split(".")?last == "cms">
    @LoginRequired
    </#if>
    public PageResponseVO<${entity}> page(
            @Validated BasePageDTO dto
    ) {
        return PageUtil.build(${table.serviceName?uncap_first}.page(new Page<>(dto.getPage(), dto.getCount()), null));
    }

}
</#if>
