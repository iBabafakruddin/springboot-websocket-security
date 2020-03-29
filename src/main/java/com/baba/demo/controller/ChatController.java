package com.baba.demo.controller;

import java.security.Principal;

import com.baba.demo.dto.WSMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;






@Controller
public class ChatController {


    @MessageMapping("/sendchat") //receive from sender
    @SendTo("/topic/chat") //send to topic
    public WSMessage publicChat(WSMessage message, Principal principal) throws Exception {
        System.out.println("Got message from client: "+ message);
        Thread.sleep(1000); // simulated delay
        return message;
    }

    @MessageMapping("/chat/private") //receive from sender
    @SendToUser("/chat/private") //send to topic
    public WSMessage privateChat(WSMessage message, Principal principal) throws Exception {
        System.out.println("Got message from"+principal.getName()+": "+ message);
        Thread.sleep(1000); // simulated delay
        return message;
    }

}
