package de.rretzbach.bobchat.irc;

import de.rretzbach.bobchat.irc.message.PartMessage;
import de.rretzbach.bobchat.irc.message.JoinMessage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rretzbach
 */
public class Channel extends Conversation {
    private ArrayList<User> users = new ArrayList<User>();

    public Channel(Network network, String channelName) {
        super(network, channelName);
    }

    void onJoin(String nick) {
        if (network.getNick().equals(nick)) {
            addMessage(new JoinMessage(new Date(), this.name));
        } else {
            addMessage(new JoinMessage(new Date(), nick, this.name));
        }
    }

    void onPart(String nick) {
        if (network.getNick().equals(nick)) {
            addMessage(new PartMessage(new Date(), this.name));
        } else {
            addMessage(new PartMessage(new Date(), nick, this.name));
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
}
