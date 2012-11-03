package de.rretzbach.bobchat.irc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.User;

/**
 *
 * @author rretzbach
 */
public interface IrcConnection {

    void changeNick(String newNick);
    
    void onNickChange(String oldNick, String login, String hostname, String newNick);
    
    void onUserList(String channel, User[] users);

    void connect(String hostname) throws IOException, IrcException, NickAlreadyInUseException;
    
    void connect(String hostname, int port, String password) throws IOException, IrcException, NickAlreadyInUseException;

    void disconnect();
    
    boolean isConnected();

    void joinChannel(String channel);

    void partChannel(String channel);

    void quitServer();

    void reconnect() throws IOException, IrcException, NickAlreadyInUseException;

    void sendAction(String target, String action);

    void sendMessage(String target, String message);

    void sendNotice(String target, String notice);

    void setEncoding(String charset) throws UnsupportedEncodingException;
    
    String getNick();
    
}
