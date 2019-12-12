package com.example.flutterpluginupdate.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.flutteropenkeysdkplugin.cryptography.SharedPreferencesEncryption;
import com.example.flutteropenkeysdkplugin.enums.MANUFACTURER;
import com.example.flutteropenkeysdkplugin.singleton.GetGson;
import com.example.flutterpluginupdate.api.response.session.SessionResponse;
import com.example.flutterpluginupdate.interfaces.OpenKeyCallBack;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author OpenKey Inc.
 * <p>
 * This class will provide all the necessary  utility methods.
 */

public class Utilities {

    private static SharedPreferences prefs;
    private static Utilities utilities;
    private Toast msg;

    public static Utilities getInstance(Context... contexts) {
        if (utilities == null) {
            utilities = new Utilities();

            if (contexts != null && contexts.length > 0)
                prefs = new SharedPreferencesEncryption(contexts[0].getApplicationContext());
        }
        return utilities;

    }

    public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 22) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return client;
    }

    public boolean isOnline(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        //we are connected to a network
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

    }

    /**
     * Clear values of shared preference.
     *
     * @param context the context
     *                If user logout then clear all the saved values from the
     *                shared preference file
     */
    public void clearValueOfKey(Context context, String key) {
        if (context == null) return;

        SharedPreferences.Editor saveValue = prefs.edit();
        saveValue.remove(key).apply();
    }


    /**
     * Save value to shared preference.
     *
     * @param key     On which key you want to save the value.
     * @param value   The value which needs to be saved.
     * @param context the context
     *                To save the value to a preference file on the specified key.
     */
    public void saveValue(String key, String value, Context context) {
        if (context == null) return;

        SharedPreferences.Editor saveValue = prefs.edit();
        saveValue.putString(key, value);
        saveValue.apply();
    }

    /**
     * Gets value from shared preference.
     *
     * @param key          The key from you want to get the value.
     * @param defaultValue Default value, if nothing is found on that key.
     * @param context      the context
     * @return the value from shared preference
     * To get the value from a preference file on the specified
     * key.
     */
    public String getValue(String key, String defaultValue, Context context) {
        if (context == null) return defaultValue;

        return prefs.getString(key, defaultValue);
    }

    /**
     * for vibration
     */
    @SuppressLint("MissingPermission")
    public void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }


    /**
     * Save value to shared preference.
     *
     * @param key     On which key you want to save the value.
     * @param value   The value which needs to be saved.
     * @param context the context
     *                To save the value to a saved preference file on the
     *                specified key.
     */
    public void saveValue(String key, boolean value, Context context) {
        if (context == null) return;

        SharedPreferences.Editor saveValue = prefs.edit();
        saveValue.putBoolean(key, value);
        saveValue.apply();
    }

    /**
     * Gets value from shared preference.
     *
     * @param key          The key from you want to get the value.
     * @param defaultValue Default value, if nothing is found on that key.
     * @param context      the context
     * @return the value from shared preference
     * To get the value from a saved preference file on the
     * specified key.
     */
    public boolean getValue(String key, boolean defaultValue, Context context) {
        return context != null && prefs.getBoolean(key, defaultValue);
    }

    /**
     * Save value to shared preference.
     *
     * @param key     On which key you want to save the value.
     * @param value   The value which needs to be saved.
     * @param context the context
     *                To save the value to a saved preference file on the
     *                specified key.
     */
    public void saveValue(String key, int value, Context context) {
        if (context == null) return;

        SharedPreferences.Editor saveValue = prefs.edit();
        saveValue.putInt(key, value);
        saveValue.apply();
    }

    /**
     * Gets value from shared preference.
     *
     * @param key          The key from you want to get the value.
     * @param defaultValue Default value, if nothing is found on that key.
     * @param context      the context
     * @return the value from shared preference
     * @description To get the value from a preference file on the specified
     * key.
     */
    public int getValue(String key, int defaultValue, Context context) {
        if (context == null) return 0;

        return prefs.getInt(key, defaultValue);
    }

    /**
     * Get retrofit Object for accessing web services.
     *
     * @return retrofit instance for web service calling
     */
    public Retrofit getRetrofit(Context context) {
        Retrofit retrofit;
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        // set your desired log level
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = getNewHttpClient(context).newBuilder();

//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.readTimeout(30, TimeUnit.SECONDS);
//        httpClient.connectTimeout(30, TimeUnit.SECONDS);


        String url = Constants.BASE_URL_DEV;

        if (context != null)
            url = Utilities.getInstance().getValue(Constants.BASE_URL, Constants.BASE_URL_DEV, context);

        // add logging as last interceptor
//        httpClient.addInterceptor(logging);
        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url)
                .client(httpClient.build())
                .build();

        return retrofit;
    }

    private OkHttpClient getNewHttpClient(Context context) {
        final String UUID = Utilities.getInstance().getValue(Constants.UUID, "", context);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging);

        client.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {

                Request request = chain.request().newBuilder()
                        .header("x-openkey-app", UUID)
                        .header("Accept", "application/json")
                        .header("Cache-Control", "no-cache")
//                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });
        return enableTls12OnPreLollipop(client).build();
    }

    /**
     * Save the booking to shared preference
     *
     * @param booking {@link }
     */
    public void saveBookingToLocal(Context context, SessionResponse booking) {
        Gson gson = new Gson();
        String bookingString = gson.toJson(booking);
        saveValue(Constants.BOOKING, bookingString, context);
    }


    /**
     * Get booking from the saved shared preference
     */
    public SessionResponse getBookingFromLocal(Context context) {
        if (context != null) {
            String bookingString = getValue(Constants.BOOKING, "", context);
            if (!TextUtils.isEmpty(bookingString)) {
                Gson gson = GetGson.getInstance();
                return gson.fromJson(bookingString, SessionResponse.class);
            }
        }
        return null;
    }

    public MANUFACTURER getManufacturer(Context context, OpenKeyCallBack openKeyCallBack) {
        final String manufacturerStr = Utilities.getInstance().getValue(Constants.MANUFACTURER, "", context);
        if (TextUtils.isEmpty(manufacturerStr)) {
            openKeyCallBack.initializationFailure(Response.UNKNOWN);
            throw new IllegalStateException(Response.UNKNOWN);
        }
        return MANUFACTURER.valueOf(manufacturerStr);
    }


}
