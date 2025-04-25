package org.batukhtin.t1test.repository;

import org.batukhtin.t1test.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    @Query("SELECT t " +
            "FROM TaskEntity t " +
            "WHERE t.id = :id AND t.user.id = :userId")
    Optional<TaskEntity> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT t " +
            "FROM TaskEntity t " +
            "WHERE t.user.id = :userId")
    List<TaskEntity> findAllByUserId(Long userId);

    void deleteByIdAndUserId(Long id, Long userId);
}
