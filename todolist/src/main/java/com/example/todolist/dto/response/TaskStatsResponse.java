package com.example.todolist.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskStatsResponse {
    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;
    
    public TaskStatsResponse() {}
    
    public TaskStatsResponse(long totalTasks, long completedTasks, long pendingTasks) {
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.pendingTasks = pendingTasks;
    }
    
    public long getTotalTasks() { return totalTasks; }
    public void setTotalTasks(long totalTasks) { this.totalTasks = totalTasks; }
    public long getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(long completedTasks) { this.completedTasks = completedTasks; }
    public long getPendingTasks() { return pendingTasks; }
    public void setPendingTasks(long pendingTasks) { this.pendingTasks = pendingTasks; }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private long totalTasks;
        private long completedTasks;
        private long pendingTasks;
        
        public Builder totalTasks(long totalTasks) { this.totalTasks = totalTasks; return this; }
        public Builder completedTasks(long completedTasks) { this.completedTasks = completedTasks; return this; }
        public Builder pendingTasks(long pendingTasks) { this.pendingTasks = pendingTasks; return this; }
        
        public TaskStatsResponse build() {
            return new TaskStatsResponse(totalTasks, completedTasks, pendingTasks);
        }
    }
}
