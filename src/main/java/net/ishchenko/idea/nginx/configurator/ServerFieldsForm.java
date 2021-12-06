/*
 * Copyright 2009 Max Ishchenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ishchenko.idea.nginx.configurator;

import com.intellij.openapi.ui.ComponentWithBrowseButton;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 28.07.2009
 * Time: 3:41:46
 */
public class ServerFieldsForm {
    private JPanel panel;
    private JTextField nameField;
    private ComponentWithBrowseButton<JTextField> executableField;
    private ComponentWithBrowseButton<JTextField> configurationField;
    private ComponentWithBrowseButton<JTextField> pidField;
    private JTextField globalsField;


    public ServerFieldsForm(final NginxConfigurationPanel.TrickyMediator mediator) {
        KeyAdapter syncListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                SwingUtilities.invokeLater(mediator::sync);
            }
        };

        nameField.addKeyListener(syncListener);
        executableField.getChildComponent().addKeyListener(syncListener);
        configurationField.getChildComponent().addKeyListener(syncListener);
        pidField.getChildComponent().addKeyListener(syncListener);
        globalsField.addKeyListener(syncListener);

        executableField.addActionListener(e -> SwingUtilities.invokeLater(mediator::chooseExecutableClicked));

        configurationField.addActionListener(e -> SwingUtilities.invokeLater(mediator::chooseConfigurationClicked));

        pidField.addActionListener(e -> SwingUtilities.invokeLater(mediator::choosePidClicked));

        mediator.nameField = nameField;
        mediator.executableField = executableField.getChildComponent();
        mediator.configField = configurationField.getChildComponent();
        mediator.pidField = pidField.getChildComponent();
        mediator.globalsField = globalsField;

    }


    public JPanel getPanel() {
        return panel;
    }

    private void createUIComponents() {
        executableField = new ComponentWithBrowseButton<>(new JTextField(), null);
        configurationField = new ComponentWithBrowseButton<>(new JTextField(), null);
        pidField = new ComponentWithBrowseButton<>(new JTextField(), null);
    }
}
