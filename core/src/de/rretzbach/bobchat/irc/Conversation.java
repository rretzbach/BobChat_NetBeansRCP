package de.rretzbach.bobchat.irc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rretzbach
 */
public class Conversation {
    
    private static String COMMAND_PREFIX = "/";
    private static String COMMAND_ESCAPE_PREFIX = "//";

    protected Network network;
    protected List<ChatMessage> messages = new ArrayList<ChatMessage>();
    protected List<ChatMessageListener> listener = new ArrayList<ChatMessageListener>();

    public Conversation(Network network, String name) {
        this.network = network;
        this.name = name;
    }

    public Network getNetwork() {
        return network;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMessageListener(ChatMessageListener listener) {
        if (!this.listener.contains(listener)) {
            this.listener.add(listener);
            if (!messages.isEmpty()) {
                for (ChatMessage msg : this.messages) {
                    listener.onMessage(msg);
                }
            }
        }
    }

    void onMessage(String sender, String login, String message) {
        ChatMessage chatMessage = new ChatMessage(new Date(), sender, message);
        addMessage(chatMessage);
    }

    void onAction(String nick, String action) {
        ChatMessage chatMessage = new ChatMessage(new Date(), "*", nick + " " + action);
        addMessage(chatMessage);
    }

    void onNotice(String nick, String notice) {
        ChatMessage chatMessage = new ChatMessage(new Date(), "/" + nick + "/", notice);
        addMessage(chatMessage);
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        for (ChatMessageListener listener : this.listener) {
            System.out.println("informing listener about new message: " + listener);
            listener.onMessage(message);
        }
    }

    public String getLabel() {
        return this.name;
    }

    public void sendMessage(String message) {
        throw new UnsupportedOperationException("could not relay message");
    }
    
    public void sendInput(String input) {
        if (input.startsWith(COMMAND_ESCAPE_PREFIX)) {
            sendMessage(input.substring(1));
        } else if (input.startsWith(COMMAND_PREFIX)) {
            IrcCommandInterpreter.get().handle(this, input.substring(1));
        } else {
            sendMessage(input);
        }
    }

    public void close() {
        throw new UnsupportedOperationException("could not close conversation");
    }
}
