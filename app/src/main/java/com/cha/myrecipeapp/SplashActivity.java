package com.cha.myrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cha.myrecipeapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    Intent intent;
    SQLiteDatabase database;
    ImageView gif_iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("thread","onCreate 진입");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);

        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);


        gif_iv = findViewById(R.id.loading_gif);
        Glide.with(this).load(R.drawable.loading).into(gif_iv);

        LoadingThread loadingThread = new LoadingThread();
        try {
            loadingThread.join();
            loadingThread.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    class StartThread extends Thread {

        @Override
        public void run() {
            Log.i("thread","스타트스레드진입");
            database = openOrCreateDatabase("selecteditem.db",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS selecteditem(num INTEGER PRIMARY KEY , idx INTEGER)");
            intent = new Intent(SplashActivity.this, MainActivity.class);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            startActivity(intent);
            finish();
            Log.i("thread","스타트스레드끝");
        }
    }
    class LoadingThread extends Thread {
        @Override
        public void run() {
            database = openOrCreateDatabase("recipe.db", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS recipe (num INTEGER PRIMARY KEY , title VARCHAR(30) NOT NULL, imgurl VARCHAR(500), hash VARCHAR(30), ingre VARCHAR(500), howto VARCHAR(2000))");
            Log.i("thread","로딩스레드진입");

            Cursor cursor = database.rawQuery("SELECT * FROM recipe", null);
            if (cursor.getCount() >= 10) {
                new StartThread().start();
                return;
            }

            String address = "http://openapi.foodsafetykorea.go.kr/api/50c404d9fa5141449493/COOKRCP01/xml/1/3";

            try {
                URL url = new URL(address);

                InputStream is = url.openStream();
                InputStreamReader isr = new InputStreamReader(is);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(isr);

                int eventType = xpp.getEventType();

                Item item = null;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagName = "";
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            item = new Item();
                            break;
                        case XmlPullParser.START_TAG:
                            tagName = xpp.getName();
                            if (tagName.equals("row")) {
                                item.hash = "";
                                item.recipe_text = "";
                                item.recipe_ingre = "";
                            } else if (tagName.equals("RCP_NM")) {
                                xpp.next();
                                item.title = xpp.getText();
                            } else if (tagName.equals("ATT_FILE_NO_MAIN")) {
                                xpp.next();
                                item.mainImg = xpp.getText();
                            } else if (tagName.equals("HASH_TAG")) {

                                if (!xpp.isEmptyElementTag()) {
                                    xpp.next();
                                    item.hash += "#" + xpp.getText() + "  ";
                                }
                            } else if (tagName.equals("RCP_WAY2")) {
                                xpp.next();
                                item.hash += "#" + xpp.getText() + "  ";
                            } else if (tagName.equals("RCP_PAT2")) {
                                xpp.next();
                                item.hash += "#" + xpp.getText();
                            } else if (tagName.equals("RCP_PARTS_DTLS")) {
                                xpp.next();
                                item.recipe_ingre += xpp.getText();
                            } else if (tagName.contains("MANUAL") && !(tagName.contains("_IMG"))) {
                                if (!xpp.isEmptyElementTag()) {
                                    xpp.next();
                                    item.recipe_text += xpp.getText();
                                }
                            }
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.END_TAG:
                            tagName = xpp.getName();
                            if (tagName.equals("row")) {
                                database.execSQL("INSERT INTO recipe (title, imgurl, hash, ingre, howto) VALUES (?,?,?,?,?)", new String[]{item.title, item.mainImg, item.hash, item.recipe_ingre, item.recipe_text});
                            }
                            break;
                        case XmlPullParser.TEXT:
                            break;
                    }
                    eventType = xpp.next();
                }


            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (XmlPullParserException e) {
                throw new RuntimeException(e);
            }
            Log.i("thread","로딩스레드끝");
            new StartThread().start();
        }


    }
}


