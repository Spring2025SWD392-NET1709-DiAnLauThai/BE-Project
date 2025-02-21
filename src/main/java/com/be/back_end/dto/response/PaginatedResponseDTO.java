package com.be.back_end.dto.response;

import lombok.Data;

import java.lang.reflect.Constructor;
import java.util.List;

@Data
public class PaginatedResponseDTO<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;



}
