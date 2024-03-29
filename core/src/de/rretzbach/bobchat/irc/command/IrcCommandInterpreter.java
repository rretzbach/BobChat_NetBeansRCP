/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.irc.command;

import de.rretzbach.bobchat.irc.Conversation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rretzbach
 */
public class IrcCommandInterpreter {
    private static IrcCommandInterpreter instance;
    private List<IrcCommand> commands = new ArrayList<IrcCommand>();
    
    private IrcCommandInterpreter() {
        commands.add(new MsgCommand());
        commands.add(new NickChangeCommand());
        commands.add(new ActionCommand());
        commands.add(new NicklistCommand());
    }
    
    public static IrcCommandInterpreter get() {
        if (instance == null) {
            instance = new IrcCommandInterpreter();
        }
        return instance;
    }

    public void handle(Conversation conversation, String input) {
        for (IrcCommand ircCommand : commands) {
            if (ircCommand.canHandle(input)) {
                ircCommand.handle(conversation, input);
                break;
            }
        }
    }

}
