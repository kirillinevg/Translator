package com.akruglov.translator.data.remote.api;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by akruglov on 04.04.17.
 */
public interface YandexService {

    String API_KEY = "trnsl.1.1.20170404T150453Z.847989c2f12d2c17.97a685370eb6a134458619cb114807364fb49565";

    String BASE_URI = "https://translate.yandex.net";

    @POST("/api/v1.5/tr.json/detect")
    Call<DetectLanguageResult> detectLanguage(
            @Query("key") String key,
            @Query("text") String text
    );

    @POST("/api/v1.5/tr.json/translate")
    Call<TranslateResult> translate(
            @Query("key") String key,
            @Query("lang") String language,
            @Query("format") String format,
            @Query("text") String text
    );

    @POST("api/v1.5/tr.json/getLangs")
    Call<GetLanguagesResult> getLanguages(
            @Query("key") String key,
            @Query("ui") String uiLanguage
    );
}
