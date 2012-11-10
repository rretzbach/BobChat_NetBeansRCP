/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.irc.util;

import de.rretzbach.bobchat.irc.Conversation;

/**
 *
 * @author rretzbach
 */
public class ConversationEstablishedAction {
    private String conversationName;
    
    public void performAction(Conversation conversation){};

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public String getConversationName() {
        return this.conversationName;
    }
}
