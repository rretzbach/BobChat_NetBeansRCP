/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.core;

import de.rretzbach.bobchat.irc.Identity;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rretzbach
 */
public class Config {

    private Map<String, Identity> identities = new HashMap<String, Identity>();
    private static Config instance = null;
    private Identity defaultIdentity = new Identity(){
        {
            nick = "Ostir";
            nick_2 = "Ostir2";
            password = null;
        }
    };

    public Config() {
        final Identity freenodeIdentity = new Identity();
        freenodeIdentity.nick = "rretzbach";
        freenodeIdentity.nick_2 = "rrretzbach";
        freenodeIdentity.password = "notreally";
        this.identities.put("irc.freenode.net", freenodeIdentity);

        final Identity izsmartIdentity = new Identity();
        izsmartIdentity.nick = "Ostwind";
        izsmartIdentity.nick_2 = "Nordwind";
        izsmartIdentity.password = "mypasswords";
        this.identities.put("irc.iz-smart.net", izsmartIdentity);
    }

    public static Config get() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public Identity getIdentity(String hostname) {
        final Identity get = identities.get(hostname);
        if (get == null) {
            return this.defaultIdentity;
        }
        return get;
    }
}
