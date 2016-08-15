package com.github.aint.yandextranslator.request;

import com.github.aint.yandextranslator.model.JsonModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface YandexTranslateEndpoint {

    @FormUrlEncoded
    @POST("translate")
    Call<JsonModel> send(@FieldMap Map<String, String> map);
}
