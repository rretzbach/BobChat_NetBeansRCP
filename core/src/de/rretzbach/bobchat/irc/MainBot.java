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

    public MainBot(Identity identity) {
        setName(identity.nick);
        try {
            setEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void onPart(String channel, String sender, String login, String hostname) {
        System.out.println("Part Event: channel=" + channel + ",sender=" + sender + ",login=" + login + ",hostname=" + hostname);
    }

    @Override
    public void onJoin(String channel, String sender, String login, String hostname) {
        System.out.println("Join Event: channel=" + channel + ",sender=" + sender + ",login=" + login + ",hostname=" + hostname);
    }

    @Override
    public void onDisconnect() {
        System.out.println("Disconnect Event");
    }

    @Override
    public void onConnect() {
        System.out.println("Connect Event");
    }

    @Override
    public void onPrivateMessage(String sender, String login, String hostname, String message) {
        System.out.println("Privmsg Event: sender=" + sender + ",login=" + login + ",hostname=" + hostname + "message=" + message);
    }

    @Override
    public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
        System.out.println("Notice Event: sourceNick=" + sourceNick + ",sourceLogin=" + sourceLogin + ",sourceHostname=" + sourceHostname + "target=" + target + "notice=" + notice);
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        System.out.println("Message Event: channel=" + channel + ",sender=" + sender + ",login=" + login + ",hostname=" + hostname + "message=" + message);
    }

    @Override
    public void onAction(String sender, String login, String hostname, String target, String action) {
        System.out.println("Action Event: sender=" + sender + ",login=" + login + ",hostname=" + hostname + "target=" + target + "action=" + action);
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
    public void onVersion(String sourceNick, String sourceLogin, String sourceHostname, String target) {
        this.sendRawLine("NOTICE " + sourceNick + " :\u0001VERSION " + "BobChat " + "1.0 alpha" + "\u0001");
    }

    @Override
    public void onNickChange(String oldNick, String login, String hostname, String newNick) {
        super.onNickChange(oldNick, login, hostname, newNick);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        super.onQuit(sourceNick, sourceLogin, sourceHostname, reason);
    }
    
    
}
