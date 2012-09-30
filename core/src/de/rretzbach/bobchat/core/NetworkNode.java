/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.core;

import de.rretzbach.bobchat.irc.Network;
import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author rretzbach
 */
public class NetworkNode extends AbstractNode {
    private final Network network;

    public NetworkNode(Network network) {
        super(Children.LEAF);
        this.network = network;
        setDisplayName(network.getHostname());
    }

    @Override
    public Image getIcon(int type) {
        return super.getIcon(type);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return super.getOpenedIcon(type);
    }
    
}
