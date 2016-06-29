package com.frankpi.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.EditText;

public class Log2 {

    public static StringBuilder log = new StringBuilder();

    public static void displayLog(final EditText tvLog, final String logtext) {
        log.append(logtext).append('\n');
        tvLog.scrollTo(0, tvLog.getLineCount());
        tvLog.post(new Runnable() {

            @Override
            public void run() {

                tvLog.setText(log.toString());
            }
        });
    }

    public static void displaystat(final EditText textview1,
                                   final String logtext) {
        textview1.post(new Runnable() {

            @Override
            public void run() {
                textview1.setText(logtext);
            }
        });
    }

    public static void clearLog(final EditText textview) {
        textview.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                log.append("Log:\n");
                log.delete(0, log.length());
                textview.setText("Log:\n");
            }
        });
    }

    static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMdd-HH:mm:ss");// 可以方便地修改日期格式
        return dateFormat.format(System.currentTimeMillis());
    }

    public static boolean saveLog(String json, String name) {
        File file = new File(Environment.getDataDirectory()
                + File.separator + name + File.separator + getTime() + ".txt");
        FileOutputStream fop;
        try {
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            fop = new FileOutputStream(file);
            // get the content in bytes
            byte[] contentInBytes = json.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 网络是否可用
     *
     * @param context:
     * @return :
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (NetworkInfo anInfo : info) {
                if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
}
