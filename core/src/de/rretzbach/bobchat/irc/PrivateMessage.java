/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.irc;

import java.util.Date;

/**
 *
 * @author rretzbach
 */
class PrivateMessage extends ChatMessage {

    public PrivateMessage(String sender, String message) {
        super(new Date(), sender, message);
    }
    
}
