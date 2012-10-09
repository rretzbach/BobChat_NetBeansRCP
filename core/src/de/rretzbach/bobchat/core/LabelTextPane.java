/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 *
 * @author rretzbach
 */
class LabelTextPane extends JTextPane {

    public LabelTextPane() {
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setEditable(false);
        setBorder(new EmptyBorder(0,0,0,0));
    }
    
}
