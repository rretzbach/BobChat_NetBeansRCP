/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.core;

import de.rretzbach.bobchat.irc.message.ChatMessage;
import java.awt.Color;
import java.awt.Component;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTML;
import org.openide.util.Exceptions;

/**
 *
 * @author rretzbach
 */
public class ChatMessageRenderer {

    private DateFormat timestampFormat = DateFormat.getTimeInstance();
    private ChatMessage message;

    void setInput(ChatMessage message) {
        this.message = message;
    }

    Component renderTimestamp() {
        return new JLabel(timestampFormat.format(message.timestamp));
    }

    Component renderNick() {
        return new JLabel(message.nick);
    }

    // TODO Anzuzeigende Umlaute trotz encoding korrigieren
    Component renderMessage() {
        final LabelTextPane textpane = new LabelTextPane();
        final StyledDocument doc = textpane.getStyledDocument();

        Style def = StyleContext.getDefaultStyleContext().
                getStyle(StyleContext.DEFAULT_STYLE);
        Color green = new Color(0, 128, 0);
        Color navy = new Color(0, 0, 128);
        Color teal = new Color(0, 128, 128);
        Color brown = new Color(128, 0, 0);
        Color purple = new Color(128, 0, 128);
        Color white = new Color(255, 255, 255, 0);
        Color orange = new Color(200, 128, 0);


        Color[] colors = {white, Color.BLACK, navy, green, Color.RED, brown, purple, orange, Color.YELLOW, Color.GREEN, teal, Color.CYAN, Color.BLUE, Color.PINK, Color.GRAY, Color.LIGHT_GRAY};



        try {
            List<Runnable> styles = new ArrayList<Runnable>();
            final StringBuilder sb = new StringBuilder();
            String[] split = tokenize(message.message);
            if (message.message.contains("red")) {
                System.out.println(split);
            }

            for (String token : split) {
                if (token.startsWith("\u0003")) {
                    Integer fg = null, bg = null;
                    System.out.println("Token: " + token);
                    if (token.contains(",")) {
                        String[] split1 = token.substring(1).split(",");
                        fg = Integer.parseInt(split1[0]);
                        bg = Integer.parseInt(split1[1]);
                    } else {
                        fg = Integer.parseInt(token.substring(1));
                    }
                    System.out.println("fg " + fg);
                    System.out.println("bg " + bg);

                    String styleName = fg + "," + (bg == null ? "null" : bg);
                    Style newStyle = doc.getStyle(styleName);
                    if (newStyle == null) {
                        newStyle = doc.addStyle(styleName, def);
                        StyleConstants.setForeground(newStyle, colors[fg]);
                        if (bg != null) {
                            StyleConstants.setBackground(newStyle, colors[bg]);
                        }
                    }
                    final Style style = newStyle;
                    final int len = sb.length();
                    styles.add(new Runnable() {
                        @Override
                        public void run() {
                            doc.setCharacterAttributes(len, message.message.length(), style, false);
                        }
                    });

                } else {
                    sb.append(token);
                }
            }
            doc.insertString(0, sb.toString(), def);
            for (Runnable runnable : styles) {
                runnable.run();
            }

            // highlight urls
            String plaintext = doc.getText(0, doc.getLength());
            Pattern urlPattern = Pattern.compile("(((http|ftp|https):\\/\\/)|www\\.)?[\\w\\-_]+(\\.[\\w\\-_]{2,})*\\.[\\w\\-_]{2,4}(\\b|[?/#])\\S*");
            Matcher matcher = urlPattern.matcher(plaintext);
            while (matcher.find()) {
                int begin = matcher.start();
                String url = matcher.group();
                SimpleAttributeSet attrs = new SimpleAttributeSet();
                attrs.addAttribute(HTML.Attribute.HREF, url);
                doc.setCharacterAttributes(begin, url.length(), attrs, true);
            }




        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }

        return textpane;
    }

    private String[] tokenize(String input) {
        List<String> result = new ArrayList<String>();

        Matcher matcher = Pattern.compile("\u0003\\d\\d?(?:,\\d\\d?)?|[^\u0003]+").matcher(input);
        while (matcher.find()) {
            result.add(matcher.group());
        }

        System.out.println(result);

        String[] arr = new String[result.size()];
        return result.toArray(arr);
    }
}