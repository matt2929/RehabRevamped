package com.example.matthew.rehabrevamped.Utilities;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 * Created by Matthew on 7/14/2016.
 */
public class SaveDataInTextFile {
    File _myFile;
    FileOutputStream outputStream;
    FileInputStream inputStream;
    Context _context;
    public static String _fileName;
    File _file;
    int count = 0;
    String _name="";
    String _title=_name+"\nTime,AccX,AccY,AccZ,AccT,GyroX,GyroY,GyroZ";
SampleAverage sampleAverage = new SampleAverage();
    public SaveDataInTextFile(Context context, String name, String fileType) {
        _name=name;
        _context = context;

        Calendar cal = Calendar.getInstance();

        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int milli = cal.get(Calendar.MILLISECOND);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        _fileName = "RehabInfo_"+name+"_"+(month + 1)+"-"+day+"-"+year+"_["+hour+"h~"+minute+"m~"+second+"s]."+fileType;
    }
    public SaveDataInTextFile(Context context, String name, String fileType,String title) {
        _name=name;
        _context = context;
        _title=_name+"\n"+title;

        Calendar cal = Calendar.getInstance();

        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int milli = cal.get(Calendar.MILLISECOND);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        _fileName = "RehabInfo_"+name+"_"+(month + 1)+"-"+day+"-"+year+"_["+hour+"h~"+minute+"m~"+second+"s]."+fileType;
    }
    public void saveData(String saveString,File fileParent) {
            try {

                String string = "";
                string += load();
                string += saveString;

                outputStream = _context.openFileOutput(_fileName, Context.MODE_WORLD_READABLE);
                outputStream.write(string.getBytes());
                outputStream.close();
                File file = new File(fileParent, _fileName);
                PrintWriter writer;

                if (!file.exists()) {
                    try {
                        file.createNewFile();
                        writer = new PrintWriter(new FileWriter(file, true));
                        int last = 0;
                        int count = 0;
                        writer.print(_title);
                        writer.append('\n');
                        for (int i = 0; i < string.length(); i++) {
                            if (string.charAt(i) == ';') {
                                writer.print(string.substring(last + 1, i));
                                writer.append('\n');
                                last = i;
                            }
                        }
                        writer.print(string.substring(last, string.length() - 1));
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    file.delete();
                    try {
                        file.createNewFile();
                        writer = new PrintWriter(new FileWriter(file, true));
                        writer.print(_name+"\nTime,AccX,AccY,AccZ,AccT");
                        writer.append('\n');
                        int last = 0;

                        for (int i = 0; i < string.length(); i++) {
                            if (string.charAt(i) == ';') {
                                writer.print(string.substring(last + 1, i));
                                writer.append('\n');
                                last = i;
                            }
                        }
                        writer.print(string.substring(last, string.length() - 1));
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public String load() {
        String line1 = "";
        try {
            inputStream = _context.openFileInput(_fileName);
            if (inputStream != null) {
                InputStreamReader inputreader = new InputStreamReader(inputStream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line = "";
                try {
                    while ((line = buffreader.readLine()) != null) {
                        line1 += line;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

            String error = "";
            error = e.getMessage();
        }
        return line1;
    }
    public void addData(String data,File fileParent) {
        File file = new File(fileParent, _fileName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

            try {
                file.canWrite();
                outputStream = _context.openFileOutput(_fileName,Context.MODE_WORLD_READABLE);
                outputStream.write(data.getBytes());
                outputStream.close();
                PrintWriter writer = new PrintWriter(new FileWriter(file, true));
                writer.append('\n');
                int last = 0;
                for (int i = 0; i < data.length(); i++) {
                    if (data.charAt(i) == ';') {
                        writer.print(data.substring(last + 1, i));
                        writer.append('\n');
                        last = i;
                    }
                }
                writer.print(data.substring(last, data.length() - 1));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}