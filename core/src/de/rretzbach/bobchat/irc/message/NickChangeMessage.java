package de.rretzbach.bobchat.irc.message;

import java.util.Date;

/**
 * TODO implement this
 * @author rretzbach
 */
public class NickChangeMessage extends ChatMessage {

    public NickChangeMessage(Date timestamp, String newNick, String oldNick) {
        super(timestamp, "*", oldNick + " is now known as " + newNick);
    }

    public NickChangeMessage(Date timestamp, String newNick) {
        super(timestamp, "*", "You are now known as " + newNick);
    }
}
