/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.irc.command;

import de.rretzbach.bobchat.irc.Conversation;
import de.rretzbach.bobchat.irc.Network;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO implement this
 * @author rretzbach
 */
public class ActionCommand extends IrcCommand {
    private String action;

    public ActionCommand() {
        keyword = "me";
        pattern = Pattern.compile("\\A\\Q" + keyword + "\\E (.+)\\z");
    }

    @Override
    public boolean canHandle(String input) {
        if (input.matches(pattern.pattern())) {
            return true;
        }
        return false;
    }

    @Override
    public void handle(Conversation conversation, String input) {
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            return;
        }
        
        action = matcher.group(1);
        
        Network network = conversation.getNetwork();
        
        network.sendAnyAction(conversation.getName(), action);
    }
    
}
