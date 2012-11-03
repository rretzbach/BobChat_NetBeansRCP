/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.irc;

/**
 *
 * @author rretzbach
 */
public interface NickChangeListener {
    void onNickChange(String oldNick, String login, String hostname, String newNick);
}
