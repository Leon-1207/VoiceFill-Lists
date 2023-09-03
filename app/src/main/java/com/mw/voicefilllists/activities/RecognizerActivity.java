package com.mw.voicefilllists.activities;

import static android.widget.Toast.makeText;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class RecognizerActivity extends AppCompatActivity implements
        RecognitionListener {

    protected static final String LOG_TAG = "RecognizerActivity";
    protected static final String PHONE_SEARCH = "phones";
    protected static final String KWS_SEARCH = "weakup";
    protected static final String KEYPHRASE = "Fareed";

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    protected SpeechRecognizer recognizer;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        // Check if user has given permission to record audio
        int permissionCheck = this.checkSelfPermission(Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};
            this.requestPermissions(permissions, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new SetupTask(this).execute();
    }

    private static class SetupTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<RecognizerActivity> activityReference;

        SetupTask(RecognizerActivity activity) {
            this.activityReference = new WeakReference<>(activity);
        }

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                activityReference.get().onStartSetup();
                Assets assets = new Assets(activityReference.get());
                File assetDir = assets.syncAssets();
                activityReference.get().setupRecognizer(assetDir);
            } catch (IOException e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception result) {
            String msg = "";
            if (result != null) {
                msg = "Failed to init recognizer " + result;
            } else {
                msg = "Recognizer is ready";
            }
            Log.i(LOG_TAG, msg);
            activityReference.get().onEndSetup();
        }
    }

    public void onStartSetup() {
    }

    public void onEndSetup() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Recognizer initialization is a time-consuming and it involves IO,
                // so we execute it in async task
                new SetupTask(this).execute();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }

    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {

    }

    @Override
    public void onBeginningOfSpeech() {
    }

    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {
    }

    protected void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them
        File dictionary = new File(assetsDir, "voxforge.dic");
        //LanguageModelModifier languageModelModifier = new LanguageModelModifier(dictionary);
        //System.out.println("1 " + languageModelModifier.isWordInDictionary("Fareed"));
        //languageModelModifier.addWordToDictionary("Fareed", "F AH N IY R TH");
        //System.out.println("2 " + languageModelModifier.isWordInDictionary("Fareed"));

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "de-de-ptm"))
                .setDictionary(dictionary)
                // .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                .getRecognizer();
        recognizer.addListener(this);

        /* In your application you might not need to add all those searches.
          They are added here for demonstration. You can leave just one.
         */

        // Phonetic search
        File phoneticModel = new File(assetsDir, "voxforge.phone");
        recognizer.addAllphoneSearch(PHONE_SEARCH, phoneticModel);

        /*
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);


        String dynamicGrammar = "#JSGF V1.0;\n" +
                "grammar dynamic;\n" +
                "public <greeting> = hello Fareed ;\n" +
                "public <command> = <greeting> ;";

        recognizer.addGrammarSearch("grammar", dynamicGrammar);
         */
    }

    @Override
    public void onError(Exception error) {
        Log.e(LOG_TAG, error.getMessage());
    }

    @Override
    public void onTimeout() {
        //
    }

    public void startListening() {
        recognizer.startListening(PHONE_SEARCH);
    }

    public void stopListening() {
        recognizer.stop();
    }

    public SpeechRecognizer getRecognizer() {
        return recognizer;
    }
}