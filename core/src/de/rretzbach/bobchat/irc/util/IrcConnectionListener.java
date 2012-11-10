package de.rretzbach.bobchat.irc.util;

/**
 *
 * @author rretzbach
 */
public interface IrcConnectionListener {

    void onConnect();

    void onDisconnect();

    void onJoin(String channel, String sender, String login, String hostname);
    
    void onPart(String channel, String sender, String login, String hostname);
    
}
