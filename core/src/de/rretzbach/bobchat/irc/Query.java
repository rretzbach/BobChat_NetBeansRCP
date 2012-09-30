package de.rretzbach.bobchat.irc;

/**
 *
 * @author rretzbach
 */
public class Query extends Conversation {
    public Query(Network network, String nick) {
        super(network, nick);
    }

    @Override
    public void sendMessage(String message) {
        this.network.sendQueryMessage(name, message);
    }

    @Override
    public void close() {
        this.network.closeQuery(name);
    }
    
}
