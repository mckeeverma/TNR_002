package com.example.marc.tnr_002;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import android.telephony.SmsManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@SuppressWarnings("deprecation")
public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    public static final String SMS_BUNDLE = "pdus";
    String TAG = "marclog";
    SmsMessage smsMessage;
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Start of onReceive");
        Bundle intentExtras = intent.getExtras();
        String format = intentExtras.getString("format");
        String emailAddress;
        String line;
        String flashType;
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    smsMessage = SmsMessage.createFromPdu((byte[]) sms[i], format);
                } else {
                    smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                }
                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();
                Log.d(TAG, "SMS originating address (___phone#): " + address);
                Log.d(TAG, "smsBody...: " + smsBody);
                if (isSaveCommandWithEmailValid(smsBody)) {
                    Log.d(TAG, "SMS message: 'Save' command with email is in valid format.");
                    smsBody = smsBody.replaceAll("(?i)save ", "");
                    emailAddress = smsBody.replaceAll("\\s+", "");
                    Log.d(TAG, "Email address from Save command: " + emailAddress);
                    String fileName = "/storage/emulated/0/phonenumber/email.txt";
                    String content = emailAddress;
                    byte[] bytes = content.getBytes();
                    try {
                        FileOutputStream fos = new FileOutputStream(fileName);
                        // out.write(bytes[0]);    // write a single byte
                        // out.write(bytes,4,10);  // write sub sequence of the byte array
                        fos.write(bytes);       // write a byte sequence
                        fos.close();
                        Log.d(TAG, "Email Write file content: " + emailAddress);
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(address, null, emailAddress + " was saved. Text N for a picture without flash. Text F for a picture with flash.", null, null);
                        try {
                            Thread.sleep(10000);
                        } catch (Exception e) {
                            Log.d(TAG, "Error on thread sleep: " + e.getMessage());
                        }
                        continue;
                    } catch (Exception e) {
                        Log.d(TAG, "Email write file error: " + e.getMessage());
                        continue;
                    }
                } else if (smsBody.equalsIgnoreCase("n") ||
                           smsBody.equalsIgnoreCase("f")) {
                    flashType = smsBody;
                    Log.d(TAG, "SMS message is valid (n or f) flash or no flash requested.");
                    try {
                        String fileName = "/storage/emulated/0/phonenumber/email.txt";
                        FileInputStream fis = new FileInputStream(fileName);
                        InputStreamReader inputStreamReader = new InputStreamReader(fis);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        while ( (line = bufferedReader.readLine()) != null )
                        {
                            stringBuilder.append(line + System.getProperty("line.separator"));
                        }
                        fis.close();
                        line = stringBuilder.toString();
                        bufferedReader.close();
                        emailAddress = line;
                        Log.d(TAG, "Email Read file content: " + emailAddress);
                        if (flashType.equalsIgnoreCase("n")) {
                            smsBody = emailAddress;
                        } else {
                            smsBody = emailAddress + " flash";
                        }
                        Log.d(TAG, "message to be sent to picture taking program: " + smsBody);
                    } catch (Exception e) {
                        Log.d(TAG, "Email Read file error: " + e.getMessage());
                        continue;
                    }
                } else {
                    Log.d(TAG, "SMS message is invalid and will be ignored.");
                    continue;
                }
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_MAIN);
                intent2.addCategory(Intent.CATEGORY_LAUNCHER);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK +
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFilename = "IMG_" + timeStamp + ".jpg";
                //------------------------------------------------------------
                //--- the following is a way to send information to an activity
                Bundle extras = new Bundle();
                extras.putString("image_filename", imageFilename);
                extras.putString("send_email_to_this_address", smsBody);
                extras.putString("sms_originating_phone_num", address);
                intent2.putExtras(extras);
                //------------------------------------------------------------
                intent2.setComponent(new ComponentName("com.example.marc.tnr_002", "com.example.marc.tnr_002.Main2Activity"));
                Log.d(TAG, "Calling startActivity now");
                try {
                    context.startActivity(intent2);
                } catch (Exception e) {
                    Log.d(TAG, "Error on startActivity: " + e.getMessage());
                    e.printStackTrace();
                }
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(address, null, "Check " + emailAddress + " email for the picture", null, null);
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    Log.d(TAG, "Error on thread sleep: " + e.getMessage());
                }
            }
            //Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();
            //MainActivity inst = MainActivity.instance();
            //inst.updateList(smsMessageStr);
        }
    }
    public static boolean isSaveCommandWithEmailValid(String string1) {
        String expression = "^\\s*save\\s*[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}\\s*$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(string1);
        return matcher.matches();
    }
}