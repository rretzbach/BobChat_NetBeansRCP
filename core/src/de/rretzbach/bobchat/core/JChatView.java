/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.core;

import de.rretzbach.bobchat.irc.message.ChatMessage;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.LineBreakMeasurer;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 *
 * @author rretzbach
 */
public class JChatView extends JPanel {

    private static final long serialVersionUID = -2987983179969517781L;
    private static final int SELECTION_MODE_NONE = -1;
    private static final int SELECTION_MODE_MESSAGE = 0;
    private static final int SELECTION_MODE_NICK = 1;
    private static final int SELECTION_MODE_ALL = 2;
    private static final Map<?, ?> DESKTOPHINTS = (Map<?, ?>) Toolkit
            .getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
    private int verticalRulerPos = 200;
    protected SelectionMark selectionBegin;
    protected SelectionMark selectionEnd;
    protected int gapH = 3;
    protected int margin = 3;
    protected int grabThreshold = 1;
    public final List<ChatMessage> messages = new ArrayList<ChatMessage>();
    private LinkedHashMap<ChatMessage, ChatMessageBounds> messagesBounds = new LinkedHashMap<ChatMessage, ChatMessageBounds>();

    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    @Override
    public Dimension getMinimumSize() {
        int maxY = getHeight();

        Rectangle lastMessageBounds = getLastMessageBound();
        if (lastMessageBounds != null) {
            maxY = lastMessageBounds.y + lastMessageBounds.height;
        }
        return new Dimension(verticalRulerPos, maxY);
    }

