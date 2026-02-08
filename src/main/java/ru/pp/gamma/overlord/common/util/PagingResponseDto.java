package ru.pp.gamma.overlord.common.util;

import java.util.List;

public record PagingResponseDto<T>(
        List<T> elements,
        int totalElements // Всего элементов
) {
}
