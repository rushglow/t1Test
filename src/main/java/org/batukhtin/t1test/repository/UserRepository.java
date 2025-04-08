package org.batukhtin.t1test.repository;

import org.batukhtin.t1test.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserByUsername(String username);

    Boolean existsByUsername(String username);
}
