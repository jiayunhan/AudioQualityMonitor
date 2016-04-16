package com.robustnet.jackjia.audioqualitymonitor;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;


public class AudioQualityMonitor extends ActionBarActivity {
    public static JSONObject result;
    public static JSONArray CallState_intents;
    public static JSONArray UIState_intents;
    public static int counter = 0;
    public static String sessionID = "UNKNOWN";
    public static String AppTriggeredCall = "UNKNOWN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_quality_monitor);
        result = new JSONObject();
        CallState_intents = new JSONArray();
        UIState_intents = new JSONArray();

    }
    class runCmd extends Thread{
        String cmd;
        boolean su;
        public runCmd(String Command, boolean usingroot){
            cmd=Command;
            su=usingroot;
        }

        public void run(){
            try {
                if (su){
                    Process sh = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(sh.getOutputStream());
                    os.writeBytes(cmd);
                    os.close();
                }
                else {
                    Runtime.getRuntime().exec(cmd);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public String GrepLogcat (String type, String Name){
        String Command = "logcat -v threadtime -b "+type+" -d|grep -v DEBUG | grep "+Name;
        String result = "";
        try{
            Process sh = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(sh.getOutputStream());
            os.writeBytes(Command);
            os.close();

            InputStreamReader reader = new InputStreamReader(sh.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(reader);
            int numRead;
            char [] buffer = new char[5000];
            StringBuffer commandOutput = new StringBuffer();
            while ((numRead = bufferedReader.read(buffer))>0){
                commandOutput.append(buffer,0,numRead);
            }
            bufferedReader.close();
            result = commandOutput.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_audio_quality_monitor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
