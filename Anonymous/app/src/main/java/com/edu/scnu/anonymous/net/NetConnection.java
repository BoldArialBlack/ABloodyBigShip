package com.edu.scnu.anonymous.net;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.edu.scnu.anonymous.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ASUS on 2018/3/30.
 *
 */

public class NetConnection {

    public NetConnection(final String url, final HttpMethod method, final SuccessCallback successCallback, final FailCallback failCallback, final String ... kvs) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {

                StringBuffer paramsStr = new StringBuffer();
                for (int i = 0; i < kvs.length - 1; i += 2) {                       /////////////
                    paramsStr.append(kvs[i]).append("=").append(kvs[i+1]).append("&");
                }

                try {
                    URLConnection uc = new URL(url).openConnection();

                    switch (method) {
                        case POST:
                            uc.setDoOutput(true);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(), Config.CHARSET));
                            bw.write(paramsStr.toString());
                            bw.flush();
                            bw.close();
                            break;
                        default:
                            uc = new URL(url + "?" + paramsStr.toString()).openConnection();
                            break;
                    }

                    Log.e("REQUEST", "url: " + uc.getURL());
                    Log.e("REQUEST", "data: " + paramsStr);

                    BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), Config.CHARSET));
                    String line = null;
                    StringBuffer result = new StringBuffer();

                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }

                    br.close();
                    Log.e("RESULT", result.toString());
                    return result.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {

                if (result != null) {
                    if (successCallback != null) {
                        successCallback.onSuccess(result);
                    }
                } else {
                    if (failCallback != null) {
                        failCallback.onFail();
                    }
                }

                super.onPostExecute(result);
            }
        }.execute();


    }

    public static interface SuccessCallback {
        void onSuccess(String result);
    }

    public static interface FailCallback {
        void onFail();
    }
}
