/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Insets;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import org.openide.util.Exceptions;

/**
 *
 * @author rretzbach
 */
class LabelTextPane extends JTextPane implements HyperlinkListener {

    public LabelTextPane() {
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setEditable(false);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        final LinkController linkController = new LinkController(this);

        addMouseListener(linkController);
        addMouseMotionListener(linkController);
        addHyperlinkListener(this);
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                Desktop.getDesktop().browse(e.getURL().toURI());
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } catch (URISyntaxException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
