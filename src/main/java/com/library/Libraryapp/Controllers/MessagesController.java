package com.library.Libraryapp.Controllers;

import com.library.Libraryapp.Entity.Message;
import com.library.Libraryapp.Service.MessagesService;
import com.library.Libraryapp.Utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("Http://localhost:3000")
@RestController
@RequestMapping("/api/messages")
public class MessagesController {

    MessagesService messagesService;

    @Autowired
    public MessagesController(MessagesService messagesService){
        this.messagesService=messagesService;
    }

    @PostMapping("/secure/add/message")
    public void postMessage(@RequestHeader(value = "Authorization") String token,@RequestBody Message messageRequest){
        String userEmail= ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        messagesService.postMessage(messageRequest,userEmail);
    }
}
