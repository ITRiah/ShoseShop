package com.shose.shoseshop.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageData<T> {
    private List<T> items;
    private int totalPage;
    private int pageNumber;
    private int pageSize;

    public static <T> PageData<T> from(Page<T> page) {
        return new PageData<>(
                page.getContent(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }
}

