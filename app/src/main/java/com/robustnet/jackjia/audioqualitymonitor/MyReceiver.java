package com.robustnet.jackjia.audioqualitymonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import android.util.JsonWriter;
/**
 * Created by jackjia on 3/18/15.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        if(intent.getAction().equals("demo.diagandroid.phone.detailedCallState")){
            System.out.println(intent.getAction());


                JSONObject object = new JSONObject();
                try {
                    object.put("Timestamp", intent.getStringExtra("Timestamp"));
                    object.put("CallState", intent.getStringExtra("CallState"));
                    object.put("VoiceAccessNetworkStateType", intent.getStringExtra("VoiceAccessNetworkStateType"));
                    object.put("VoiceAccessNetworkStateSignal", intent.getStringExtra("VoiceAccessNetworkStateSignal"));
                    object.put("VoiceAccessNetworkStateBand", intent.getStringExtra("VoiceAccessNetworkStateBand"));
                    AudioQualityMonitor.CallState_intents.put(object.toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                            /*
*/
                //Log.d("DEBUG",result.toString());
            /*
            System.out.println("Timestamp:" +intent.getStringExtra("Timestamp"));
            System.out.println("CallState:" +intent.getStringExtra("CallState"));
            System.out.println("CallNumber:" +intent.getStringExtra("CallNumber"));
            System.out.println("CallID:" +intent.getStringExtra("CallID"));
            System.out.println("VoiceAccessNetworkStateType:" +intent.getStringExtra("VoiceAccessNetworkStateType"));
            System.out.println("VoiceAccessNetworkStateBand:" +intent.getStringExtra("VoiceAccessNetworkStateBand"));
            System.out.println("VoiceAccessNetworkStateSignal:" +intent.getStringExtra("VoiceAccessNetworkStateSignal"));*/
        }
        else if(intent.getAction().equals("demo.diagandroid.phone.AppTriggeredCall")){
            System.out.println(intent.getAction());
            JSONObject object = new JSONObject();
            try {
                object.put("Timestamp", intent.getStringExtra("Timestamp"));
                object.put("ApplicationPackageName", intent.getStringExtra("ApplicationPackageName"));
                AudioQualityMonitor.AppTriggeredCall = intent.getStringExtra("ApplicationPackageName");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(intent.getAction().equals("demo.diagandroid.phone.UICallState")){
            System.out.println(intent.getAction());
            JSONObject object = new JSONObject();
            try {
                object.put("Timestamp", intent.getStringExtra("Timestamp"));
                object.put("UICallState", intent.getStringExtra("UICallState"));
                object.put("VoiceAccessNetworkStateType", intent.getStringExtra("VoiceAccessNetworkStateType"));
                object.put("VoiceAccessNetworkStateSignal", intent.getStringExtra("VoiceAccessNetworkStateSignal"));
                object.put("VoiceAccessNetworkStateBand", intent.getStringExtra("VoiceAccessNetworkStateBand"));
                AudioQualityMonitor.UIState_intents.put(object.toString());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(intent.getAction().equals("demo.diagandroid.phone.LogcatEvent")){
            System.out.println(intent.getAction());
            JSONObject object = new JSONObject();
            try {
                object.put("CallID", intent.getStringExtra("CallID"));
                object.put("CallNumber", intent.getStringExtra("CallNumber"));
                object.put("CallSetupFailureReason", intent.getStringExtra("CallSetupFailureReason"));
                object.put("CallDropReason", intent.getStringExtra("CallDropReason"));
                object.put("MutingStartTime", intent.getStringExtra("MutingStartTime"));
                object.put("MutingEndTime", intent.getStringExtra("MutingEndTime"));
                object.put("StartTime", intent.getStringExtra("StartTime"));
                object.put("EndTime", intent.getStringExtra("EndTime"));
                object.put("CallStartLocationX", intent.getExtras().getDouble("CallStartLocationX"));
                object.put("CallStartLocationY", intent.getExtras().getDouble("CallStartLocationY"));
                object.put("CallEndLocationX", intent.getExtras().getDouble("CallEndLocationX"));
                object.put("CallEndLocationY", intent.getExtras().getDouble("CallEndLocationY"));
                object.put("CallDirection", intent.getStringExtra("CallDirection"));
                object.put("CallType", intent.getStringExtra("CallType"));
                object.put("ApplicationPackageName",AudioQualityMonitor.AppTriggeredCall);
                object.put("MSISDN",intent.getStringExtra("MSISDN"));
                object.put("Model",intent.getStringExtra("Model"));
                AudioQualityMonitor.result.put("CallAggregatedData",object.toString());
                AudioQualityMonitor.result.put("CallState_Intents",AudioQualityMonitor.CallState_intents);
                AudioQualityMonitor.result.put("UIState_Intents",AudioQualityMonitor.UIState_intents);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                System.out.println(AudioQualityMonitor.result.toString(2));
            }
            catch (Exception e){
                e.printStackTrace();
            }

            HttpClient httpClient = new DefaultHttpClient();
            try{
                //Force to use synchronized network
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpPost request = new HttpPost("http://psychic-rush-755.appspot.com/upload");
                //Log.d("DEBUG",AudioQualityMonitor.result.toString());
                List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                parameters.add(new BasicNameValuePair("result",AudioQualityMonitor.result.toString()));
                request.setEntity(new UrlEncodedFormEntity(parameters));
                HttpResponse response = httpClient.execute(request);
                StatusLine status = response.getStatusLine();
                if(status.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    Log.e("DEBUG",out.toString());
                    out.close();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally{
                httpClient.getConnectionManager().shutdown();

            }
            AudioQualityMonitor.result = null;
            AudioQualityMonitor.result = new JSONObject();
            AudioQualityMonitor.UIState_intents = null;
            AudioQualityMonitor.UIState_intents = new JSONArray();
            AudioQualityMonitor.CallState_intents = null;
            AudioQualityMonitor.CallState_intents = new JSONArray();
        }
    }
}
