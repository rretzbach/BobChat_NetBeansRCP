package de.rretzbach.bobchat.irc.message;

import de.rretzbach.bobchat.irc.message.ChatMessage;

/**
 *
 * @author rretzbach
 */
public interface ChatMessageListener {
    void onMessage(ChatMessage message);
}
