package com.shose.shoseshop.util;

import com.shose.shoseshop.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class ObjectUtil {
    private ObjectUtil() {
    }

    public static <T> T wrapNullPointer(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static <T> T wrapNullPointer(Supplier<T> supplier, T defaultValue) {
        try {
            return supplier.get();
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    public static String toBlankWithNull(String s) {
        if (StringUtils.isNotBlank(s)) {
            return s;
        }
        return StringUtils.EMPTY;
    }

    public static String toFullName(String firstName, String lastName) {
        return String.format(Constants.FULL_NAME_FORMAT, toBlankWithNull(lastName), toBlankWithNull(firstName));
    }

    public static String join(String... strings) {
        if (strings == null) {
            return StringUtils.EMPTY;
        }
        return Arrays.stream(strings).filter(Objects::nonNull).collect(Collectors.joining(Constants.COMMA_CHARACTER));
    }
}
