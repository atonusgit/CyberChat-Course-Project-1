package ato.cyberchat.database;

import ato.cyberchat.domain.ChatMessage;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.h2.tools.RunScript;

public class ChatDatabase
{  
    public static void init() throws SQLException
    {
        Connection connection = DriverManager.getConnection("jdbc:h2:file:./chat_database", "sa", "");
        try
        {
            // If database has not yet been created, insert content
            RunScript.execute(connection, new FileReader("sql/chat_database-schema.sql"));
            RunScript.execute(connection, new FileReader("sql/chat_database-import.sql"));
        }
        catch (Throwable t)
        {
            System.err.println(t.getMessage());
        }
        connection.close();
    }
    
    public static ArrayList<ChatMessage> getAllChatMessages() throws SQLException
    {
        // Open connection
        Connection connection = DriverManager.getConnection("jdbc:h2:file:./chat_database", "sa", "");

        // Execute query // UNSECURE
        String query = "SELECT * FROM chatroom";
        ResultSet rs = connection.createStatement().executeQuery(query);

        ArrayList<ChatMessage> chatMessageList = new ArrayList<>();
        
        while (rs.next())
        {
            ChatMessage chatmessage = new ChatMessage(rs.getTimestamp("ts"),rs.getString("usr"), rs.getString("msg"));
            chatMessageList.add(chatmessage);
        }
        
        return chatMessageList;
    }
    
    public static void insert(String usr, String msg) throws SQLException
    {
        ChatMessage newMessage = new ChatMessage(usr, msg);
        
        // Open connection
        Connection connection = DriverManager.getConnection("jdbc:h2:file:./chat_database", "sa", "");

        // Execute query // UNSECURE
        String query = "INSERT INTO chatroom(ts, usr, msg) VALUES ('" + newMessage.getTimestamp() + "', '" + newMessage.getUsername() + "', '" + newMessage.getMessage() + "')";
        connection.createStatement().executeUpdate(query);
        
        connection.close();
    }
}
