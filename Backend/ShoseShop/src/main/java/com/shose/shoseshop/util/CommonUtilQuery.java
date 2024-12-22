package com.shose.shoseshop.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommonUtilQuery {

    public static <T> void each(List<T> list, Each<T> each) {
        if (Boolean.FALSE.equals(CommonUtilQuery.isTrue(list))) {
            return;
        }

        for (int index = 0; index < list.size(); index++) {
            try {
                each.do_(index, list.get(index));
            } catch (Exception e) {
                System.err.println("Error during each operation: " + e.getMessage());
            }
        }
    }

    public static <T> List<T> findAll(List<T> list, final Find<T> findAll_) {
        final List<T> newList = new ArrayList<>();

        if (Boolean.FALSE.equals(CommonUtilQuery.isTrue(list))) {
            return newList;
        }

        each(list, (int index, T item) -> {
            if (Boolean.TRUE.equals(findAll_.do_(index, item))) {
                newList.add(item);
            }
        });

        return newList;
    }

    public static <T> T find(List<T> list, final Find<T> findAll_) {
        if (Boolean.FALSE.equals(CommonUtilQuery.isTrue(list))) {
            return null;
        }

        for (int index = 0; index < list.size(); index++) {
            if (Boolean.TRUE.equals(findAll_.do_(index, list.get(index)))) {
                return list.get(index);
            }
        }

        return null;
    }

    public static <T1, T2> List<T2> collect(List<T1> list, Collect<T1, T2> collect) {
        List<T2> newList = new ArrayList<>();

        if (Boolean.FALSE.equals(CommonUtilQuery.isTrue(list))) {
            return newList;
        }

        for (int index = 0; index < list.size(); index++) {
            try {
                newList.add(collect.do_(index, list.get(index)));
            } catch (Exception e) {
                System.err.println("Error during collect operation: " + e.getMessage());
            }
        }

        return newList;
    }

    public static Boolean isTrue(Object value) {
        boolean result = value != null;

        if (value instanceof String) {
            result = !((String) value).trim().isEmpty();
        }

        if (value instanceof Number) {
            result = !((Number) value).equals(0L);
        }

        if (value instanceof Boolean) {
            result = (Boolean) value;
        }

        if (value instanceof Collection) {
            result = !((Collection<?>) value).isEmpty();
        }

        if (value instanceof Object[]) {
            result = ((Object[]) value).length > 0;
        }

        return result;
    }

    public static <T> T nvl(T object, NVL<T>... nvls) {
        int index = 0;
        if (nvls != null) {
            while (object == null && index < nvls.length) {
                object = nvls[index].getDefaultValue();
                index++;
            }
        }
        return object;
    }

    public interface Each<T> {
        void do_(int index, T item);
    }

    public interface Collect<T1, T2> {
        T2 do_(int index, T1 item);
    }

    public interface Find<T> {
        Boolean do_(int index, T item);
    }

    public interface NVL<T> {
        T getDefaultValue();
    }

    public static Pageable sortAgain(Pageable pageable, String key) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
                        .and(Sort.by(key).ascending())
        );
    }

    public static <T> T getOne(List<T> list) {
        if (Boolean.TRUE.equals(CommonUtilQuery.isTrue(list))) {
            return list.get(0);
        }
        return null;
    }
}
