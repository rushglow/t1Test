package org.batukhtin.t1test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.batukhtin.t1test.model.enums.TaskStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {

    private String title;

    private String description;

    private TaskStatus status;
}
