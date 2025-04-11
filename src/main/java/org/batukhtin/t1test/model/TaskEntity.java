package org.batukhtin.t1test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.batukhtin.t1test.model.enums.TaskStatus;

@Entity
@Table(name = "tasks")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 4096, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @ManyToOne
    @JoinTable(name = "user_tasks", joinColumns = {@JoinColumn(name = "task_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    @JsonIgnore
    private UserEntity user;
}
