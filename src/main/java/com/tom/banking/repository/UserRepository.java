package com.tom.banking.repository;

import com.tom.banking.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findUserByUsername(String username);
    User findUserByIBAN(String IBAN);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET password = :password, email = :email", nativeQuery = true)
    void updateUser(@Param("password") String password, @Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user(first_name, last_name, IBAN, balance, username, password," +
            " birth_date, email) VALUES (:firstName, :lastName, :IBAN, :balance, :username," +
            " :password, :birthDate, :email)",
            nativeQuery = true)
    void saveUser(@Param("firstName") String firstName, @Param("lastName") String lastName,
                  @Param("IBAN") String IBAN, @Param("balance") Double balance,
                  @Param("username") String username,
                  @Param("password") String password, @Param("birthDate") LocalDate birthDate,
                  @Param("email") String email);

}
