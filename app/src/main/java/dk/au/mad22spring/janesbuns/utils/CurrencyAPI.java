package dk.au.mad22spring.janesbuns.utils;

import android.app.Application;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.List;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import dk.au.mad22spring.janesbuns.Repository;
import dk.au.mad22spring.janesbuns.models.Currency;

    // https://www.currencyconverterapi.com/docs
public class CurrencyAPI {
    private static final String TAG = "CurrencyAPI";


        // https://free.currconv.com/api/v7/convert?q=DKK_EUR,EUR_DKK&compact=ultra&apiKey=8c339bcbe21bcd7cbb1b
    private static void sendRequest(String convertTo, RequestQueue queue, Application app) {
        StringRequest mRequest = new StringRequest(
                Request.Method.GET,
                "https://free.currconv.com/api/v7/currencies?apiKey=8c339bcbe21bcd7cbb1b",
                response -> getCurrency(response, app),
                error -> Log.e(TAG, "that did not work", error ));
        queue.add(mRequest);
    }

    private static void getCurrency(String response, Application app) {
        Log.d(TAG, "setCurrency: " + response);
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Currency> currency = parseJson(response);
            if (currency != null){
                Log.d(TAG, "CurrencySymbol: " + currency.get(0).getCurrencySymbol());
            }
        });
    }

        public static List<Currency> parseJson(String json){
            Gson gson = new GsonBuilder().create();
            Currency currency = gson.fromJson(json, Currency.class);
            return currency.currencies;
        }
}
