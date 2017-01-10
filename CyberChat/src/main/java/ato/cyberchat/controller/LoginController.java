package ato.cyberchat.controller;

import ato.cyberchat.database.ChatDatabase;
import ato.cyberchat.domain.ChatMessage;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController
{
    String username = "";
    String password = "";
    
    @RequestMapping("/login")
    public String login()
    {
        return "login";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String gate(@RequestParam("username") String usernamePOST, @RequestParam("password") String passwordPOST)
    {
        username = usernamePOST;
        password = passwordPOST;
        
        if (username.equals("atonus") && password.equals("123"))
        {
            return "redirect:/chatroom";
        }
        else
        {
            return "redirect:/login";
        }
    }
    
    // @ResponseBody
    @RequestMapping("/chatroom")
    public String chatroom(Model model) throws SQLException
    {
        if (username.equals("atonus") && password.equals("123"))
        {
            ChatDatabase.init();
            
            ArrayList<ChatMessage> chatMessageList = null;
            chatMessageList = ChatDatabase.getAllChatMessages();
            model.addAttribute("chatmessages", chatMessageList);
            model.addAttribute("username", username);
            
            return "chatroom";
        }
        else
        {
            return "Get da fug out.";
        }
    }
    
    @RequestMapping(value = "/chatroom", method = RequestMethod.POST)
    public String newMessage(@RequestParam("message") String message, @RequestParam("name") String name) throws SQLException
    {    
        ChatDatabase.insert(name, message);
        return "redirect:/chatroom";
    }
}
