package de.rretzbach.bobchat.irc.message;

import java.util.Date;

/**
 * TODO implement this
 * @author rretzbach
 */
public class QuitMessage extends ChatMessage {

    public QuitMessage(Date timestamp, String nick, String reason) {
        super(timestamp, "<--", nick + " has quit (" + reason + ")");
    }

    public QuitMessage(Date timestamp, String reason) {
        super(timestamp, "<--", "You have quit (" + reason + ")");
    }
}
