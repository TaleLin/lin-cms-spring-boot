package com.lin.cms.merak.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponseVO<T> {

    private long total;

    private List<T> items;

    private long page;

    private long count;

    public static PageResponseVO genPageResult(long total, List items, long page, long count) {
        return new PageResponseVO(total, items, page, count);
    }
}
