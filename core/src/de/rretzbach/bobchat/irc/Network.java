package de.rretzbach.bobchat.irc;

import de.rretzbach.bobchat.core.ChannelListTopComponent;
import de.rretzbach.bobchat.core.ConversationListener;
import de.rretzbach.bobchat.core.ConversationTreeNodes;
import de.rretzbach.bobchat.util.ChannelListAction;
import de.rretzbach.bobchat.util.WindowUtil;
import java.awt.Window;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.openide.util.Exceptions;

/**
 *
 * @author rretzbach
 */
public class Network implements IrcConnectionListener, IrcMessageListener, IrcConnection {

    protected List<Conversation> conversations = new ArrayList<Conversation>();

    public List<Conversation> getConversations() {
        return conversations;
    }
    private final String hostname;
    private IrcBot bot;
    private List<Runnable> connectActions = new ArrayList<Runnable>();
    private List<ConversationEstablishedAction> joinActions = new ArrayList<ConversationEstablishedAction>();
    private Status status = Status.DISCONNECTED;
    private Identity identity;
    private List<ConversationListener> listener = new ArrayList<ConversationListener>();

    public Network(Identity identity, String hostname) {
        this.identity = identity;
        this.hostname = hostname;
        // exchange implementations by using either TestBot or MainBot
        this.bot = new MainBot(identity) {
            @Override
            public void onConnect() {
                Network.this.onConnect();
            }

            @Override
            public void onJoin(String channel, String sender, String login, String hostname) {
                Network.this.onJoin(channel, sender, login, Network.this.hostname);
            }

            @Override
            public void onMessage(String channel, String sender, String login, String hostname, String message) {
                Network.this.onMessage(channel, sender, login, Network.this.hostname, message);
            }

            @Override
            public void onPart(String channel, String sender, String login, String hostname) {
                Network.this.onPart(channel, sender, login, Network.this.hostname);
            }

            @Override
            public void onPrivateMessage(String sender, String login, String hostname, String message) {
                Network.this.onPrivateMessage(sender, login, Network.this.hostname, message);
            }

            @Override
            public void onAction(String sender, String login, String hostname, String target, String action) {
                Network.this.onAction(sender, login, hostname, target, action);
            }

            @Override
            public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
                Network.this.onNotice(sourceNick, sourceLogin, sourceHostname, target, notice);
            }
        };
    }

    @Override
    public void onConnect() {
        status = Status.CONNECTED;
        final Iterator<Runnable> it = connectActions.iterator();
        while (it.hasNext()) {
            it.next().run();
            it.remove();
        }
    }

    @Override
    public synchronized boolean isConnected() {
        return bot.isConnected();
    }

    @Override
    public void onDisconnect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onJoin(final String channel, final String sender, String login, String hostname) {
        Channel findChannelByName = findChannelByName(channel);
        if (bot.getNick().equals(sender)) {
            onChannelJoin(findChannelByName);
        }
        findChannelByName.onJoin(sender);
    }

    private void onChannelJoin(Channel channel) {
        final Iterator<ConversationEstablishedAction> it = joinActions.iterator();
        while (it.hasNext()) {
            final ConversationEstablishedAction joinAction = it.next();
            final String conversationName = joinAction.getConversationName();
            final String channelName = channel.getName();
            if (conversationName.equals(channelName)) {
                joinAction.performAction(channel);
                it.remove();
            }
        }
    }

    private void onChannelPart(String channel) {
        // do nothing
    }

    @Override
    public void onPart(final String channel, final String sender, String login, String hostname) {
        if (sender.equals(bot.getNick())) {
            onChannelPart(channel);
            return;
        }

        Channel findChannelByName = findChannelByName(channel);
        findChannelByName.onPart(sender);
    }

    @Override
    public void onAction(String sender, String login, String hostname, String target, String action) {
        if (target.startsWith("#")) {
            onChannelAction(sender, target, action);
        } else {
            onPrivateAction(sender, action);
        }
    }

    @Override
    public void onMessage(final String channel, final String sender, final String login, String hostname, final String message) {
        Channel findChannelByName = findChannelByName(channel);
        findChannelByName.onMessage(sender, login, message);
    }

    @Override
    public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
        if (target.startsWith("#")) {
            onChannelNotice(sourceNick, target, notice);
        } else {
            onPrivateNotice(sourceNick, notice);
        }
    }

    @Override
    public void onPrivateMessage(final String sender, final String login, final String hostname, final String message) {
        Query query = findQueryByName(sender);
        if (query == null) {
            query = getQuery(sender);
            WindowUtil.openConversationWindow(hostname, query);
        }
        query.addMessage(new PrivateMessage(sender, message));
    }

    public String getHostname() {
        return hostname;
    }

    @Override
    public String getNick() {
        return bot.getNick();
    }

    @Override
    public void changeNick(String newNick) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void connect(String hostname) throws IOException, IrcException, NickAlreadyInUseException {
        status = Status.CONNECTING;
        bot.connect(hostname, 6667, identity.password);
    }

    @Override
    public void disconnect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void joinChannel(String channel) {
        bot.joinChannel(channel);
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
        System.out.println("Sending message "+target+" to "+message);
        onMessage(target, bot.getNick(), bot.getNick(), hostname, message);
        bot.sendMessage(target, message);
    }

    @Override
    public void sendNotice(String target, String notice) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setEncoding(String charset) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private synchronized boolean isConnecting() {
        return status == Status.CONNECTING;
    }

    public Channel getChannel(final String name) {
        Channel channel = findChannelByName(name);
        if (channel == null) {
            channel = new Channel(this, name);
            conversations.add(channel);
            fireAddConversation(channel);

            // connect and join
            final Runnable joinChannelAction = new Runnable() {
                @Override
                public void run() {
                    joinActions.add(new ConversationEstablishedAction() {
                        @Override
                        public void performAction(Conversation conversation) {
                            System.out.println("joined the channel " + conversation.getName());
                        }

                        @Override
                        public String getConversationName() {
                            return name;
                        }
                    });
                    joinChannel(name);
                }
            };

            if (isConnected()) {
                joinChannelAction.run();
            } else {
                connectActions.add(joinChannelAction);
            }

            if (!isConnecting() && !isConnected()) {
                try {
                    connect(hostname);
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        return channel;
    }

    public Query getQuery(final String name) {
        Query query = findQueryByName(name);
        if (query == null) {
            query = new Query(this, name);
            conversations.add(query);
            fireAddConversation(query);

            // connect
            final Runnable queryReadyAction = new Runnable() {
                @Override
                public void run() {
                    System.out.println("query is ready " + name);
                }
            };

            if (isConnected()) {
                queryReadyAction.run();
            } else {
                connectActions.add(queryReadyAction);
            }

            if (!isConnecting() && !isConnected()) {
                try {
                    connect(hostname);
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        return query;
    }

    private Channel findChannelByName(String name) {
        Iterator<Conversation> iterator = new ArrayList<Conversation>(conversations).iterator();
        while (iterator.hasNext()) {
            Conversation conversation = iterator.next();
            if (conversation.getName().equals(name) && conversation instanceof Channel) {
                return (Channel) conversation;
            }
        }
        return null;
    }

    private Query findQueryByName(String name) {
        Iterator<Conversation> iterator = new ArrayList<Conversation>(conversations).iterator();
        while (iterator.hasNext()) {
            Conversation conversation = iterator.next();
            System.out.println("looking for a query, but found " + conversation);
            if (conversation.getName().equals(name) && conversation instanceof Query) {
                return (Query) conversation;
            }
        }
        return null;
    }

    void sendQueryMessage(String name, String message) {
        Query findQueryByName = findQueryByName(name);
        findQueryByName.addMessage(new PrivateMessage(bot.getNick(), message));
        bot.sendMessage(name, message);
    }

    void closeChannel(String name) {
        final Channel channel = findChannelByName(name);
        conversations.remove(channel);
        bot.partChannel(name);
    }

    void closeQuery(String name) {
        final Query query = findQueryByName(name);
        conversations.remove(query);
        bot.partChannel(name);
    }

    @Override
    public void connect(String hostname, int port, String password) throws IOException, IrcException, NickAlreadyInUseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void onChannelAction(String nick, String channel, String action) {
        Channel findChannel = findChannelByName(channel);
        findChannel.onAction(nick, action);
    }

    private void onPrivateAction(String nick, String action) {
        Query query = findQueryByName(nick);
        query.onAction(nick, action);
    }

    private void onChannelNotice(String nick, String channel, String notice) {
        Channel findChannel = findChannelByName(channel);
        findChannel.onNotice(nick, notice);
    }

    private void onPrivateNotice(String nick, String notice) {
        // TODO not all notices come from a query!
//        Query query = findQueryByName(nick);
//        query.onNotice(nick, notice);
    }

    public void addConversationListener(ConversationTreeNodes listener) {
        this.listener.add(listener);
    }

    private void fireAddConversation(Conversation conversation) {
        for (ConversationListener listener: this.listener) {
            listener.addedConversation(conversation);
        }
    }

    void sendAnyMessage(String target, String message) {
        if (target.startsWith("#")) {
            sendMessage(target, message);
        } else {
            sendQueryMessage(target, message);
        }
    }

    public enum Status {
        DISCONNECTED, CONNECTING, CONNECTED
    }
}
