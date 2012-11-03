/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.core;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import org.openide.util.Exceptions;

public class LinkController extends MouseAdapter
        implements MouseMotionListener {

    private Element lastElement;

    LinkController(LabelTextPane editor) {
    }

    @Override
    public void mouseMoved(MouseEvent ev) {
        JTextPane editor = (JTextPane) ev.getSource();
        if (!editor.isEditable()) {
            Point pt = new Point(ev.getX(), ev.getY());
            int pos = editor.viewToModel(pt);
            if (pos >= 0) {
                Document doc = editor.getDocument();
                if (doc instanceof DefaultStyledDocument) {
                    DefaultStyledDocument hdoc =
                            (DefaultStyledDocument) doc;
                    Element e = hdoc.getCharacterElement(pos);
                    AttributeSet a = e.getAttributes();
                    String href = (String) a.getAttribute(HTML.Attribute.HREF);

                    if (lastElement != null) {
                        SimpleAttributeSet ma = new SimpleAttributeSet(a);
                        StyleConstants.setUnderline(ma, false);
                        hdoc.setCharacterAttributes(lastElement.getStartOffset(), lastElement.getEndOffset() - lastElement.getStartOffset(), ma, false);
                    }

                    if (href != null) {
                        SimpleAttributeSet ma = new SimpleAttributeSet(a);
                        StyleConstants.setUnderline(ma, true);
                        hdoc.setCharacterAttributes(e.getStartOffset(), e.getEndOffset() - e.getStartOffset(), ma, false);

                        lastElement = e;
                        editor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else {
                        editor.setCursor(Cursor.getDefaultCursor());
                    }

                }
            }
        }
    }

    /**
     * Called for a mouse click event. If the component is read-only (ie a
     * browser) then the clicked event is used to drive an attempt to follow the
     * reference specified by a link.
     *
     * @param e the mouse event
     * @see MouseListener#mouseClicked
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        JTextPane editor = (JTextPane) e.getSource();

        if (!editor.isEditable()) {
            Point pt = new Point(e.getX(), e.getY());
            int pos = editor.viewToModel(pt);
            if (pos >= 0) {
                Document doc = editor.getDocument();
                if (doc instanceof DefaultStyledDocument) {
                    DefaultStyledDocument hdoc =
                            (DefaultStyledDocument) doc;
                    Element e2 = hdoc.getCharacterElement(pos);
                    AttributeSet a = e2.getAttributes();
                    String href = (String) a.getAttribute(HTML.Attribute.HREF);
                    if (href != null) {
                        try {
                            if (!href.startsWith("http://")) {
                                href = "http://" + href;
                            }
                            editor.fireHyperlinkUpdate(new HyperlinkEvent(this, HyperlinkEvent.EventType.ACTIVATED, new URL(href)));
                        } catch (MalformedURLException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }

            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (lastElement != null) {
            JTextPane editor = (JTextPane) e.getSource();
            Document doc = editor.getDocument();
            if (doc instanceof DefaultStyledDocument) {
                DefaultStyledDocument hdoc =
                        (DefaultStyledDocument) doc;
                SimpleAttributeSet ma = new SimpleAttributeSet();
                StyleConstants.setUnderline(ma, false);
                hdoc.setCharacterAttributes(lastElement.getStartOffset(), lastElement.getEndOffset() - lastElement.getStartOffset(), ma, false);
            }
        }
    }
}
