package com.example.wordpronouncer;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextToSpeech textToSpeech;
    private String[] listPitchRatio = new String[]{"Normal", "Slow", "Fast"};
    private SharedPreferences sharedPreferences;
    private String utterence = "";
    private ToggleButton toggleButton;
    private CoordinatorLayout coordinatorLayout;
    private Spinner spinnerAccent,spinnerPitch;
    final static String filePath = Environment.getExternalStorageDirectory() + "/Learn_Pronunciation/";
    private boolean isAccentAvailable = false;
    private TextView selectedAccentTextView, selectedPitchTextView;
    private ArrayList<Locale_Name_Language> ttsAvailableLanguagesList = new ArrayList<>();
    private AlertDialog alertDialog;
    private int CHECK_TTS_CODE=1100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE);

        /*SharedPreferences.Editor editor1 =sharedPreferences.edit();
        editor1.clear();
        editor1.apply();*/

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        TextView label = findViewById(R.id.label0);
        TextView label1 = findViewById(R.id.label1);
        TextView language_Accent_Label = findViewById(R.id.language_accent_label);
        TextView pitchLabel = findViewById(R.id.pitch_label);
        TextView saveLabel = findViewById(R.id.save_label);
        toggleButton = findViewById(R.id.save_btn);
        editText = findViewById(R.id.edittext);
        spinnerAccent = findViewById(R.id.spinner);
        spinnerPitch = findViewById(R.id.pitch_spinner);
        ImageView help = findViewById(R.id.btn_help);
        ImageView about = findViewById(R.id.btn_info);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        final TextView settings = findViewById(R.id.heading_settings);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        final ImageButton floatingBtn = findViewById(R.id.floating_btn);
        selectedAccentTextView = findViewById(R.id.textview_selected_accent);
        selectedPitchTextView = findViewById(R.id.textview_selected_pitch);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-Bold.ttf");
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-Medium.ttf");
        Typeface ubuntu_regular = Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-Regular.ttf");

        editText.setTypeface(ubuntu_regular);
        label.setTypeface(typeface);
        label1.setTypeface(typeface);
        language_Accent_Label.setTypeface(typeface1);
        pitchLabel.setTypeface(typeface1);
        saveLabel.setTypeface(typeface1);
        toggleButton.setTypeface(typeface1);
        settings.setTypeface(typeface);
        selectedAccentTextView.setTypeface(ubuntu_regular);
        selectedPitchTextView.setTypeface(ubuntu_regular);

        spinnerAccent.setVisibility(View.INVISIBLE);
        selectedAccentTextView.setVisibility(View.INVISIBLE);
        spinnerPitch.setVisibility(View.INVISIBLE);
        selectedPitchTextView.setVisibility(View.INVISIBLE);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, CHECK_TTS_CODE);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment_help fragment_help = new Fragment_help();
                fragment_help.show(fragmentManager,"");
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment_About_Container fragment_about_container = new Fragment_About_Container();
                fragment_about_container.show(fragmentManager,"");
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            // requesting write external storage permission
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                            toggleButton.setChecked(false);
                        } else {
                            //permission already granted
                            toggleButton.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
                            toggleButton.setChecked(true);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isSaveEnabled", true);
                            editor.apply();
                        }
                    } else {
                        // build version less than Marshmallow, no need to check permissions
                        toggleButton.setChecked(true);
                        toggleButton.setTextColor(getResources().getColor(android.R.color.white));
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isSaveEnabled", true);
                        editor.apply();
                    }
                } else {
                    toggleButton.setTextColor(getResources().getColor(android.R.color.black));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isSaveEnabled", false);
                    editor.apply();
                }
            }
        });

        try {
            utterence = sharedPreferences.getString("utterence", "");

            // checking if the string variable is empty in shared preferences
            // create a new value in shared preferences and initialize it from 0 as utterence count for tts
            if (Objects.requireNonNull(utterence).isEmpty()) {
                // this code will work for the first time only
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("utterence", "0");
                editor.apply();
                utterence = "0";
            } else {
                // getting utterence value from shared preference
                // if it is already stored there
                utterence = sharedPreferences.getString("utterence", "");
            }

            // checking if this boolean variable is true in shared preferences,
            // make toggle button checked
            if (sharedPreferences.getBoolean("isSaveEnabled", false)) {
                toggleButton.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
                toggleButton.setChecked(true);
            } else {
                toggleButton.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.black));
                toggleButton.setChecked(false);
            }
        } catch (Exception ignored) {
            // handling exception to create some fields in shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("utterence", "0");
            editor.putBoolean("isSaveEnabled", false);
            editor.apply();
            utterence = "0";
        }

        //Floating Button below
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        //checking device is connected to Internet or not
        // It is necessary functionality for downloading different languages in Text-to-Speech engine.
        new CheckInternetConnectivity().execute();
        // end of onCreate()
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == CHECK_TTS_CODE){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                // Initializing Text to Speech engine.
                textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            textToSpeech.setLanguage(Locale.getDefault());
                            textToSpeech.setOnUtteranceProgressListener(utteranceProgressListener);

                            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item_layout, listPitchRatio);
                            adapter1.setDropDownViewResource(android.R.layout.simple_list_item_checked);
                            spinnerPitch.setAdapter(adapter1);
                            spinnerPitch.setVisibility(View.VISIBLE);
                            selectedPitchTextView.setVisibility(View.VISIBLE);

                            spinnerPitch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String item = parent.getItemAtPosition(position).toString();
                                    if (item.equalsIgnoreCase("slow")) {
                                        textToSpeech.setSpeechRate(0.5f);
                                        selectedPitchTextView.setText("Speed: Slow");
                                    } else if (item.equalsIgnoreCase("normal")) {
                                        textToSpeech.setSpeechRate(1.0f);
                                        selectedPitchTextView.setText("Speed: Normal");
                                    } else {
                                        textToSpeech.setSpeechRate(2.0f);
                                        selectedPitchTextView.setText("Speed: Fast");
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                            getAvailableLanguagesTts();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Failed to initialise TTS",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else{
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    public void pronounceWord(View v) {

        if (editText.getText().toString().trim().length() == 0) {
            editText.setBackground(getResources().getDrawable(R.drawable.edittext_bg_error));
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(editText, "translationX", 30.0f, -30.0f, 0.0f);
            objectAnimator.setDuration(200);
            objectAnimator.setRepeatCount(3);
            objectAnimator.setInterpolator(new LinearInterpolator());
            objectAnimator.start();
        } else {
            // Toast.makeText(getApplicationContext(),utterence,Toast.LENGTH_SHORT).show();
            editText.setBackground(getResources().getDrawable(R.drawable.edittext_bg_correct));
            String word = editText.getText().toString();

            // getting previously stored utterenceID from shared preference and incrementing it by 1
            // due to utterenceID should be unique every time TTS speaking.
            int temp = Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString("utterence", null)));
            temp += 1;
            utterence = String.valueOf(temp);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("utterence", utterence);
            editor.apply();
            textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, utterence);
        }
    }

    public void viewList(View v) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // requesting write external storage permission
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
            } else {
                //permission already granted
                startActivity(new Intent(MainActivity.this, saved_audio_list.class));
            }
        } else {
            // build version less than Marshmallow, no need to check permissions
            startActivity(new Intent(MainActivity.this, saved_audio_list.class));
        }
    }

    private UtteranceProgressListener utteranceProgressListener = new UtteranceProgressListener() {

        @Override
        public void onStart(String utteranceId) {

        }

        @Override
        public void onDone(final String utteranceId) {

            if (sharedPreferences.getBoolean("isSaveEnabled", false)) {
                // autosave button is selected so save every audio from tts
                // if selected accent is available, then save else dont save
                if (isAccentAvailable) {
                    saveAudio(utteranceId);
                }
            }
        }

        @Override
        public void onError(String utteranceId) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                toggleButton.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
                toggleButton.setChecked(true);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isSaveEnabled", true);
                editor.apply();
            } else {
                // permission denied by user
                toggleButton.setTextColor(ContextCompat.getColor(MainActivity.this, android.R.color.black));
                toggleButton.setChecked(false);
            }
        } else if (requestCode == 102) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted
                startActivity(new Intent(MainActivity.this, saved_audio_list.class));
            }
        }
    }

    private void saveAudio(String id) {

        File path = new File(filePath);
        File file = new File(filePath + "/" + editText.getText().toString() + "_" + spinnerAccent.getSelectedItem().toString() + ".mp3");

        try {
            if (!path.isDirectory()) {
                path.mkdirs();
            }

            if (!file.exists()) {
                textToSpeech.synthesizeToFile(editText.getText().toString(), null, file, id);
                Snackbar.make(coordinatorLayout, "Audio Saved", Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception ignored) {
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class CheckInternetConnectivity extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                HttpURLConnection urlConnection = (HttpURLConnection) (new URL("https://www.who.int/").openConnection());
                urlConnection.setRequestProperty("User-Agent", "Android");
                urlConnection.setRequestProperty("Connection", "close");
                urlConnection.setConnectTimeout(1500);
                urlConnection.connect();
                return (urlConnection.getResponseCode() == 200);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);

            if(!b){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Alert!!!");
                builder.setMessage("Internet connection is required to access some core functionality.");
                builder.setCancelable(false);
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(alertDialog!=null && alertDialog.isShowing()){
                            alertDialog.dismiss();
                        }
                        new CheckInternetConnectivity().execute();
                    }
                });
                builder.setNegativeButton("Skip once", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(alertDialog!=null && alertDialog.isShowing()){
                            alertDialog.dismiss();
                        }
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
            else{
                if(alertDialog!=null && alertDialog.isShowing()){
                    alertDialog.dismiss();
                }
            }
        }
    }

    public void getAvailableLanguagesTts(){

        try{
            Set<Locale> availableLanguages = textToSpeech.getAvailableLanguages();
            for (Locale loc : availableLanguages) {
                ttsAvailableLanguagesList.add(new Locale_Name_Language(loc.getDisplayLanguage(), loc));
            }

            Collections.sort(ttsAvailableLanguagesList, new Comparator<Locale_Name_Language>() {
                @Override
                public int compare(Locale_Name_Language o1, Locale_Name_Language o2) {
                    return o1.getLocaleName().compareToIgnoreCase(o2.getLocaleName());
                }
            });

            ArrayList<String> listt = new ArrayList<>();
            for (int i = 0; i < ttsAvailableLanguagesList.size(); i++) {
                listt.add(ttsAvailableLanguagesList.get(i).getLocaleName());
            }

            Collections.sort(listt);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item_layout, listt);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
            spinnerAccent.setAdapter(adapter);
            spinnerAccent.setVisibility(View.VISIBLE);
            selectedAccentTextView.setVisibility(View.VISIBLE);

            spinnerAccent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (textToSpeech.isLanguageAvailable(ttsAvailableLanguagesList.get(position).getLocaleLanguage()) >= TextToSpeech.LANG_AVAILABLE) {
                        textToSpeech.setLanguage(ttsAvailableLanguagesList.get(position).getLocaleLanguage());
                        isAccentAvailable = true;
                        selectedAccentTextView.setText("Accent: " + ttsAvailableLanguagesList.get(position).getLocaleName());
                    } else {
                        textToSpeech.setLanguage(Locale.getDefault());
                        Toast.makeText(getApplicationContext(), "Accent not available", Toast.LENGTH_SHORT).show();
                        isAccentAvailable = false;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        catch(Exception ignored){

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(textToSpeech!=null){
            textToSpeech.shutdown();
        }
    }

    // end of class
}