/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.core;

import de.rretzbach.bobchat.irc.Network;

/**
 *
 * @author rretzbach
 */
public interface NetworkListener {
    void addedNetwork(Network network);
    void removedNetwork(Network network);
    
}
