package com.guilsch.vocavox;

import android.os.AsyncTask;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.json.JSONArray;

import java.io.IOException;
import java.net.URLEncoder;

public class TranslationAPI extends AsyncTask<String, String, String> {

    private OnTranslationCompleteListener listener;

    @Override
    protected String doInBackground(String... strings) {

        String[] strArr = strings;
        String str = "";

        try {
            String encode = URLEncoder.encode(strArr[0], "utf-8");
            StringBuilder sb = new StringBuilder();

            sb.append("https://translate.googleapis.com/translate_a/single?client=gtx&sl=");
            sb.append(strArr[1]);
            sb.append("&tl=");
            sb.append(strArr[2]);
            sb.append("&dt=t&q=");
            sb.append(encode);

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(sb.toString())
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    String responseBodyString = responseBody.string();

                    JSONArray jsonArray = new JSONArray(responseBodyString).getJSONArray(0);
                    StringBuilder result = new StringBuilder();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONArray jsonArray2 = jsonArray.getJSONArray(i);
                        result.append(jsonArray2.get(0).toString());
                    }

                    return result.toString();
                }
            } else {
                throw new IOException("Request failed with code: " + response.code());
            }

        } catch (Exception e) {
            Log.e("translate_api", e.getMessage());
            if (listener != null) {
                listener.onError(e);
            }
            return str;
        }
        return str;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null) {
            listener.onStartTranslation();
        }
    }

    @Override
    protected void onPostExecute(String text) {
        if (listener != null) {
            listener.onCompleted(text);
        }
    }

    public interface OnTranslationCompleteListener {
        void onStartTranslation();
        void onCompleted(String text);
        void onError(Exception e);
    }

    public void setOnTranslationCompleteListener(OnTranslationCompleteListener listener) {
        this.listener = listener;
    }
}