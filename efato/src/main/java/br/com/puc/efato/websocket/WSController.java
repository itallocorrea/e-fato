package br.com.puc.efato.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WSController {


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        try{
            Thread.sleep(1000); // simulated delay
            return new Greeting("Hello aa");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}