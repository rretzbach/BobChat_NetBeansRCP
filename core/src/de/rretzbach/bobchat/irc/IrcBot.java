package de.rretzbach.bobchat.irc;

import de.rretzbach.bobchat.irc.util.IrcConnection;
import de.rretzbach.bobchat.irc.util.IrcConnectionListener;
import de.rretzbach.bobchat.irc.util.IrcMessageListener;

/**
 *
 * @author rretzbach
 */
public interface IrcBot extends IrcBotDelegate {

    public void setDelegate(IrcBotDelegate delegate);
}
