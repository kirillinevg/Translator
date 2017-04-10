package com.akruglov.translator.data.remote.api;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TranslateResult {

    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("lang")
    @Expose
    private String language;

    @SerializedName("text")
    @Expose
    private List<String> text = null;

    public Integer getCode() {
        return code;
    }

    public String getLanguage() {
        return language;
    }

    public List<String> getText() {
        return text;
    }
}