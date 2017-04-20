package com.akruglov.translator.data.remote.api;

import java.util.HashMap;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by akruglov on 04.04.17.
 */
public class GetLanguagesResult {

    @SerializedName("dirs")
    @Expose
    private List<String> directions = null;

    @SerializedName("langs")
    @Expose
    private HashMap<String, String> languages;

    public List<String> getDirections() {
        return directions;
    }

    public HashMap<String, String> getLanguages() {
        return languages;
    }

}
