package com.akruglov.translator.data.models;

/**
 * Created by akruglov on 06.04.17.
 */

public class Language {

    private int id;
    private String key;
    private String description;

    public Language(int id, String key, String description) {
        this.id = id;
        this.key = key;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
