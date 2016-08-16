package com.github.aint.yandextranslator.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.aint.yandextranslator.R;
import com.github.aint.yandextranslator.lang.Language;
import com.github.aint.yandextranslator.model.TranslateJsonResponse;
import com.github.aint.yandextranslator.service.YandexTranslateService;
import com.google.gson.Gson;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements Callback<TranslateJsonResponse>, OnItemSelectedListener {

    private static final String TAG = MainActivity.class.getName();

    private static final String YANDEX_TRANSLATE_URL = "https://translate.yandex.net/api/v1.5/tr.json/";
    private static final String API_KEY = "trnsl.1.1.20160815T165258Z.23bee0520431f4c8.5466f3d425604ca53525b97b38a76fff39e468a0";

    private static final String SOMETHING_WENT_WRONG_TOAST = "Something went wrong. Try again";

    private static final String KEY_REQUEST_FIELD = "key";
    private static final String TEXT_REQUEST_FIELD = "text";
    private static final String LANG_REQUEST_FIELD = "lang";

    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(new Gson()))
            .baseUrl(YANDEX_TRANSLATE_URL)
            .build();

    private YandexTranslateService translateService = retrofit.create(YandexTranslateService.class);

    private String langFrom = "English";
    private String langTo = "Ukrainian";

    @BindView(R.id.textFrom) EditText textFromEditText;
    @BindView(R.id.textTo) EditText textToEditText;

    @BindView(R.id.lang_spinner1) Spinner langSpinner1;
    @BindView(R.id.lang_spinner2) Spinner langSpinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpLangSpinners();
    }

    private void setUpLangSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.languages,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        initFromLangSpinner(adapter);
        initToLangSpinner(adapter);
    }

    private void initToLangSpinner(ArrayAdapter<CharSequence> adapter) {
        langSpinner2.setAdapter(adapter);
        langSpinner2.setSelection(adapter.getPosition(langTo));
        langSpinner2.setOnItemSelectedListener(this);
    }

    private void initFromLangSpinner(ArrayAdapter<CharSequence> adapter) {
        langSpinner1.setAdapter(adapter);
        langSpinner1.setSelection(adapter.getPosition(langFrom));
        langSpinner1.setOnItemSelectedListener(this);
    }

    private void translateText() {
        hideKeyboard();
        translateService.translate(getRequestFieldMap()).enqueue(this);
    }

    private Map<String, String> getRequestFieldMap() {
        Map<String, String> map = new TreeMap<>();
        map.put(KEY_REQUEST_FIELD, API_KEY);
        map.put(TEXT_REQUEST_FIELD, textFromEditText.getText().toString());
        map.put(LANG_REQUEST_FIELD, getLangCodes());
        return map;
    }

    private String getLangCodes() {
        return Language.valueOf(langFrom.toUpperCase()).getLangCode()
                + "-" +  Language.valueOf(langTo.toUpperCase()).getLangCode();
    }

    @Override
    public void onResponse(Call<TranslateJsonResponse> call, Response<TranslateJsonResponse> response) {
        if (response.code() == 200) {
            String textTo = response.body().getText()[0];
            textToEditText.setHint(textTo);
            return;
        }
        Toast.makeText(this, "Oops... " + response.code(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Call<TranslateJsonResponse> call, Throwable t) {
        Toast.makeText(this, SOMETHING_WENT_WRONG_TOAST, Toast.LENGTH_LONG).show();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (R.id.menu_ok == itemId) {
            translateText();
        } else if (R.id.menu_lang == itemId) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.lang_spinner1) {
            langFrom = parent.getItemAtPosition(position).toString();
        } else if (spinner.getId() == R.id.lang_spinner2) {
            langTo = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
