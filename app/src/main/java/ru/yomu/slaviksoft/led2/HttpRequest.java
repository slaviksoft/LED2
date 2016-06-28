package ru.yomu.slaviksoft.led2;

import android.os.AsyncTask;
import android.util.Log;

public class HttpRequest extends AsyncTask<Integer, Void, Boolean> {

    OnHttpRequestListener mListener;

    public interface OnHttpRequestListener{
        void onHttpRequestResult(boolean result);
    }

    public HttpRequest(OnHttpRequestListener listener){
        mListener = (OnHttpRequestListener)listener;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        try {
            Thread.sleep(params[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);
        mListener.onHttpRequestResult(b);
    }


    static public void login_request(String login, String password, OnHttpRequestListener listener){

        HttpRequest http = new HttpRequest(listener);
        http.execute(300);
        Log.d("DEBUG", "login=" + login);
        Log.d("DEBUG", "pass=" + password);
    }


}

