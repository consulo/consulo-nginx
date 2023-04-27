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

package net.ishchenko.idea.nginx.run;

import consulo.ui.ex.awt.internal.laf.MultiLineLabelUI;
import consulo.ui.ex.awtUnsafe.TargetAWT;
import consulo.nginx.icon.NginxIconGroup;
import net.ishchenko.idea.nginx.NginxBundle;
import net.ishchenko.idea.nginx.configurator.NginxServerDescriptor;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 29.07.2009
 * Time: 18:00:55
 */
public class NginxRunSettingsForm {

  JComboBox serverCombo;
  JTextField executableField;
  JTextField configurationField;
  JButton configureButton;
  JPanel panel;
  JTextField globalsField;
  JTextField pidField;
  JCheckBox showHttpLogCheckBox;
  JTextField httpLogPathField;
  JCheckBox showErrorLogCheckBox;
  JTextField errorLogPathField;
  private JLabel explanationLabel;

  public NginxRunSettingsForm(final NginxRunSettingsEditor.Mediator mediator) {

    mediator.form = this;

    serverCombo.setRenderer(new NginxServerComboboxRenderer());
    serverCombo.addActionListener(e -> mediator.onChooseDescriptor((NginxServerDescriptor) serverCombo.getSelectedItem()));

    configureButton.addActionListener(
        e -> mediator.showServerManagerDialog()
    );

    showHttpLogCheckBox.addActionListener(e -> mediator.onHttpLogCheckboxAction());

    showErrorLogCheckBox.addActionListener(e -> mediator.onErrorLogCheckboxAction());

  }

  public JPanel getPanel() {
    return panel;
  }

  private void createUIComponents() {
    explanationLabel = new JLabel(NginxBundle.message("run.layoutExplanation"));
    explanationLabel.setUI(new MultiLineLabelUI());
  }


  /**
   * @noinspection ALL
   */
  private void $$$loadButtonText$$$(AbstractButton component, String text) {
    StringBuffer result = new StringBuffer();
    boolean haveMnemonic = false;
    char mnemonic = '\0';
    int mnemonicIndex = -1;
    for (int i = 0; i < text.length(); i++) {
      if (text.charAt(i) == '&') {
        i++;
        if (i == text.length()) break;
        if (!haveMnemonic && text.charAt(i) != '&') {
          haveMnemonic = true;
          mnemonic = text.charAt(i);
          mnemonicIndex = result.length();
        }
      }
      result.append(text.charAt(i));
    }
    component.setText(result.toString());
    if (haveMnemonic) {
      component.setMnemonic(mnemonic);
      component.setDisplayedMnemonicIndex(mnemonicIndex);
    }
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return panel;
  }

  private static class NginxServerComboboxRenderer extends BasicComboBoxRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      BasicComboBoxRenderer result = (BasicComboBoxRenderer) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      if (value != null) {
        result.setIcon(TargetAWT.to(NginxIconGroup.nginx()));
        result.setText(((NginxServerDescriptor) value).getName());
      }
      return result;
    }
  }
}
