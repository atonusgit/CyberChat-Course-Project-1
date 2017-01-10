package ato.cyberchat.domain;

import java.sql.Timestamp;
import java.util.Date;

public class ChatMessage
{
    private Timestamp timestamp;
    private String username;
    private String message;

    private static java.sql.Timestamp getCurrentTimeStamp()
    {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());
    }
    
    public ChatMessage(String usr, String msg)
    {
        this.timestamp = getCurrentTimeStamp();
        this.username = usr;
        this.message = msg;
    }
    
    public ChatMessage(Timestamp ts, String usr, String msg)
    {
        this.timestamp = ts;
        this.username = usr;
        this.message = msg;
    }
    
    public void setTimestamp(Timestamp ts)
    {
        this.timestamp = ts; // new java.util.Date();
    }
    
    public Date getTimestamp()
    {
        return timestamp;
    }
    
    public void setUsername(String usr)
    {
        this.username = usr;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setMessage(String msg)
    {
        this.message = msg;
    }

    public String getMessage()
    {
        return message;
    } 
}
