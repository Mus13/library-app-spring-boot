package com.library.Libraryapp.Service;

import com.library.Libraryapp.Entity.Message;
import com.library.Libraryapp.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessagesService {

    private MessageRepository messageRepository;

    @Autowired
    public MessagesService(MessageRepository messageRepository){
        this.messageRepository=messageRepository;
    }

    public void postMessage(Message messageRequest, String userEmail){

        Message message=new Message(messageRequest.getTitle(), messageRequest.getQuestion());
        message.setUserEmail(userEmail);
        messageRepository.save(message);
    }
}
