package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.Account;


public interface AccountRepository extends JpaRepository<Account, Integer> {

    /***
     * 
     * @param username
     * @return the account that matches a certain Username string for the Username field
     */
    Account findAccountByUsername(String username);

    /***
     * 
     * @param username
     * @return if an account with the given Username exists in the database
     */
    boolean existsByUsername(String username);

    /***
     * 
     * @param username
     * @param password
     * @return if an account with the given Username and password exists in the database
     */
    boolean existsByUsernameAndPassword(String username, String password);

}
