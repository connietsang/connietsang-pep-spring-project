package com.example.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    AccountService accountService;
    AccountRepository accountRepository;
    MessageService messageService;
    MessageRepository messageRepository;

    @Autowired
    public SocialMediaController(AccountService accountService, AccountRepository accountRepository, 
    MessageService messageService, MessageRepository messageRepository){
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.messageService = messageService;
        this.messageRepository = messageRepository;
    }

    @PostMapping(value="/register")
    public ResponseEntity<Account> postAccount(@RequestBody Account account){
        if (account.getPassword().length() < 4 || account.getUsername().equals("")){
            return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);
        }
        else if(accountRepository.existsByUsername(account.getUsername())){
            return new ResponseEntity<Account>(HttpStatus.CONFLICT);
        }
        Account registeredAccount = accountService.registerAccount(account);
        return new ResponseEntity<Account>(registeredAccount, HttpStatus.OK);
    }

    @PostMapping(value="/login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account){
        if (accountRepository.existsByUsernameAndPassword(account.getUsername(), account.getPassword())){
            return new ResponseEntity<Account>(accountRepository.findAccountByUsername(account.getUsername()),HttpStatus.OK);
        }
        return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value="/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message){
        if (!accountRepository.existsById(message.getPosted_by()) || message.getMessage_text().equals("") ||
        message.getMessage_text().length() > 254 ){
            return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
        }
        Message postedMessage = messageService.postMessage(message);
        return new ResponseEntity<Message>(postedMessage, HttpStatus.OK);
    }

    @GetMapping(value="/messages")
    public ResponseEntity<List<Message>> getMessages(){
        List<Message> messages = messageService.getAllMessages();
        return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
    }

    @GetMapping(value="/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer message_id) throws NoSuchElementException{
        try{
            Message message = messageService.getMessageById(message_id);
            return new ResponseEntity<Message>(message, HttpStatus.OK);
        
        }catch(NoSuchElementException e){
            return new ResponseEntity<Message>(HttpStatus.OK);
        }   
    }

    @DeleteMapping(value="/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer message_id){
        if (messageRepository.existsById(message_id)){
            messageService.deleteMessageById(message_id);
            return new ResponseEntity<Integer>(1, HttpStatus.OK);
        }
        return new ResponseEntity<Integer>(HttpStatus.OK);
    }

    @PatchMapping(value="/messages/{message_id}")
    public ResponseEntity<Integer> updateMessageById(@RequestBody Message newMessage, @PathVariable Integer message_id){
        String newMessageText = newMessage.getMessage_text();
        if (messageRepository.existsById(message_id) && newMessageText.length() < 255 && !newMessageText.equals("")){
            messageService.updateMessageById(message_id, newMessageText);
            return new ResponseEntity<Integer>(1, HttpStatus.OK);
        }
        return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value="/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAllMessagesFromUser(@PathVariable Integer account_id){
        if (accountRepository.existsById(account_id)){
            return new ResponseEntity<List<Message>>(messageService.getAllMessagesFromUser(account_id), HttpStatus.OK);
        }
        return new ResponseEntity<List<Message>>(HttpStatus.OK);
    }

}
