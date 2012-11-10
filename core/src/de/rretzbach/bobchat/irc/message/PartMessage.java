package de.rretzbach.bobchat.irc.message;

import java.util.Date;

/**
 *
 * @author rretzbach
 */
public class PartMessage extends ChatMessage {

    public PartMessage(Date timestamp, String nick, String channel) {
        super(timestamp, "<--", nick + " hat " + channel + " verlassen");
    }

    public PartMessage(Date timestamp, String channel) {
        super(timestamp, "<--", "Sie haben Kanal " + channel + " verlassen");
    }
}
