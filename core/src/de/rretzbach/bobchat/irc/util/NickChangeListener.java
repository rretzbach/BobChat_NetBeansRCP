/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.irc.util;

/**
 *
 * @author rretzbach
 */
public interface NickChangeListener {
    void onNickChange(String oldNick, String newNick);
}
