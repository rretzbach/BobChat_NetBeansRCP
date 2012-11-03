package de.rretzbach.bobchat.irc;

import java.util.Date;

/**
 * 
 * @author rretzbach
 */
public class JoinMessage extends ChatMessage {
    public JoinMessage(Date timestamp, String nick, String channel) {
        super(timestamp, "-->", nick + " hat " + channel + " betreten");
    }
    
    public JoinMessage(Date timestamp, String channel) {
        super(timestamp, "-->", "Sie sind jetzt im Gespräch über " + channel);
    }
}
