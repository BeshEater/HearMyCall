package com.besheater.hearmycall;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WebServerHandler extends Service {
    private final IBinder binder = new WebServerBinder();
    private final String urlGetUsers = "http://192.168.1.39:8080/GetUsers";
    private final String urlGetMessages = "http://192.168.1.39:8080/GetMessages";
    private final String urlAddMessage = "http://192.168.1.39:8080/AddMessage";
    private final String uniqIdCookieName = "uniqId";
    private final long serverUpdateInterval = 10000; // in ms
    private URI uriGetUsers;
    private URI uriGetMessages;
    private URI uriAddMessage;
    private CookieManager cookieManager;
    private RequestQueue queue;
    private List<User> users = new ArrayList<>();
    private List<ChatMessage> messages = new ArrayList<>();
    private boolean isUsersUpdating = false;
    private boolean isMessagesUpdating = false;
    private UserData userData;
    public WebServerHandler() {
        try {
            this.uriGetUsers = new URI(urlGetUsers);
            this.uriGetMessages = new URI(urlGetMessages);
            this.uriAddMessage = new URI(urlAddMessage);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getConnectedUser() {

        User connectedUser = null;
        if (userData.getConnectedUsersId() != null &&
                userData.getConnectedUsersId().length > 0 &&
                users.size() > 0) {

            int connectedUserId = userData.getConnectedUsersId()[0];
            for (User user : users) {
                if (user.id == connectedUserId) {
                    connectedUser = user;
                }
            }
        }
        return connectedUser;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void stopPeriodicUsersUpdates() {
        isUsersUpdating = false;
    }
    public void stopPeriodicMessagesUpdates() {
        isMessagesUpdating = false;
    }

    public void setupRepeatedRequestsInBackground(UserData userData) {
        this.userData = userData;
        // Stop all previous periodic updates
        isUsersUpdating = false;
        isMessagesUpdating = false;

        // Create custom Cookie cookieManager
        cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        // Create queue
        queue = Volley.newRequestQueue(this);

        // Start requesting server
        startRequestingServerForUsers();
        startRequestingServerForMessages();
    }

    public void sendMessage(ChatMessage message) {

        // Create response listener
        final Response.Listener<JSONObject> respListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Do nothing
            }
        };

        // Create response error listener
        final Response.ErrorListener respErrListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Do nothing
                Log.i("ERR connecting to serv:",
                        "Can't add message to /AddMessage message:" + error.getMessage());
            }
        };

        // Construct object to send
        Gson gson = new Gson();
        JSONObject thisMessageJSONobj = null;
        try {
            thisMessageJSONobj = new JSONObject(gson.toJson(message));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Construct request with message object
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST, urlAddMessage,
                        thisMessageJSONobj, respListener, respErrListener);
        // Make uniqId cookie
        HttpCookie cookie = new HttpCookie(uniqIdCookieName, userData.getUniqId());
        // Add uniqId cookie
        cookieManager.getCookieStore().add(uriAddMessage, cookie);

        queue.add(jsonObjectRequest);

    }

    public void startRequestingServerForUsers() {

        // Create response listener
        final Response.Listener<JSONObject> respListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //updateUsersList(response);
                updateFromGetUsersResponse(response);
            }
        };

        // Create response error listener
        final Response.ErrorListener respErrListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Do nothing
                Log.i("ERR connecting to serv:",
                        "Can't update users from /GetUsers message:" + error.getMessage());
            }
        };

        // Set running
        isUsersUpdating = true;

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isUsersUpdating) {
                    JSONObject thisUserJSONobj = getThisUserJSONobj();

                    // Construct request with User object
                    JsonObjectRequest jsonObjectRequest =
                            new JsonObjectRequest(Request.Method.POST, urlGetUsers,
                                    thisUserJSONobj, respListener, respErrListener);
                    // Make uniqId cookie
                    HttpCookie cookie = new HttpCookie(uniqIdCookieName, userData.getUniqId());
                    // Add uniqId cookie
                    cookieManager.getCookieStore().add(uriGetUsers, cookie);

                    queue.add(jsonObjectRequest);

                    // Request the same later
                    handler.postDelayed(this, serverUpdateInterval);
                }
            }
        });
    }

    public void startRequestingServerForMessages() {
        // Create response listener
        final Response.Listener<JSONObject> respListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //updateUsersList(response);
                updateFromGetMessagesResponse(response);
            }
        };

        // Create response error listener
        final Response.ErrorListener respErrListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Do nothing
                Log.i("ERR connecting to serv:",
                        "Can't update messages from /GetMessages message:" + error.getMessage());
            }
        };

        // Set running
        isMessagesUpdating = true;

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isMessagesUpdating) {
                    JSONObject thisUserJSONobj = getThisUserJSONobj();

                    // Construct request with User object
                    JsonObjectRequest jsonObjectRequest =
                            new JsonObjectRequest(Request.Method.POST, urlGetMessages,
                                    thisUserJSONobj, respListener, respErrListener);
                    // Make uniqId cookie
                    HttpCookie cookie = new HttpCookie(uniqIdCookieName, userData.getUniqId());
                    // Add uniqId cookie
                    cookieManager.getCookieStore().add(uriGetMessages, cookie);

                    queue.add(jsonObjectRequest);

                    // Request the same later
                    handler.postDelayed(this, serverUpdateInterval);
                }
            }
        });
    }

    private JSONObject getThisUserJSONobj() {
        // Create User object to send
        User thisUser = userData.getThisUserObject();
        Gson gson = new Gson();
        JSONObject thisUserJSONobj = null;
        try {
            thisUserJSONobj = new JSONObject(gson.toJson(thisUser));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return thisUserJSONobj;
    }

    private void updateFromGetMessagesResponse(JSONObject respObj) {
        // Clear all prev messages
        messages.clear();

        try {
            JSONArray messagesJSON = respObj.getJSONArray("messages");
            for (int i = 0; i < messagesJSON.length(); i++) {
                JSONObject messageJSON = (JSONObject) messagesJSON.get(i);
                User user = parseUser(messageJSON.getJSONObject("user"));
                ChatMessage message = new ChatMessage(user,
                        messageJSON.getString("text"),
                        messageJSON.getLong("time"));
                messages.add(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateFromGetUsersResponse(JSONObject respObj) {
        // update uniqId
        List<HttpCookie> cookies = cookieManager.getCookieStore().get(uriGetUsers);
        for (HttpCookie cookie : cookies) {
            if (cookie.getName().equals("uniqIdCookieName")) {
                userData.setUniqId(cookie.getValue());
                break;
            }
        }

        try {
            // update Id
            userData.setId(respObj.getInt("yourId"));

            List<User> users = new ArrayList<>();

            // update Users list
            JSONArray usersJSON = respObj.getJSONArray("users");
            for (int i = 0; i < usersJSON.length(); i++) {
                JSONObject userJSON = (JSONObject) usersJSON.get(i);
                User user = parseUser(userJSON);
                users.add(user);
            }

            this.users = users;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private User parseUser(JSONObject userJSON) throws JSONException {
        User user = new User(userJSON.getInt("id"),
                userJSON.getString("name"),
                userJSON.getDouble("latitude"),
                userJSON.getDouble("longitude"),
                userJSON.getInt("avatarImageNum"),
                userJSON.optString("callMessage", null),
                getConUsersIdArr(userJSON.optJSONArray("connectedUsersId")),
                userJSON.getLong("time")
        );
        return user;
    }

    private int[] getConUsersIdArr(JSONArray jsonArray) throws JSONException {
        if (jsonArray != null) {
            int length = jsonArray.length();
            int[] arr = new int[length];
            for (int i = 0; i < length; i++) {
                arr[i] = jsonArray.getInt(i);
            }
            return arr;
        } else {
            return null;
        }
    }

    public class WebServerBinder extends Binder {

        WebServerHandler getWebServerHandlerService() {
            return WebServerHandler.this;
        }

    }
}
