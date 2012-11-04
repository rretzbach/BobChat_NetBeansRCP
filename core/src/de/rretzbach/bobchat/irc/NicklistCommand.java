/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.irc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO implement this
 * @author rretzbach
 */
public class NicklistCommand extends IrcCommand {
    private String action;

    public NicklistCommand() {
        keyword = "nicklist";
        pattern = Pattern.compile("\\A\\Q" + keyword + "\\E\\z");
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
        
        if (conversation instanceof Channel) {
            Channel channel = (Channel) conversation;
            List<User> users = channel.getUsers();
            Collections.sort(users, new Comparator<User>(){
                @Override
                public int compare(User o1, User o2) {
                    return o1.getNick().compareTo(o2.getNick());
                }
            });
            channel.addMessage(new ChatMessage(new Date(), "!", users.toString().replaceAll("[\\[\\]]+", "")));
        }
    }
    
}
