package com.akruglov.translator.data.remote;

import com.akruglov.translator.data.TranslateDataSource;
import com.akruglov.translator.data.models.Language;
import com.akruglov.translator.data.remote.api.DetectLanguageResult;
import com.akruglov.translator.data.remote.api.GetLanguagesResult;
import com.akruglov.translator.data.remote.api.TranslateResult;
import com.akruglov.translator.data.remote.api.YandexService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by akruglov on 09.04.17.
 */

public class TranslateRemoteDataSource implements TranslateDataSource {

    private static TranslateRemoteDataSource INSTANCE;

    private Retrofit retrofit;
    private YandexService translateService;

    public static TranslateRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TranslateRemoteDataSource();
        }
        return INSTANCE;
    }

    private TranslateRemoteDataSource() {
        retrofit = new Retrofit.Builder()
                .baseUrl(YandexService.BASE_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        translateService = retrofit.create(YandexService.class);
    }

    public void getLanguages(final ResultCallback<List<Language>> callback) {
        Call<GetLanguagesResult> getLanguagesResultCall = translateService.getLanguages(
                        YandexService.API_KEY,
                        Locale.getDefault().getLanguage());

        getLanguagesResultCall.enqueue(new Callback<GetLanguagesResult>() {
            public void onResponse(Call<GetLanguagesResult> call, Response<GetLanguagesResult> response) {
                if (response.isSuccessful()) {
                    HashMap<String,String> languagesMap = response.body().getLanguages();
                    List<Language> languages = new ArrayList<Language>();
                    for (String key : languagesMap.keySet()) {
                        languages.add(new Language(-1, key, languagesMap.get(key)));
                    }
                    callback.onLoaded(languages);
                }
            }

            @Override
            public void onFailure(Call<GetLanguagesResult> call, Throwable throwable) {
                callback.onNotAvailable();
            }
        });
    }

    public void translate(String sourceText, String sourceLanguage, String destinationLanguage) {
        Call<TranslateResult> translateCall = translateService.translate(
                YandexService.API_KEY,
                sourceLanguage + "-" + destinationLanguage,
                "html",
                sourceText);

        translateCall.enqueue(new Callback<TranslateResult>() {
            @Override
            public void onResponse(Call<TranslateResult> call, Response<TranslateResult> response) {
                if (response.isSuccessful()) {
                    List<String> text = response.body().getText();
                    //text.forEach(System.out::println);
                }
            }

            @Override
            public void onFailure(Call<TranslateResult> call, Throwable throwable) {
                System.out.println(throwable);
            }
        });
    }

    public void detectLanguage(String sourceText) {
        Call<DetectLanguageResult> detectLanguageResultCall = translateService.detectLanguage(
                YandexService.API_KEY,
                sourceText
        );

        detectLanguageResultCall.enqueue(new Callback<DetectLanguageResult>() {
            @Override
            public void onResponse(Call<DetectLanguageResult> call, Response<DetectLanguageResult> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<DetectLanguageResult> call, Throwable throwable) {
                System.out.println(throwable);
            }
        });
    }
}
