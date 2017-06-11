package ato.cyberchat.controller;

import ato.cyberchat.database.ChatDatabase;
import ato.cyberchat.domain.ChatMessage;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.h2.tools.RunScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController
{
    String username = "";
    String password = "";
    String givenPassword = "x";
    
    public static void init() throws SQLException
    {
        
        // User database
        Connection connection = DriverManager.getConnection("jdbc:h2:file:./user_database", "sa", "");
        try {
            // If database has not yet been created, insert content
            RunScript.execute(connection, new FileReader("sql/user_database-schema.sql"));
            RunScript.execute(connection, new FileReader("sql/user_database-import.sql"));
        }
        catch (Throwable t


            ) {
                System.err.println(t.getMessage());
        }

        connection.close ();
        
    }
    
    @RequestMapping("/login")
    public String login() throws SQLException
    {
        LoginController.init();
        return "login";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String gate(@RequestParam("username") String usernamePOST, @RequestParam("password") String passwordPOST) throws SQLException
    {
        username = usernamePOST;
        password = passwordPOST;
        
        // Open connection
        Connection connection = DriverManager.getConnection("jdbc:h2:file:./user_database", "sa", "");
        
        // Execute query // UNSECURE
        String query = "SELECT pw FROM users WHERE usr = '" + username + "'";
        System.out.println(query);
        ResultSet rs = connection.createStatement().executeQuery(query);
        
        try {
            while (rs.next()) {
                givenPassword = rs.getString(1);
                System.out.println("INPUR PW: " + password + " DB PW: "+ givenPassword);
                
                connection.close();
                
                if (password.equals(givenPassword))
                {
                    return "redirect:/chatroom";
                } else {
                    return "redirect:/login";
                }
                
            }
        } finally {
            rs.close();
        }

        return "redirect:/login";
    }
    
    // @ResponseBody
    @RequestMapping("/chatroom")
    public String chatroom(Model model) throws SQLException
    {
        if (password.equals(givenPassword))
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
