package com.bfs.quizlet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class Page<T> {
    private List<T> content;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    public Page(List<T> content,
                int currentPage,
                int pageSize,
                long totalElements) {
        this.content = content;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
    }

    public static <T> Page<T> getOnePageContent(List<T> allContent, int currentPage, int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize must be greater than 0");
        }
        if (currentPage <= 0) {
            throw new IllegalArgumentException("currentPage must be greater than 0 (1-based)");
        }
        int totalElements = allContent.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        if (currentPage > totalPages) {
            return new Page<>(Collections.emptyList(), currentPage, pageSize, totalElements);
        }

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, totalElements);

        return new Page<>(allContent.subList(start, end), currentPage, pageSize, totalElements);
    }
}
