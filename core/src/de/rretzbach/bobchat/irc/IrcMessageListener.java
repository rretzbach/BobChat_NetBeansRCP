package de.rretzbach.bobchat.irc;

/**
 *
 * @author rretzbach
 */
public interface IrcMessageListener {

    void onAction(String sender, String login, String hostname, String target, String action);

    void onMessage(String channel, String sender, String login, String hostname, String message);

    void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice);

    void onPrivateMessage(String sender, String login, String hostname, String message);
    
}
