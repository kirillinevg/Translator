package com.akruglov.translator.data.models;

import android.support.annotation.NonNull;

/**
 * Created by akruglov on 07.04.17.
 */

public class Translation {

    private int id;
    private Language sourceLanguage;
    private Language destinationLanguage;
    private String sourceText;
    private String translatedText;
    private boolean isFavorite;

    public Translation(int id,
                       @NonNull Language sourceLanguage,
                       @NonNull Language destinationLanguage,
                       String sourceText,
                       String translatedText,
                       boolean isFavorite) {
        this.id = id;
        this.sourceLanguage = sourceLanguage;
        this.destinationLanguage = destinationLanguage;
        this.sourceText = sourceText;
        this.translatedText = translatedText;
        this.isFavorite = isFavorite;
    }

    public Translation(Translation other) {
        this(other.getId(),
             other.getSourceLanguage(),
             other.getDestinationLanguage(),
             other.getSourceText(),
             other.getTranslatedText(),
             other.isFavorite());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Language getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(@NonNull Language sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public Language getDestinationLanguage() {
        return destinationLanguage;
    }

    public void setDestinationLanguage(@NonNull Language destinationLanguage) {
        this.destinationLanguage = destinationLanguage;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
