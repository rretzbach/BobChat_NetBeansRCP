/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.irc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.openide.util.Exceptions;

/**
 *
 * @author rretzbach
 */
public class TestBot implements IrcBot {

    private String hostname;
    private final Identity identity;

    public TestBot(Identity identity) {
        this.identity = identity;

    }

    @Override
    public void joinChannel(final String channel) {
        final String nick = identity.nick;

        // self join
        onJoin(channel, nick, "Woot", TestBot.this.hostname);

        // channel message types
        onMessage(channel, "TestNick", "TestLogin", hostname, "Veggies sunt bona vobis, proinde vos postulo esse magis arugula kombu soybean bitterleaf water spinach catsear chard daikon beet greens jícama squash wakame seakale silver beet lentil chickweed wattle seed.");
        
        
//        repeat(500, new Runnable() {
//
//            @Override
//            public void run() {
//                onMessage(channel, "TestNick", "TestLogin", hostname, "\u00034red");
//            }
//        });

        onMessage(channel, "TestNick", "TestLogin", hostname, "http://imgur.com/a/AW4Os this");
        
        onMessage(channel, "TestNick", "TestLogin", hostname, "\u00033,0Die richtige Antwort war: \u00032,0 < Thomas Wolfe > \u00032,0");
        onJoin(channel, "Someone", "Someone", hostname);
        onPart(channel, "Someone", "Someone", hostname);
        onAction("Someone", "Someone", hostname, channel, "is feeling well");
        onNotice("Someone", "Someone", hostname, channel, "this is a notice");
        
        ArrayList<User> arrayList = new ArrayList<User>();
        arrayList.add(new User("@", "Someone"));
        onUsernameList(channel, arrayList);
        
        onQuit("Someone", "Someone", hostname, "needs some fresh air");
        
        // query message types
        onPrivateMessage("Someone", "Someone", hostname, "hi");
        onAction("Someone", "Someone", hostname, nick, "is feeling well");
        onNotice("Someone", "Someone", hostname, nick, "this is a notice");
    }

    @Override
    public void connect(String hostname) {
        this.hostname = hostname;
        onConnect();
    }

    @Override
    public void onConnect() {
    }

    @Override
    public void onJoin(String channel, String sender, String login, String hostname) {
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
    }

    @Override
    public void changeNick(String newNick) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void disconnect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void partChannel(String channel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void quitServer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reconnect() throws IOException, IrcException, NickAlreadyInUseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendAction(String target, String action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendMessage(String target, String message) {
        //onMessage(target, "TestNick", "TestLogin", hostname, message);
    }

    @Override
    public void sendNotice(String target, String notice) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setEncoding(String charset) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onDisconnect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onPart(String channel, String sender, String login, String hostname) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onAction(String sender, String login, String hostname, String target, String action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onPrivateMessage(String sender, String login, String hostname, String message) {
    }

    @Override
    public String getNick() {
        return identity.nick;
    }

    private void delay(int i, final Runnable runnable) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, i);
    }

    private void repeat(int interval, final Runnable runnable) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, new Date(), interval);
    }

    @Override
    public void connect(String hostname, int port, String password) throws IOException, IrcException, NickAlreadyInUseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onNickChange(String oldNick, String login, String hostname, String newNick) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onUsernameList(String channel, List<User> users) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
