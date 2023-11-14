package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.repository.MessageRepository;
import com.example.entity.Message;


@Transactional
@Service
public class MessageService {
    MessageRepository messageRepository;
    
    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Message postMessage(Message message){
       return messageRepository.save(message);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer message_id){
        Optional<Message> messageOptional = messageRepository.findById(message_id);
        if (messageOptional.isPresent()){
            return messageOptional.orElseThrow();
        }
        return null;
    }

    public void deleteMessageById(Integer message_id){
        messageRepository.deleteById(message_id);
    }

    public void updateMessageById(Integer message_id, String text){
        messageRepository.updateMessageText(message_id, text);
    }

    public List<Message> getAllMessagesFromUser(Integer posted_by){
        return messageRepository.getAllMessagesFromUser(posted_by);
    }

}
