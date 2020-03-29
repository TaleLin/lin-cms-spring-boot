package io.github.talelin.merak.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页数据统一 view object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponseVO<T> {

    private long total;

    private List<T> items;

    private long page;

    private long count;
}
