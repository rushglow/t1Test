package org.batukhtin.t1test.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;


public enum TaskStatus {
    @JsonProperty("NEW")
    NEW,
    @JsonProperty("IN_PROGRESS")
    IN_PROGRESS,
    @JsonProperty("DONE")
    DONE;
}