/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.core;

import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 *
 * @author rretzbach
 */
public class LabelTextArea extends JTextArea {

    public LabelTextArea() {
        super();
        init();
    }

    public LabelTextArea(String text) {
        super(text);
        init();
    }

    @Override
    public void setBorder(Border border) {
        // disabled
    }

    private void init() {
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
    }
    
    
}
