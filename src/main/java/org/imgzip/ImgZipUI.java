package org.imgzip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SettingPanel extends JPanel {
    private final JFrame parent;

    private final JTextField[] fields = new JTextField[2];
    private final JButton[] buttons = new JButton[2];

    SettingPanel(JFrame parent) {
        this.parent = parent;

        final JLabel[] labels = new JLabel[2];
        final String[] labelTexts = {"Image:", "Zip:"};

        setLayout(null);

        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel(labelTexts[i]);
            labels[i].setBounds(20, 20 + i * 50, 50, 30);

            fields[i] = new JTextField();
            fields[i].setEnabled(false);
            fields[i].setBounds(75, 25 + i * 50, 250, 25);

            buttons[i] = new JButton("..");
            buttons[i].setBounds(330, 25 + i * 50, 50, 25);
            buttons[i].addActionListener(new ButtonEventListener());

            add(labels[i]);
            add(fields[i]);
            add(buttons[i]);
        }
    }

    public String getTextValue(int i) {
        return fields[i].getText();
    }

    private class ButtonEventListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            FileDialog dialog = new FileDialog(parent, "열기", FileDialog.LOAD);
            dialog.setDirectory(".");
            dialog.setVisible(true);

            if (dialog.getFile() == null) {
                return;
            }

            String fileName = dialog.getDirectory() + dialog.getFile();
            for (int i = 0; i < buttons.length; i++) {
                if (e.getSource().equals(buttons[i])) {
                    fields[i].setText(fileName);
                }
            }
        }
    }
}

class ProgressPanel extends JPanel {
    private final JProgressBar progressBar;

    public void setValue(int value) {
        progressBar.setValue(value);
    }

    ProgressPanel() {
        setLayout(null);

        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBounds(5, 0, 475, 25);

        add(progressBar);
    }
}

class ExecutePanel extends JPanel {
    private final SettingPanel settingPanel;
    private final ProgressPanel progressPanel;

    ExecutePanel(SettingPanel settingPanel, ProgressPanel progressPanel) {
        this.settingPanel = settingPanel;
        this.progressPanel = progressPanel;

        setLayout(null);

        JButton button = new JButton("Merge");
        button.setBounds(0, 20, 80, 90);
        button.addActionListener(new ButtonEventListener());

        add(button);
    }

    private class ButtonEventListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MergeThread task = new MergeThread(settingPanel, progressPanel);
            task.start();
        }
    }
}

public class ImgZipUI extends JFrame {
    ImgZipUI() {
        setTitle("ImgZip");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container c = getContentPane();
        c.setLayout(null);

        SettingPanel settingPanel = new SettingPanel(this);
        ProgressPanel progressPanel = new ProgressPanel();
        ExecutePanel executePanel = new ExecutePanel(settingPanel, progressPanel);

        settingPanel.setBounds(0, 0, 395, 100);
        progressPanel.setBounds(0, 130, 500, 30);
        executePanel.setBounds(395, 0, 100, 130);
        c.add(settingPanel);
        c.add(progressPanel);
        c.add(executePanel);

        setSize(500, 200);
        setResizable(false);
        setVisible(true);
    }
}
