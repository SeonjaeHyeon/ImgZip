package org.imgzip;

import java.io.*;

public class MergeThread extends Thread {
    private final SettingPanel settingPanel;
    private final ProgressPanel progressPanel;

    MergeThread(SettingPanel settingPanel, ProgressPanel progressPanel) {
        this.settingPanel = settingPanel;
        this.progressPanel = progressPanel;
    }

    @Override
    public void run() {
        File imgFile = new File(settingPanel.getTextValue(0));
        File zipFile = new File(settingPanel.getTextValue(1));

        File newFile = new File("new_" + imgFile.getName());

        long imgSize = imgFile.length();
        long zipSize = zipFile.length();
        long totalSize = imgSize + zipSize;

        long size = 0;

        try {
            BufferedInputStream buffIn = new BufferedInputStream(new FileInputStream(imgFile));
            BufferedInputStream buffIn2 = new BufferedInputStream(new FileInputStream(zipFile));

            BufferedOutputStream buffOut = new BufferedOutputStream(new FileOutputStream(newFile));

            int c;
            while ((c = buffIn.read()) != -1) {
                buffOut.write(c);
                progressPanel.setValue((int) (++size * 100 / totalSize));
            }
            buffOut.flush();

            while ((c = buffIn2.read()) != -1) {
                buffOut.write(c);
                progressPanel.setValue((int) (++size * 100 / totalSize));
            }
            buffOut.flush();

            buffIn.close();
            buffIn2.close();
            buffOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
