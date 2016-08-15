package com.github.aint.yandextranslator.service;

import com.github.aint.yandextranslator.model.TranslateJsonResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface YandexTranslateService {

    @FormUrlEncoded
    @POST("translate")
    Call<TranslateJsonResponse> translate(@FieldMap Map<String, String> map);
}
