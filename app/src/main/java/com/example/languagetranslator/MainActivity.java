package com.example.languagetranslator;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ArrayList<TrContainer> al = new ArrayList<>();
    public static String ENGLISH = "en";
    public static String ALBANIAN = "sq";
    public static String BULGARIAN = "bg";
    public static String HUNGARIAN = "hu";
    public static String GREEK = "el";
    public static String INDONESIAN = "id";
    public static String GERMAN = "de";
    public static String DUTCH = "nl";
    public static String FRENCH = "fr";
    public static String ITALIAN = "it";
    public static String TURKISH = "tr";
    public static String HINDI = "hi";
    public static String ARABIC = "ar";
    public static String RUSSIAN = "ru";
    public static String PORTUGUESE = "pt";
    public static String SPANISH = "es";
    public static String JAPANESE = "ja";
    public static String CHINESE = "zh";
    public static String POLISH = "pl";
    public static String SWEDISH = "sv";
    public static String FINNISH = "fi";
    public static String ROMANIAN = "ro";
    public static String UKRAINIAN = "uk";
    public static String CZECH = "cs";
    public static String KOREAN = "ko";
    static String[] languages = {ENGLISH,ALBANIAN,BULGARIAN,HUNGARIAN,GREEK,INDONESIAN,GERMAN,DUTCH,FRENCH,ITALIAN,TURKISH,HINDI,ARABIC,RUSSIAN,PORTUGUESE,SPANISH,JAPANESE,CHINESE,POLISH,SWEDISH,FINNISH,ROMANIAN,UKRAINIAN,CZECH,KOREAN};
    static String[] languages2 = {ARABIC, BULGARIAN, CZECH, GERMAN, GREEK, ENGLISH, SPANISH, FINNISH, FRENCH, HINDI, HUNGARIAN, INDONESIAN, ITALIAN, JAPANESE, KOREAN, DUTCH, POLISH, PORTUGUESE, ROMANIAN, RUSSIAN, ALBANIAN, SWEDISH, TURKISH, UKRAINIAN, CHINESE};

    static MainActivity a;
    EditText tv1;
    Button button;
    int count = 0;
    static ArrayList<TranslateContainer> transList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        a = this;
        tv1 = findViewById(R.id.text1);
        button = findViewById(R.id.button);
        transList.add(new TranslateContainer(tv1,tv1.getText().toString()));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskTranslate task = new taskTranslate();
                task.execute();
            }
        });
    }

    public class taskTranslate extends AsyncTask<String,Integer,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String trans = "Соревноваться с другими путешественниками", lang = "ru";
                        for (String targetLang : languages) {
                            if (!Objects.equals(targetLang, lang)) {
                                OkHttpClient client = new OkHttpClient();
                                StringBuilder googleApiUrl = new StringBuilder("https://translate.googleapis.com/translate_a/single?client=gtx&sl="+lang+"&tl=" + targetLang + "&dt=t");
                                FormBody.Builder body = new FormBody.Builder();
                                body.add("q", trans);
                                RequestBody requestBody = body.build();
                                Request request = new Request.Builder().url(googleApiUrl.toString()).post(requestBody).build();
                                Call call = client.newCall(request);
                                call.enqueue(new Callback() {
                                    @Override
                                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                        String jsonData = response.body().string();
                                        try {
                                            StringBuilder sb = new StringBuilder();
                                            JSONArray object = new JSONArray(jsonData);
                                            for (int i = 0; i < object.getJSONArray(0).length(); i++) {
                                                sb.append(object.getJSONArray(0).getJSONArray(i).getString(0));
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    al.add(new TrContainer(targetLang,sb.toString()));
                                                    count++;
                                                    if (count == 25)
                                                    {
                                                        startLog();
                                                    }

                                                }
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                    @Override
                                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                        Log.d(TAG, "HTTP CALL FAIL");
                                    }


                                });
                            }
                            else
                            {
                                al.add(new TrContainer(targetLang,trans.toString()));
                                count++;
                                if (count == 25)
                                {
                                    startLog();
                                }
                            }
                        }

            return null;
        }



    }

    private void startLog() {
        for (int i = 0; i<=24; i++)
        {
            for (TrContainer it : al)
            {
                if (Objects.equals(languages2[i], it.langName))
                {
                    Log.d(it.langName,"\""+it.text+"\",");
                }
            }

        }
    }

    public class TranslateContainer
    {
        TextView tv;
        String text;

        public TranslateContainer(TextView tv, String text) {
            this.tv = tv;
            this.text = text;
        }

        public TextView getTv() {
            return tv;
        }

        public void setTv(TextView tv) {
            this.tv = tv;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public class TrContainer{
        public String langName;
        public String text;

        public TrContainer(String langName, String text) {
            this.langName = langName;
            this.text = text;
        }
    }
}