    public JChatView() {
        super(true);
        //setPreferredSize(new Dimension(640, 480));
        MouseAdapter l = new MouseAdapter() {
            protected static final int STATE_NORMAL = 0;
            protected static final int STATE_MOVING_VRULER = 1;
            protected static final int STATE_SELECTING_TEXT = 2;
            protected int state = STATE_NORMAL;

            @Override
            public void mousePressed(MouseEvent e) {
                if (isOverVerticalRuler(e)) {
                    state = STATE_MOVING_VRULER;
                } else {
                    selectionBegin = computeMark(e.getPoint());
                    selectionEnd = selectionBegin;
                    state = STATE_SELECTING_TEXT;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (state != STATE_NORMAL) {
                    state = STATE_NORMAL;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (state == STATE_MOVING_VRULER) {
                    verticalRulerPos = e.getX();
                }

                if (state == STATE_SELECTING_TEXT) {
                    selectionEnd = computeMark(e.getPoint());
                }

                JChatView.this.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (state == STATE_MOVING_VRULER || isOverVerticalRuler(e)) {
                    setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
                } else {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }

            private boolean isOverVerticalRuler(MouseEvent e) {
                return e.getPoint().x >= verticalRulerPos - grabThreshold
                        && e.getPoint().x <= verticalRulerPos + grabThreshold;
            }
        };
        addMouseListener(l);
        addMouseMotionListener(l);
    }

    protected SelectionMark computeMark(Point selectionPoint) {
        SelectionMark mark = new SelectionMark();
        mark.x = selectionPoint.x;
        mark.y = selectionPoint.y;

        ChatMessageBounds messageBounds = null;
        ChatMessage message = null;
        for (ChatMessage msg : this.messages) {
            messageBounds = messagesBounds.get(msg);
            if (messageBounds != null && messageBounds.bounds.contains(selectionPoint)) {
                message = msg;
                mark.msgIndex = this.messages.indexOf(msg);
                break;
            }
        }

        if (mark.msgIndex == null) {
            int lastIndex = this.messages.size() - 1;
            message = this.messages.get(lastIndex);
            ChatMessageBounds lastMessageBounds = this.messagesBounds
                    .get(message);
            if (lastMessageBounds != null
                    && selectionPoint.y > lastMessageBounds.bounds.y
                    + lastMessageBounds.bounds.height) {
                messageBounds = lastMessageBounds;
                mark.msgIndex = lastIndex;
            }
        }

        if (mark.msgIndex == null) {

            return null;
        }

        ChatMessageLineBounds markedLineBounds = null;
        for (ChatMessageLineBounds lineBounds : messageBounds.linesBounds) {
            if (new Rectangle(messageBounds.bounds.x, lineBounds.bounds.y,
                    messageBounds.bounds.width, lineBounds.bounds.height)
                    .contains(selectionPoint)) {
                markedLineBounds = lineBounds;
                break;
            }
        }

        if (markedLineBounds == null) {
            int lastIndex = messageBounds.linesBounds.size() - 1;
            ChatMessageLineBounds lastLineBounds = messageBounds.linesBounds
                    .get(lastIndex);
            if (selectionPoint.y > lastLineBounds.bounds.y
                    + lastLineBounds.bounds.height) {
                markedLineBounds = lastLineBounds;
                mark.textPos = markedLineBounds.start + markedLineBounds.length;
            }
        }

        if (markedLineBounds == null) {
            return null;
        }

        if (mark.textPos == null) {

            String substr = message.message.substring(markedLineBounds.start,
                    markedLineBounds.start + markedLineBounds.length);

            Graphics2D g2d = (Graphics2D) getGraphics();
            FontMetrics fontMetrics = g2d.getFontMetrics();

            for (int i = 0; i <= markedLineBounds.length; i++) {
                if (selectionPoint.x <= markedLineBounds.bounds.x
                        + fontMetrics.stringWidth(substr.substring(0,
                        Math.min(i + 1, markedLineBounds.length)))
                        || i == markedLineBounds.length) {
                    mark.textPos = markedLineBounds.start + i;
                    break;
                }
            }
        }

        return mark;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.addRenderingHints(DESKTOPHINTS);

        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(Color.BLACK);
        g2d.drawLine(verticalRulerPos, 0, verticalRulerPos, getHeight());

        int index = 0;
        int offsetY = 10;
        for (ChatMessage msg : messages) {
            int height = drawMessage(g2d, index++, margin, margin + offsetY);
            offsetY += height + gapH;
        }

        g2d.dispose();

        revalidate();
    }

    private int drawMessage(Graphics2D g2d, int messageIndex, int offsetX,
            int offsetY) {
        {
            ChatMessage msg = this.messages.get(messageIndex);

            ChatMessageBounds messageBounds = new ChatMessageBounds();
            this.messagesBounds.put(msg, messageBounds);

            // timestamp
            drawString(new SimpleDateFormat("HH:mm:ss").format(msg.timestamp),
                    offsetX, offsetY, g2d, SELECTION_MODE_ALL, messageIndex);

            // nick
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int w = fontMetrics.stringWidth(msg.nick);
            int h = fontMetrics.getHeight();

            int x = verticalRulerPos - w - gapH;
            int y = offsetY;

            drawString(msg.nick, x, y, g2d, SELECTION_MODE_NICK, messageIndex);

            // multiline message
            AttributedString attributedString = new AttributedString(
                    msg.message);
            AttributedCharacterIterator paragraph = attributedString
                    .getIterator();
            LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph,
                    g2d.getFontRenderContext());
            lineMeasurer.setPosition(paragraph.getBeginIndex());
            int msgX = verticalRulerPos + gapH + offsetX;
            int msgY = y;

            messageBounds.bounds.x = offsetX;
            messageBounds.bounds.y = msgY - fontMetrics.getAscent();

            int breakWidth = getWidth() - msgX - margin;
            messageBounds.bounds.width = breakWidth + this.verticalRulerPos
                    + gapH;

            int i = 0;
            while (lineMeasurer.getPosition() < paragraph.getEndIndex()) {
                lineMeasurer.nextLayout(breakWidth);

                ChatMessageLineBounds messageLineBounds = new ChatMessageLineBounds();
                messageBounds.linesBounds.add(messageLineBounds);
                messageLineBounds.bounds.x = msgX;
                messageLineBounds.bounds.y = msgY - fontMetrics.getAscent();
                messageLineBounds.bounds.width = breakWidth;
                messageLineBounds.bounds.height = h;
                messageLineBounds.start = i;
                messageLineBounds.length = lineMeasurer.getPosition() - i;
                // g2d.setColor(Color.GREEN);
                // g2d.drawRect(messageLineBounds.bounds.x,
                // messageLineBounds.bounds.y,
                // messageLineBounds.bounds.width,
                // messageLineBounds.bounds.height);

                drawString(msg.message, i, lineMeasurer.getPosition(), msgX,
                        msgY, g2d, SELECTION_MODE_MESSAGE, messageIndex);
                i = lineMeasurer.getPosition();

                msgY += h;
            }
            ChatMessageLineBounds messageLineBounds = new ChatMessageLineBounds();
            messageBounds.linesBounds.add(messageLineBounds);
            messageLineBounds.bounds.x = msgX;
            messageLineBounds.bounds.y = msgY - h - fontMetrics.getAscent();
            messageLineBounds.bounds.width = breakWidth;
            messageLineBounds.bounds.height = h;
            messageLineBounds.start = i;
            messageLineBounds.length = lineMeasurer.getPosition() - i;

            // g2d.setColor(Color.RED);
            // g2d.drawRect(messageLineBounds.bounds.x,
            // messageLineBounds.bounds.y, messageLineBounds.bounds.width,
            // messageLineBounds.bounds.height);

            drawString(msg.message, i, lineMeasurer.getPosition(), msgX, msgY,
                    g2d, SELECTION_MODE_MESSAGE, messageIndex);

            int multilineHeight = msgY - y;

            messageBounds.bounds.height = multilineHeight;

            // g2d.setColor(Color.BLUE);
            // g2d.drawRect(messageBounds.bounds.x, messageBounds.bounds.y,
            // messageBounds.bounds.width, messageBounds.bounds.height);

            return multilineHeight;
        }
    }

    private void drawString(String str, int x, int y, Graphics2D g2d,
            int selectionMode, int messageIndex) {
        drawString(str, 0, str.length(), x, y, g2d, selectionMode, messageIndex);
    }

    private void drawString(String str, int begin, int end, int x, int y,
            Graphics2D g2d, int selectionMode, int messageIndex) {

        String substr = str.substring(begin, end);

        // selection
        if (selectionBegin != null && selectionEnd != null) {
            SelectionMark start = selectionBegin;
            SelectionMark stop = selectionEnd;
            if (stop.compareTo(start) < 0) {
                SelectionMark tmp = start;
                start = stop;
                stop = tmp;
            }

            if (selectionMode == SELECTION_MODE_MESSAGE) {

                // full selection
                if ((messageIndex > start.msgIndex && messageIndex < stop.msgIndex)
                        || (messageIndex == start.msgIndex
                        && messageIndex < stop.msgIndex && start.textPos <= begin)
                        || (messageIndex == stop.msgIndex
                        && messageIndex > start.msgIndex && stop.textPos > end)
                        || (messageIndex == stop.msgIndex
                        && messageIndex == start.msgIndex && start.textPos <= begin && stop.textPos > end)) {
                    drawSelectedString(substr, x, y, g2d);

                } else // partial selection start and stop on same message
                if (messageIndex == start.msgIndex
                        && messageIndex == stop.msgIndex
                        && start.textPos >= begin && start.textPos <= end
                        && stop.textPos >= begin && stop.textPos <= end) {
                    String firstPart = str.substring(begin, start.textPos);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(firstPart, x, y);

                    int secondPartOffset = g2d.getFontMetrics().stringWidth(
                            firstPart);

                    String secondPart = str.substring(start.textPos,
                            stop.textPos);
                    drawSelectedString(secondPart, x + secondPartOffset, y, g2d);

                    int thirdPartOffset = g2d.getFontMetrics().stringWidth(
                            secondPart);

                    g2d.setColor(Color.BLACK);
                    g2d.drawString(str.substring(stop.textPos, end), x
                            + secondPartOffset + thirdPartOffset, y);
                } else // partial selection start
                if (messageIndex == start.msgIndex && start.textPos >= begin
                        && start.textPos <= end) {
                    String firstPart = str.substring(begin, start.textPos);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(firstPart, x, y);

                    int secondPartOffset = g2d.getFontMetrics().stringWidth(
                            firstPart);

                    drawSelectedString(str.substring(start.textPos, end), x
                            + secondPartOffset, y, g2d);
                } else // partial selection stop
                if (messageIndex == stop.msgIndex && stop.textPos >= begin
                        && stop.textPos <= end) {
                    String firstPart = str.substring(begin, stop.textPos);
                    drawSelectedString(firstPart, x, y, g2d);

                    int secondPartOffset = g2d.getFontMetrics().stringWidth(
                            firstPart);

                    g2d.setColor(Color.BLACK);
                    g2d.drawString(str.substring(stop.textPos, end), x
                            + secondPartOffset, y);
                } else {

                    // no selection
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(substr, x, y);
                }
            } else {
                if (currentSelectionMode(g2d) >= selectionMode
                        && messageIndex >= start.msgIndex
                        && messageIndex <= stop.msgIndex) {

                    drawSelectedString(substr, x, y, g2d);

                } else {
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(substr, x, y);
                }
            }
        } else {
            g2d.setColor(Color.BLACK);
            g2d.drawString(substr, x, y);
        }
    }

    private void drawSelectedString(String substr, int x, int y, Graphics2D g2d) {
        Color selectionBackground = UIManager.getDefaults().getColor(
                "nimbusSelectionBackground");
        if (selectionBackground == null) {
            selectionBackground = new Color(222, 225, 229);
        }
        g2d.setColor(selectionBackground);
        int w = g2d.getFontMetrics().stringWidth(substr);
        int h = g2d.getFontMetrics().getHeight();
        g2d.fillRect(x, y - g2d.getFontMetrics().getAscent(), w, h);
        g2d.setColor(Color.WHITE);
        g2d.drawString(substr, x, y);
    }

    private int currentSelectionMode(Graphics2D g2d) {
        if (selectionEnd != null) {
            int leftEdge = selectionBegin.x < selectionEnd.x ? selectionBegin.x
                    : selectionEnd.x;

            if (leftEdge < margin + g2d.getFontMetrics().getMaxAdvance() / 2) {
                return SELECTION_MODE_ALL;
            } else if (leftEdge < verticalRulerPos) {
                return SELECTION_MODE_NICK;
            } else {
                return SELECTION_MODE_MESSAGE;
            }

        }
        return SELECTION_MODE_NONE;
    }

    void addMessage(ChatMessage chatMessage) {
        messages.add(chatMessage);
        repaint();
    }

    Rectangle getLastMessageBound() {
        if (this.messages.isEmpty()) {
            return null;
        }
        
        int lastIndex = this.messages.size() - 1;
        ChatMessage message = this.messages.get(lastIndex);
        final ChatMessageBounds messageBound = this.messagesBounds
                                                .get(message);
        if (messageBound == null) {
            return null;
        }
        
        return messageBound.bounds;
    }

    public static class SelectionMark implements Comparable<SelectionMark> {

        public Integer textPos;
        public Integer msgIndex;
        public int x;
        public int y;

        @Override
        public int compareTo(SelectionMark o) {
            int messageLevel = new Integer(this.msgIndex).compareTo(o.msgIndex);
            if (messageLevel != 0) {
                return messageLevel;
            }

            int textPosLevel = new Integer(this.textPos).compareTo(o.textPos);
            if (textPosLevel != 0) {
                return textPosLevel;
            }

            return 0;
        }
    }

    public static class ChatMessageBounds {

        public Rectangle bounds = new Rectangle();
        public List<ChatMessageLineBounds> linesBounds = new ArrayList<ChatMessageLineBounds>();
    }

    public static class ChatMessageLineBounds {

        public Rectangle bounds = new Rectangle();
        public int start;
        public int length;
    }
}
