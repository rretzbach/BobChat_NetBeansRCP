/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.rretzbach.bobchat.core;

import de.rretzbach.bobchat.core.util.ChannelViewAction;
import de.rretzbach.bobchat.core.util.WindowUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.modules.ModuleInstall;
import org.openide.windows.WindowManager;
import org.openide.windows.WindowSystemEvent;
import org.openide.windows.WindowSystemListener;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        WindowManager.getDefault().addWindowSystemListener(new WindowSystemListener() {

            @Override
            public void beforeLoad(WindowSystemEvent wse) {
            }

            @Override
            public void afterLoad(WindowSystemEvent wse) {
                WindowUtil.executeChannelViewAction(new ChannelViewAction() {
                    @Override
                    public void performAction(ChannelViewTopComponent channelViewTopComponent) {
                        channelViewTopComponent.registerComponent();
                    }
                });
            }

            @Override
            public void beforeSave(WindowSystemEvent wse) {
            }

            @Override
            public void afterSave(WindowSystemEvent wse) {
            }
        });
    }
}
