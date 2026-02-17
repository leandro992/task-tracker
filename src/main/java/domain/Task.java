package domain;

import java.time.Instant;

public class Task {
    Long id;
    String description;
    Status status;
    Instant createdAt;
    Instant updatedAt;

public Task(Long id, String description, Status status, Instant updatedAt, Instant createdAt) {
    this.createdAt = createdAt;
    this.description = description;
    this.id = id;
    this.status = status;
    this.updatedAt = updatedAt;
}


    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
