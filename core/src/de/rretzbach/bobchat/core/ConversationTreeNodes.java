/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.core;

import de.rretzbach.bobchat.irc.util.ConversationListener;
import de.rretzbach.bobchat.irc.Conversation;
import de.rretzbach.bobchat.irc.Network;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author rretzbach
 */
public class ConversationTreeNodes extends Children.Keys<Conversation> implements ConversationListener {
    private Network network;

    public ConversationTreeNodes(Network network) {
        this.network = network;
        network.addConversationListener(this);
    }
    
    @Override
    protected Node[] createNodes(Conversation conversation) {
        AbstractNode node = new AbstractNode(LEAF);
                node.setDisplayName(conversation.getName());
                return new Node[]{node};
    }

    @Override
    public void addedConversation(Conversation conversation) {
        setKeys(network.getConversations());
    }

    @Override
    public void removedConversation(Conversation conversation) {
        setKeys(network.getConversations());
    }
    
}
