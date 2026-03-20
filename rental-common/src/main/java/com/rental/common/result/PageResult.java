package com.rental.common.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页结果
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageResult<T> extends Result<T> {

    private Long total;
    private Long current;
    private Long size;

    public PageResult() {
    }

    public PageResult(T data, Long total, Long current, Long size) {
        super(200, "success", data);
        this.total = total;
        this.current = current;
        this.size = size;
    }

    public static <T> PageResult<T> of(T data, Long total, Long current, Long size) {
        return new PageResult<>(data, total, current, size);
    }
}
