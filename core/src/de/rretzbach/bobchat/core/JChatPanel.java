/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.core;

import de.rretzbach.bobchat.irc.ChatMessage;
import de.rretzbach.bobchat.irc.Channel;
import de.rretzbach.bobchat.irc.ChatMessageListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author rretzbach
 */
// TODO not possible to scroll up
public class JChatPanel extends javax.swing.JPanel implements ChatMessageListener, Scrollable {

    private static final Map<?, ?> DESKTOPHINTS = (Map<?, ?>) Toolkit
            .getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
    private ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
    private Channel channel;
    private Integer rulerPosition = null;
    private ChatMessageRenderer chatMessageRenderer = new ChatMessageRenderer();

    /**
     * Creates new form JChatPanel2
     */
    public JChatPanel() {
        super(true);
        initComponents();

        setLayout(new GridBagLayout());

        // add filler so that components don't show up in the middle
        {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 3;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            add(new JLabel(""), gbc);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 153, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    List<ChatMessage> getMessages() {
        return this.messages;
    }

    @Override
    public synchronized void onMessage(ChatMessage message) {
        messages.add(message);
        chatMessageRenderer.setInput(message);

        int col = 0;
        int row = this.messages.size() + 1;
        {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = col++;
            gbc.gridy = row;
            gbc.insets = new Insets(0, 5, 0, 3);
            gbc.anchor = GridBagConstraints.NORTHWEST;
            add(chatMessageRenderer.renderTimestamp(), gbc);
        }
        {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = col++;
            gbc.gridy = row;
            gbc.insets = new Insets(0, 15, 0, 3);
            gbc.anchor = GridBagConstraints.NORTHEAST;
            add(chatMessageRenderer.renderNick(), gbc);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col++;
        gbc.gridy = row;
        gbc.insets = new Insets(0, 3 + 1, 0, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(chatMessageRenderer.renderMessage(), gbc);


        validate();
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        switch (orientation) {
            case SwingConstants.VERTICAL:
                return visibleRect.height / 30;
            case SwingConstants.HORIZONTAL:
                return visibleRect.width / 30;
            default:
                throw new IllegalArgumentException("Invalid orientation: " + orientation);
        }
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        switch (orientation) {
            case SwingConstants.VERTICAL:
                return visibleRect.height;
            case SwingConstants.HORIZONTAL:
                return visibleRect.width;
            default:
                throw new IllegalArgumentException("Invalid orientation: " + orientation);
        }
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (rulerPosition != null) {
            drawRuler(g, rulerPosition);
        }
    }

    private void drawRuler(Graphics g, Integer rulerPosition) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.addRenderingHints(DESKTOPHINTS);

        g2d.setColor(Color.BLACK);
        g2d.drawLine(rulerPosition, 0, rulerPosition, getHeight());

        g2d.dispose();
    }

    @Override
    public void validate() {
        super.validate();

        // scroll to bottom
        JScrollBar verticalScrollBar = ((JScrollPane) JChatPanel.this.getParent().getParent()).getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());

        // store the rightmost element to be used in paintcomponent
        if (getComponentCount() > 0) {
            Component lastElement = getComponent(getComponentCount() - 1);
            JChatPanel.this.rulerPosition = lastElement.getX() - 3 - 1;
        }
    }
}
