package com.akruglov.translator.data.remote.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by akruglov on 04.04.17.
 */
public class DetectLanguageResult {

    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("lang")
    @Expose
    private String language;

    public Integer getCode() {
        return code;
    }


    public String getLanguage() {
        return language;
    }

}
