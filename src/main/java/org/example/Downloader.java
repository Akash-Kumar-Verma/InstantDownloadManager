package org.example;

import module.FileInfo;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;


public class Downloader extends Thread {
    private  FileInfo file;
    private DownloadManager manager;

    public Downloader(FileInfo file,DownloadManager manager){
        this.file=file;
        this.manager=manager;
    }
    @Override
    public void run() {

        this.file.setStatus("DOWNLOADING");
        this.manager.updateUI(this.file);


        try {
            //download logic
            URL url = new URL(this.file.getUrl());
            URLConnection urlConnection = url.openConnection();
            int fileSize = urlConnection.getContentLength();
            System.out.println("File size: " + fileSize);

            int countByte = 0;
            double per = 0.0;
            double byteSum = 0.0;
            BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());

            FileOutputStream fos = new FileOutputStream(this.file.getPath());
            byte data[] = new byte[1024];
            while (true) {

                countByte = bufferedInputStream.read(data, 0, 1024);
                if (countByte == -1) {
                    break;
                }

                fos.write(data, 0, countByte);

                byteSum = byteSum + countByte;

                if (fileSize > 0) {
                    per = (byteSum / fileSize * 100);
                   // System.out.println(per);
                    this.file.setPer(per + "");
                    this.manager.updateUI(file);
                }

            }

            fos.close();
            bufferedInputStream.close();

            this.setName(100 + "");
            this.file.setStatus("DONE");
        } catch (IOException e) {
            this.file.setStatus("FAILED");
            System.out.println("Downloading error");
            e.printStackTrace();
        } finally {

        }
        this.manager.updateUI(this.file);
    }
}
