package de.rretzbach.bobchat.irc;

import java.util.regex.Pattern;

/**
 *
 * @author rretzbach
 */
public abstract class IrcCommand {
    protected String keyword;
    protected Pattern pattern;
    
    public abstract boolean canHandle(String input);

    public abstract void handle(Conversation conversation, String input);
    
    protected String[] splitFirstSpace(String input) {
        int firstSpace = input.indexOf(" ");
        String one = input.substring(0, firstSpace - 1);
        String two = input.substring(firstSpace + 1);
        return new String[]{one, two};
    }

}
