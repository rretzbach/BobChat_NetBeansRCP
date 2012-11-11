package de.rretzbach.bobchat.irc;

import de.rretzbach.bobchat.irc.message.PartMessage;
import de.rretzbach.bobchat.irc.message.JoinMessage;
import de.rretzbach.bobchat.irc.message.NickChangeMessage;
import de.rretzbach.bobchat.irc.message.QuitMessage;
import de.rretzbach.bobchat.irc.util.NickChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * TODO react to certain highlight words, e.g. the currently used nick
 * @author rretzbach
 */
public class Channel extends Conversation {

    private ArrayList<User> users = new ArrayList<User>();

    public Channel(Network network, String channelName) {
        super(network, channelName);
    }

    void onJoin(String nick) {
        if (isMe(nick)) {
            addMessage(new JoinMessage(new Date(), this.name));
        } else {
            addMessage(new JoinMessage(new Date(), nick, this.name));
            // TODO determine prefix
            users.add(new User("", nick));
        }
    }

    void onPart(String nick) {
        if (isMe(nick)) {
            addMessage(new PartMessage(new Date(), this.name));
        } else {
            addMessage(new PartMessage(new Date(), nick, this.name));
            users.remove(new User("", nick));
        }
    }

    @Override
    public void sendMessage(String message) {
        this.network.sendMessage(name, message);
    }

    @Override
    public void close() {
        this.network.closeChannel(this.name);
    }

    public List<User> getUsers() {
        return users;
    }

    void setUsers(Collection<User> users) {
        this.users.clear();
        this.users.addAll(users);
    }

    void onQuit(String nick, String reason) {
        if (isMe(nick)) {
            addMessage(new QuitMessage(new Date(), reason));
        } else {
            addMessage(new QuitMessage(new Date(), nick, reason));
            users.remove(new User("", nick));
        }
    }

    private boolean isMe(String nick) {
        return network.getNick().equals(nick);
    }

    void onNickChange(String newNick, String oldNick) {
        if (isMe(oldNick) || isMe(newNick)) {
            for (NickChangeListener listener : network.getNickChangeListeners()) {
                listener.onNickChange(newNick, oldNick);
            }
            // display own nickchange
            addMessage(new NickChangeMessage(new Date(), newNick));
        } else {
            // nickchange for other ppl
            addMessage(new NickChangeMessage(new Date(), newNick, oldNick));
            
            // change user in channel list
            User oldUser = new User("", oldNick);
            for (Iterator<User> it = users.iterator(); it.hasNext();) {
                User user = it.next();
                if (user.equals(oldUser)) {
                    user.setNick(newNick);
                }
            }
        }
    }
}
