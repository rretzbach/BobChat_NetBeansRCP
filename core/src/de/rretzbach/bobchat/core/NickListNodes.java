/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.core;

import de.rretzbach.bobchat.irc.Channel;
import de.rretzbach.bobchat.irc.Router;
import java.util.List;
import org.jibble.pircbot.User;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author rretzbach
 */
public class NickListNodes extends Children.Keys<User> {
    private final Channel channel;

    public NickListNodes(Channel channel) {
        this.channel = channel;
    }

    @Override
    protected Node[] createNodes(User key) {
        List<User> users = channel.getUsers();
        return users.toArray(new Node[]{});
    }
    
}
