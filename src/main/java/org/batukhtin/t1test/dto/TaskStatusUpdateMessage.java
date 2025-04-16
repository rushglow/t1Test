package org.batukhtin.t1test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.batukhtin.t1test.model.enums.TaskStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusUpdateMessage {
    private Long taskId;
    private String title;
    private TaskStatus status;
    private String email;
}
