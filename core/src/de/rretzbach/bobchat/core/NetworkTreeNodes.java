/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.core;

import de.rretzbach.bobchat.irc.util.NetworkListener;
import de.rretzbach.bobchat.irc.Conversation;
import de.rretzbach.bobchat.irc.Network;
import de.rretzbach.bobchat.irc.Router;
import de.rretzbach.bobchat.core.util.WindowUtil;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author rretzbach
 */
public class NetworkTreeNodes extends Children.Keys<Network> implements NetworkListener {

    public NetworkTreeNodes() {
        Router.get().addNetworkListener(this);
    }

    
    
    @Override
    protected void addNotify() {
        // 
        // List<Network> networks = Router.get().getNetworks();
        // List<NetworkNode> networkNodes = new ArrayList<NetworkNode>();
        // for (Network network : networks) {
        //     networkNodes.ad
        // }
        // setKeys(Arrays.asList("a", "b"));
    }

    @Override
    protected Node[] createNodes(final Network network) {
        AbstractNode node = new AbstractNode(new ConversationTreeNodes(network)) {
            @Override
            public Action[] getActions(boolean context) {
                Action joinChannel = new AbstractAction("Join channel") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String input = JOptionPane.showInputDialog("Please enter the name of the channel");
                        WindowUtil.openChannelWindow(network.getHostname(), input);
                    }
                };
                Action reconnectNetwork = new AbstractAction("Reconnect") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        network.reconnect();
                    }
                };
                return new Action[]{joinChannel, reconnectNetwork};
            }
        
        };
        node.setDisplayName(network.getHostname());
        return new Node[]{node};
    }

    @Override
    public void addedNetwork(Network network) {
        setKeys(Router.get().getNetworks());
    }

    @Override
    public void removedNetwork(Network network) {
        setKeys(Router.get().getNetworks());
    }
    
}
