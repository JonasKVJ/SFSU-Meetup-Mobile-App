package com.example.MeetupsEventBrowser.controller;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.MeetupsEventBrowser.app.App;
import com.example.MeetupsEventBrowser.model.GPSTracker;
import com.example.MeetupsEventBrowser.model.Meetup;
import com.example.MeetupsEventBrowser.request.JsonRequest;
import com.example.MeetupsEventBrowser.volley.VolleySingleton;

import java.util.List;

/**
 * <p> Provides interface between {@link android.app.Activity} and {@link com.android.volley.toolbox.Volley} </p>
 */
public class JsonController
{

    private final int TAG = 100;

    private OnResponseListener responseListener;

    /**
     * @param responseListener {@link OnResponseListener}
     */
    public JsonController(OnResponseListener responseListener)
    {
        this.responseListener = responseListener;
    }

    /**
     * Adds request to volley request queue
     *
     * @param query query term for search
     */
    public void sendRequest(String query)
    {
        int queryLength = query.length();

        //Update location for every 3rd letter in the search bar
        boolean updateLocation = (queryLength%3)==0;

        //default search, always used if no location data is available
        String url = "https://api.meetup.com/find/groups?&sign=true&key=256e4958272a645d7451336a3949d1c&photo-host=public&page=20&text=" + Uri.encode(query);

        if(updateLocation==true)
        {
            //Update latitude and longitude according to location data of the device
            GPSTracker gpsTracker = new GPSTracker(App.getContext());
            final int radius = 10;
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            String stringLatitude = Double.toString(latitude);
            String stringLongitude = Double.toString(longitude);

            if (gpsTracker.getIsGPSTrackingEnabled())
            {
                if (stringLatitude == "0.0")
                {
                    System.out.println("Latitude is null");
                } else
                {
                    System.out.println("Latitude is: " + latitude);
                }

                if (stringLongitude == "0.0")
                {
                    System.out.println("Longitude is null");
                } else
                {
                    System.out.println("Longitude is: " + longitude);
                }
            } else
            {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                //Crashes program

                //gpsTracker.showSettingsAlert();
            }

            //Search for Meetup groups based on location only if lat and lon is available
            if(longitude!=0.0 && latitude!=0.0)
            {
                url = "https://api.meetup.com/find/groups?&sign=true&key=256e4958272a645d7451336a3949d1c&photo-host=public&page=20&text=" + Uri.encode(query) + "&lon="
                        + longitude + "&lat=" + latitude + "&radius=" + radius;
            }

        }


        // Request Method
        int method = Request.Method.GET;

        // Create new request using JsonRequest
        JsonRequest request
                = new JsonRequest(
                method,
                url,
                new Response.Listener<List<Meetup>>()
                {
                    @Override
                    public void onResponse(List<Meetup> meetups)
                    {
                        responseListener.onSuccess(meetups);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        responseListener.onFailure(error.getMessage());
                    }
                }
        );

        // Add tag to request
        request.setTag(TAG);

        // Get RequestQueue from VolleySingleton
        VolleySingleton.getInstance(App.getContext()).addToRequestQueue(request);
    }

    /**
     * <p>Cancels all request pending in request queue,</p>
     * <p> There is no way to control the request already processed</p>
     */
    public void cancelAllRequests()
    {
        VolleySingleton.getInstance(App.getContext()).cancelAllRequests(TAG);
    }

    /**
     * Interface to communicate between {@link android.app.Activity} and {@link JsonRequest}
     * <p>Object available in {@link JsonRequest} and implemented in {@link com.example.MeetupsEventBrowser.MainActivity}</p>
     */
    public interface OnResponseListener
    {
        void onSuccess(List<Meetup> meetups);

        void onFailure(String errorMessage);
    }

}