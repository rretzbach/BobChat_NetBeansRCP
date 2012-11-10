/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.irc.util;

import de.rretzbach.bobchat.irc.Conversation;
import de.rretzbach.bobchat.irc.Conversation;
import de.rretzbach.bobchat.irc.Network;

/**
 *
 * @author rretzbach
 */
public interface ConversationListener {
    void addedConversation(Conversation conversation);
    void removedConversation(Conversation conversation);
}
