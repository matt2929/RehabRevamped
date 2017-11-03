package com.example.matthew.rehabrevamped.Utilities;

import android.content.Context;
import android.os.Environment;

import com.example.matthew.rehabrevamped.Activities.WelcomeScreen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 * Created by matt2929 on 11/3/17.
 */

public class JerkBack {
    File _myFile;
    FileOutputStream outputStream;
    FileInputStream inputStream;
    Context _context;
    public static String _fileName;
    File _file;
    int count = 0;
    String _name = "";
    String _title = _name + "\nTime,AccX,AccY,AccZ,AccT,GyroX,GyroY,GyroZ";
    SampleAverage sampleAverage = new SampleAverage();

    public JerkBack(Context context, String name) {
        _name = name;
        _context = context;

        Calendar cal = Calendar.getInstance();

        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int milli = cal.get(Calendar.MILLISECOND);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        _fileName = WelcomeScreen.Username + "_Jerk_" + name + "_" + (month + 1) + "-" + day + "-" + year + "_[" + hour + "h~" + minute + "m~" + second + "s].csv";
    }

    public void saveData(int jerk, boolean leftHand) {
        try {
            File fileP = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "RehabRevamped");
            if (!fileP.exists()) {
                fileP.mkdir();
            }
            String string = "";
            String hand = "";
            if (leftHand) {
                hand = "Left";
            } else {
                hand = "right";
            }
            string += "" + jerk + "," + hand;

            outputStream = _context.openFileOutput(_fileName, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
            File file = new File(fileP, _fileName);
            PrintWriter writer;

            if (!file.exists()) {
                try {
                    file.createNewFile();
                    writer = new PrintWriter(new FileWriter(file, true));
                    writer.print(string);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                file.delete();
                try {
                    file.createNewFile();
                    writer = new PrintWriter(new FileWriter(file, true));
                    writer.print(string);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
