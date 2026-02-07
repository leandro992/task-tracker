package org.maladireta.lib.domain;

import java.time.Instant;

public class Task {
    long id;
    String description;
    Status status;
    Instant createdAt;
    Instant updatedAt;
}
