package com.example.MeetupsEventBrowser.model;

import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Meetup {
    private String urlkey;
    private String members;
    private String link;
    private String name;
    private String description;
    private String ID;
    private String updated;
    private String photo;


    /**
     * @param jsonArray {@link JSONObject} response, received in Volley success listener
     * @return list of movies
     * @throws JSONException
     */
    public static List<Meetup> parseJson(JSONArray jsonArray) throws JSONException {
        List<Meetup> meetups = new ArrayList<>();

        // Get JSONArray from JSONObject
        for (int i = 0; i < jsonArray.length(); i++) {
            meetups.add(new Meetup(jsonArray.getJSONObject(i)));
        }

        return meetups;
    }

    /**
     * <p>Class constructor</p>
     * <p>Sample Meetup JSONObject</p>
     * <pre>
     * [
     *  {
     *  "score": 1,
     *  "id": 13402242,
     *  "name": "Docker Online Meetup",
     *  "link": "https://www.meetup.com/Docker-Online-Meetup/",
     *  "urlname": "Docker-Online-Meetup",
     *  "description": ...
     *      ...
     *  }
     * ]
     * </pre>
     *
     * @param jsonObject {@link JSONObject} from each item in the search result
     * @throws JSONException when parser fails to parse the given JSON
     */
    private Meetup(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("urlkey")) this.setUrlkey(jsonObject.getString("urlkey"));
        if (jsonObject.has("members")) this.setMembers(jsonObject.getString("members"));
        if (jsonObject.has("link")) this.setLink(jsonObject.getString("link"));
        if (jsonObject.has("name")) this.setName(jsonObject.getString("name"));
        if (jsonObject.has("description")) this.setDescription(jsonObject.getString("description"));
        this.setDescription(removeHtml(this.getDescription()));
        if (jsonObject.has("id")) this.setID(jsonObject.getString("id"));
        if (jsonObject.has("updated")) this.setUpdated(jsonObject.getString("updated"));
        if (jsonObject.has("group_photo"))
            this.setPhoto(jsonObject.getJSONObject("group_photo").getString("photo_link"));
    }

    public String removeHtml(String mDescription)
    {
        String result = Html.fromHtml(mDescription).toString();
        return result;
    }

    public String getUrlkey() {
        return urlkey;
    }

    public void setUrlkey(String urlkey) {
        this.urlkey = urlkey;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
