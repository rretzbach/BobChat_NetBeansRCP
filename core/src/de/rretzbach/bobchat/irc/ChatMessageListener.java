package de.rretzbach.bobchat.irc;

/**
 *
 * @author rretzbach
 */
public interface ChatMessageListener {
    void onMessage(ChatMessage message);
}
