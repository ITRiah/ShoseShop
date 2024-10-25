package com.shose.shoseshop.repository;
import com.shose.shoseshop.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @EntityGraph(attributePaths = "role")
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);


    User findByUsernameOrEmail(String username, String email);

    @Query("SELECT u FROM User u WHERE (u.username = :username OR u.email = :email) AND u.id <> :id")
    List<User> findByUsernameOrEmailAndNotId(@Param("username") String username, @Param("email") String email, @Param("id") Long id);

    @Query("SELECT u.id FROM User u WHERE u.id IN :userIds")
    Set<Long> findExistingUserIds(@Param("userIds") Set<Long> userIds);

    @Query("SELECT u FROM User u WHERE MONTH(u.birthday) = :month AND DAY(u.birthday) = :date")
    List<User> searchByBirthDay(int date, int month);
}
