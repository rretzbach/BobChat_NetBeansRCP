package de.rretzbach.bobchat.irc;

import java.util.Date;

/**
 *
 * @author rretzbach
 */
public class Channel extends Conversation {

    public Channel(Network network, String channelName) {
        super(network, channelName);
    }

    void onJoin(String nick) {
        if (network.getNick().equals(nick)) {
            addMessage(new JoinMessage(new Date(), nick, this.name));
        } else {
            addMessage(new JoinMessage(new Date(), this.name));
        }
    }

    void onPart(String nick) {
        if (network.getNick().equals(nick)) {
            addMessage(new PartMessage(new Date(), nick, this.name));
        } else {
            addMessage(new PartMessage(new Date(), this.name));
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
}
