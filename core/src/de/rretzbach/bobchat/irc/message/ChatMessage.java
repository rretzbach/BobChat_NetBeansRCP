/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.irc.message;

import java.util.Date;

/**
 *
 * @author rretzbach
 */
public class ChatMessage {
	public Date timestamp;
	public String nick;
	public String message;
	
	public ChatMessage(Date timestamp, String nick, String message) {
		this.timestamp = timestamp;
		this.nick = nick;
		this.message = message;
	}

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.timestamp != null ? this.timestamp.hashCode() : 0);
        hash = 23 * hash + (this.nick != null ? this.nick.hashCode() : 0);
        hash = 23 * hash + (this.message != null ? this.message.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChatMessage other = (ChatMessage) obj;
        return true;
    }
        
        
}
