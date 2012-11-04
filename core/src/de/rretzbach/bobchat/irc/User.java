/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.irc;

/**
 *
 * @author rretzbach
 */
public class User {
    private String prefix;
    private String nick;

    public User(String prefix, String nick) {
        this.prefix = prefix;
        this.nick = nick;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if ((this.nick == null) ? (other.nick != null) : !this.nick.toLowerCase().equals(other.nick.toLowerCase())) {
            return false;
        }
        return true;
    }   
    
}
