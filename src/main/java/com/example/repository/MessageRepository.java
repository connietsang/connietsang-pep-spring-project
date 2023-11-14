package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Message;


public interface MessageRepository extends JpaRepository<Message, Integer>{

    /***
     * updates a message identified by its message_id and replaces its message_text field 
     * @param message_id
     * @param message_text
     */
    @Modifying
    @Query("update Message m set m.message_text = :message_text where m.message_id = :message_id")
    void updateMessageText(@Param(value="message_id") Integer message_id, @Param(value="message_text") String message_text);


    /***
     * selects all messages posted by a user, identified by the posted_by field 
     * @param posted_by
     * @return
     */
    @Modifying
    @Query(value="select * from Message m where m.posted_by = :posted_by", nativeQuery = true)
    List<Message> getAllMessagesFromUser(@Param("posted_by") Integer posted_by);
   
}
