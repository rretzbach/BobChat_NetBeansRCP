/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.core.util;

import de.rretzbach.bobchat.core.ChannelListTopComponent;
import de.rretzbach.bobchat.core.ChannelViewTopComponent;
import de.rretzbach.bobchat.core.NickListTopComponent;
import de.rretzbach.bobchat.irc.Conversation;
import de.rretzbach.bobchat.irc.Router;
import javax.swing.SwingUtilities;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author rretzbach
 */
public class WindowUtil {

    public static void executeChannelListAction(final ChannelListAction action) {
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                for (Mode mode : WindowManager.getDefault().getModes()) {
                    TopComponent[] openedTopComponents = WindowManager.getDefault().getOpenedTopComponents(mode);
                    for (TopComponent comp : openedTopComponents) {
                        if (comp instanceof ChannelListTopComponent) {
                            action.performAction((ChannelListTopComponent) comp);
                            return;
                        }
                    }
                }
            }
        });
    }

    public static void executeChannelViewAction(final ChannelViewAction action) {
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                for (Mode mode : WindowManager.getDefault().getModes()) {
                    TopComponent[] openedTopComponents = WindowManager.getDefault().getOpenedTopComponents(mode);
                    for (TopComponent comp : openedTopComponents) {
                        if (comp instanceof ChannelViewTopComponent) {
                            action.performAction((ChannelViewTopComponent) comp);
                        }
                    }
                }
            }
        });
    }

    public static void openChannelWindow(String hostname, String name) {
        ChannelViewTopComponent comp = findConversationWindow(hostname, name);
        if (comp == null) {
            comp = new ChannelViewTopComponent();
            comp.setHostname(hostname);
            comp.setName(name);
            comp.setType("channel");
            Mode myMode = WindowManager.getDefault().findMode("editor");
            myMode.dockInto(comp);
            comp.open();
            comp.registerComponent();
        }
        comp.requestActive();
    }

    public static void openConversationWindow(final String hostname, final Conversation conversation) {
        final ChannelViewTopComponent comp = findConversationWindow(hostname, conversation.getName());
        if (comp == null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println("open new window for " + hostname + " " + conversation.getName());
                    ChannelViewTopComponent comp = new ChannelViewTopComponent();
                    comp.setHostname(hostname);
                    comp.setConversation(conversation);
                    Mode myMode = WindowManager.getDefault().findMode("editor");
                    myMode.dockInto(comp);
                    comp.open();
                    comp.requestAttention(false);
                }
            });
        } else {
            comp.requestAttention(true);
        }
    }

    private static ChannelViewTopComponent findConversationWindow(String hostname, String name) {
        System.out.println("looking for " + hostname + " " + name);
        for (Mode mode : WindowManager.getDefault().getModes()) {
            TopComponent[] openedTopComponents = WindowManager.getDefault().getOpenedTopComponents(mode);
            for (TopComponent comp : openedTopComponents) {
                if (comp instanceof ChannelViewTopComponent) {
                    ChannelViewTopComponent channelView = (ChannelViewTopComponent) comp;
                    System.out.println("just found " + channelView.getHostname() + " " + channelView.getName());

                    if (channelView.getHostname().equals(hostname) && channelView.getName().equals(name)) {
                        return channelView;
                    }
                }
            }
        }
        return null;
    }
}
