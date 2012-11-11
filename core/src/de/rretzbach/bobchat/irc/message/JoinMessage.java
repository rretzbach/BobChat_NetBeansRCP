package de.rretzbach.bobchat.irc.message;

import java.util.Date;

/**
 * 
 * @author rretzbach
 */
public class JoinMessage extends ChatMessage {
    public JoinMessage(Date timestamp, String nick, String channel) {
        super(timestamp, "-->", nick + " has joined " + channel);
    }
    
    public JoinMessage(Date timestamp, String channel) {
        super(timestamp, "-->", "You have joined " + channel);
    }
}
