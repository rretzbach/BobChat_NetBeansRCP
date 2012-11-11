/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.irc;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.openide.util.Exceptions;

/**
 *
 * @author rretzbach
 */
public class MainBot extends PircBot implements IrcBot {
    private final String hostname;
    private IrcBotDelegate delegate;

    public MainBot(Identity identity, String hostname) {
        this.hostname = hostname;
        setName(identity.nick);
        try {
            setEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Exceptions.printStackTrace(ex);
        }
        setLogin(identity.nick);
        setVersion("BobChat 1.0 alpha Java IRC client - " + "https://github.com/rretzbach/BobChat");
    }

    @Override
    public void onPart(String channel, String sender, String login, String hostname) {
        delegate.onPart(channel, sender, login, hostname);
    }

    @Override
    public void onJoin(String channel, String sender, String login, String hostname) {
        delegate.onJoin(channel, sender, login, hostname);
    }

    @Override
    public void onDisconnect() {
        delegate.onDisconnect();
    }

    @Override
    public void onConnect() {
        delegate.onConnect();
    }

    @Override
    public void onPrivateMessage(String sender, String login, String hostname, String message) {
        delegate.onPrivateMessage(sender, login, hostname, message);
    }

    @Override
    public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
        delegate.onNotice(sourceNick, sourceLogin, sourceHostname, target, notice);
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        onMessage(channel, sender, login, hostname, message);
    }

    @Override
    public void onAction(String sender, String login, String hostname, String target, String action) {
        delegate.onAction(sender, login, hostname, target, action);
    }

    @Override
    public void onServerResponse(int code, String response) {
        System.out.println("Serverresponse Event: code=" + code + ",response=" + response);
    }

    @Override
    public void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel) {
        System.out.println("Invite Event: targetNick=" + targetNick + ",sourceNick=" + sourceNick + ",sourceLogin=" + sourceLogin + ",sourceHostname=" + sourceHostname + "channel=" + channel);
    }

    @Override
    public void onNickChange(String oldNick, String login, String hostname, String newNick) {
        delegate.onNickChange(oldNick, login, hostname, newNick);
    }

    @Override
    public void onUserList(String channel, User[] users) {
        List<de.rretzbach.bobchat.irc.User> usernames = new ArrayList<de.rretzbach.bobchat.irc.User>(users.length);
        for (User user : users) {
            usernames.add(new de.rretzbach.bobchat.irc.User(user.getPrefix(), user.getNick()));
         }
        onUsernameList(channel, usernames);
    }

    @Override
    public void onUsernameList(String channel, List<de.rretzbach.bobchat.irc.User> users) {
        delegate.onUsernameList(channel, users);
    }

    @Override
    public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        delegate.onQuit(sourceNick, sourceLogin, sourceHostname, reason);
    }

    @Override
    public void setDelegate(IrcBotDelegate delegate) {
        this.delegate = delegate;
    }
    
    
}
