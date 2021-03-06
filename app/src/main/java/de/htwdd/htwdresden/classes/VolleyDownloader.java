package de.htwdd.htwdresden.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.LruCache;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * asynchroner HTTP-Download über Projekt Volley
 *
 * @author Kay Förster
 */
public class VolleyDownloader {
    private static VolleyDownloader ourInstance;
    private static Context mcontext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private VolleyDownloader(Context context) {
        mcontext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyDownloader getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new VolleyDownloader(context);
        return ourInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(mcontext.getApplicationContext());
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        if (mImageLoader == null)
            mImageLoader = new ImageLoader(getRequestQueue(), new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap> mCache = new LruCache<>(10);

                @Override
                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url, bitmap);
                }
            });
        return mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }


    /**
     * Überprüft ob aktuell eine Internetverbindung besteht.
     *
     * @param context Referenz auf die aktuelle Activity
     * @return true=Internet verbindung vorhanden, sonst false
     */
    public static boolean CheckInternet(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] networks = connec.getAllNetworks();

        for (Network network : networks) {
            NetworkInfo networkInfo = connec.getNetworkInfo(network);
            if (networkInfo.isConnected())
                return true;
        }
        return false;
    }

    public static int getResponseCode(VolleyError error) {
        if (error.networkResponse != null)
            return error.networkResponse.statusCode;
        else
            // Wenn kein Response vom Server kommt, genauere Fehlermeldung unterscheiden
            if (error instanceof TimeoutError)
                return Const.internet.HTTP_TIMEOUT;
            else if (error instanceof NoConnectionError)
                return Const.internet.HTTP_NO_CONNECTION;
            else if (error instanceof NetworkError)
                return Const.internet.HTTP_NETWORK_ERROR;
            else return Const.internet.HTTP_DOWNLOAD_ERROR;
    }
}
