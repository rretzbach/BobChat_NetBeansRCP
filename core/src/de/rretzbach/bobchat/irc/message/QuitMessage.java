package de.rretzbach.bobchat.irc.message;

import java.util.Date;

/**
 * TODO implement this
 * @author rretzbach
 */
public class QuitMessage extends ChatMessage {

    public QuitMessage(Date timestamp, String nick, String channel) {
        super(timestamp, "<--", nick + " hat " + channel + " verlassen");
    }

    public QuitMessage(Date timestamp, String channel) {
        super(timestamp, "<--", "Sie haben Kanal " + channel + " verlassen");
    }
}
