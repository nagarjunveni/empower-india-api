package com.andhraempower.repository;

import com.andhraempower.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findById(Long id);

    @Query("SELECT u FROM User u WHERE " +
            "(:searchTerm IS NULL OR " +
            "   LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "   LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "   LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "   u.phoneNumber LIKE CONCAT('%', :searchTerm, '%')) AND " +
            "(:districtId IS NULL OR u.districtId = :districtId) AND " +
            "(:roleId IS NULL OR :roleId IN (SELECT r.id FROM u.roles r))")
    List<User> findUsers(@Param("searchTerm") String searchTerm,
                           @Param("districtId") Long districtId,
                           @Param("roleId") Long roleId);

    Optional<User> findByEmailOrPhoneNumber(String email, String phone);
}
