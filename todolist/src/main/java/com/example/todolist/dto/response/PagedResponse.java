package com.example.todolist.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagedResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isLastPage;
    
    public PagedResponse() {}
    
    public PagedResponse(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages, boolean isLastPage) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isLastPage = isLastPage;
    }
    
    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }
    public int getPageNumber() { return pageNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public boolean isLastPage() { return isLastPage; }
    public void setLastPage(boolean lastPage) { isLastPage = lastPage; }
    
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }
    
    public static class Builder<T> {
        private List<T> content;
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean isLastPage;
        
        public Builder<T> content(List<T> content) { this.content = content; return this; }
        public Builder<T> pageNumber(int pageNumber) { this.pageNumber = pageNumber; return this; }
        public Builder<T> pageSize(int pageSize) { this.pageSize = pageSize; return this; }
        public Builder<T> totalElements(long totalElements) { this.totalElements = totalElements; return this; }
        public Builder<T> totalPages(int totalPages) { this.totalPages = totalPages; return this; }
        public Builder<T> isLastPage(boolean isLastPage) { this.isLastPage = isLastPage; return this; }
        
        public PagedResponse<T> build() {
            return new PagedResponse<>(content, pageNumber, pageSize, totalElements, totalPages, isLastPage);
        }
    }
}
