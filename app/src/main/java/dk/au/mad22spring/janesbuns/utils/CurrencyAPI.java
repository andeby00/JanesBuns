package dk.au.mad22spring.janesbuns.utils;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

// https://www.currencyconverterapi.com/docs
public class CurrencyAPI {
    public interface IApiCallback {
        void apiHandler(String response);
    }

    private static final String TAG = "CurrencyAPI";

    public void sendRequest(String url, RequestQueue queue, IApiCallback callback) {
        StringRequest mRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> callback.apiHandler(response),
                error -> Log.e(TAG, "that did not work", error )
        );

        queue.add(mRequest);
    }
}
