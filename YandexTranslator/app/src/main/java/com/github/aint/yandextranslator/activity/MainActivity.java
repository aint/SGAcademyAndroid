package com.github.aint.yandextranslator.activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
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
import com.github.aint.yandextranslator.model.DetectJsonResponse;
import com.github.aint.yandextranslator.model.TranslateJsonResponse;
import com.github.aint.yandextranslator.service.YandexTranslateService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends AppCompatActivity implements Callback<TranslateJsonResponse>, OnItemSelectedListener {

    private static final String TAG = MainActivity.class.getName();

    private static final String YANDEX_TRANSLATE_URL = "https://translate.yandex.net/api/v1.5/tr.json/";
    private static final String API_KEY = "trnsl.1.1.20160815T165258Z.23bee0520431f4c8.5466f3d425604ca53525b97b38a76fff39e468a0";

    private static final String SOMETHING_WENT_WRONG_TOAST = "Something went wrong. Try again";
    private static final String TRANSLATED_TEXT_COPIED_TOAST = "Translated text copied";
    private static final String PROGGRESS_DIALOG_MESSAGE = "Translating...";

    private static final String KEY_REQUEST_FIELD = "key";
    private static final String TEXT_REQUEST_FIELD = "text";
    private static final String LANG_REQUEST_FIELD = "lang";

    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(new Gson()))
            .baseUrl(YANDEX_TRANSLATE_URL)
            .build();

    private YandexTranslateService translateService = retrofit.create(YandexTranslateService.class);

    private ArrayAdapter<CharSequence> spinnerAdapter;

    private String langFrom = Language.Ukrainian.name();
    private String langTo = Language.Ukrainian.name();

    @BindView(R.id.textFrom) EditText textFromEditText;
    @BindView(R.id.textTo) EditText textToEditText;

    @BindView(R.id.lang_spinner1) Spinner fromLangSpinner;
    @BindView(R.id.lang_spinner2) Spinner toLangSpinner;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpLangSpinners();
        initProgressDialog();
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(PROGGRESS_DIALOG_MESSAGE);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.setProgressPercentFormat(null);
    }

    private void setUpLangSpinners() {
        spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.languages,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        initFromLangSpinner();
        initToLangSpinner();
    }

    private void initFromLangSpinner() {
        fromLangSpinner.setAdapter(spinnerAdapter);
        fromLangSpinner.setSelection(spinnerAdapter.getPosition(langFrom));
        fromLangSpinner.setOnItemSelectedListener(this);
    }

    private void initToLangSpinner() {
        toLangSpinner.setAdapter(spinnerAdapter);
        toLangSpinner.setSelection(spinnerAdapter.getPosition(getDefaultLocale().name()));
        toLangSpinner.setOnItemSelectedListener(this);
    }

    private Language getDefaultLocale() {
        return Language.lookUp(getResources().getConfiguration().locale.getLanguage());
    }

    private void translateText() {
        hideKeyboard();
        translateService.translate(getRequestFieldMap()).enqueue(this);
        progressDialog.show();
    }

    private Map<String, String> getRequestFieldMap() {
        Map<String, String> map = new TreeMap<>();
        map.put(KEY_REQUEST_FIELD, API_KEY);
        map.put(TEXT_REQUEST_FIELD, textFromEditText.getText().toString());
        map.put(LANG_REQUEST_FIELD, getLangCodes());
        return map;
    }

    private String getLangCodes() {
        String langCodeFrom = detectInputLanguage();
        langFrom = Language.lookUp(langCodeFrom).name();
        fromLangSpinner.setSelection(spinnerAdapter.getPosition(langFrom));
        return langCodeFrom + "-" +  Language.valueOf(langTo).getLangCode();
    }

    private String detectInputLanguage() {
        try {
            return new DetectLangAsyncTask().execute(textFromEditText.getText().toString()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return Language.valueOf(langFrom).getLangCode();
    }

    @Override
    public void onResponse(Call<TranslateJsonResponse> call, Response<TranslateJsonResponse> response) {
        progressDialog.dismiss();
        if (response.code() == 200) {
            String textTo = response.body().getText()[0];
            textToEditText.setHint(textTo);
            return;
        }
        Toast.makeText(this, "Oops... " + response.code(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Call<TranslateJsonResponse> call, Throwable t) {
        progressDialog.dismiss();
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
        } else if (R.id.menu_clear == itemId) {
            clearInputFields();
        } else if (R.id.menu_copy == itemId) {
            Toast.makeText(this, TRANSLATED_TEXT_COPIED_TOAST, LENGTH_SHORT).show();
            copyToClipboard();
        }
        return super.onOptionsItemSelected(item);
    }

    private void copyToClipboard() {
        ClipData clip = ClipData.newPlainText("", textToEditText.getHint());
        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(clip);
    }

    private void clearInputFields() {
        textFromEditText.setText("");
        textToEditText.setHint("");
    }

    public void onSwapLangButtonClick(View view) {
        fromLangSpinner.setSelection(spinnerAdapter.getPosition(langTo));
        toLangSpinner.setSelection(spinnerAdapter.getPosition(langFrom));
        langFrom = fromLangSpinner.getSelectedItem().toString();
        langTo = toLangSpinner.getSelectedItem().toString();
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

    private class DetectLangAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                DetectJsonResponse jsonResponse = translateService.detect(API_KEY, params[0]).execute().body();
                if (jsonResponse.getCode() == 200) {
                    return jsonResponse.getLang();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
