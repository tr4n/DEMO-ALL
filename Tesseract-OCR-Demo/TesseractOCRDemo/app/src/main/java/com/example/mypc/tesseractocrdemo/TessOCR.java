package com.example.mypc.tesseractocrdemo;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import static android.content.ContentValues.TAG;

public class TessOCR {
    public TessBaseAPI tessBaseAPI = null;
    public final String TESS_DATA = "/tessdata";
    public final String PATH_DATA = Environment.getExternalStorageDirectory().toString()+ "/Tess";

    public TessOCR(TessBaseAPI tessBaseAPI) {
        this.tessBaseAPI = tessBaseAPI;
    }



    public TessOCR() {
        this.tessBaseAPI = new TessBaseAPI();
        String dataPath = Environment.getExternalStorageDirectory().toString() + "/tesseract";
        String language = "eng";

        File dir = new File(dataPath, "tessdata");
        if(!dir.exists()){
            dir.mkdirs();
        }

        Log.d(TAG, "TessOCR: " + dir.getAbsolutePath().toString());

        tessBaseAPI.init(dataPath, language);
       File[] files = (new File(Environment.getExternalStorageDirectory() + "/tesseract/tessdata" )).listFiles();

        if(files != null)
        for(File subFile: files){
            Log.d(TAG, "TessOCR: " + subFile.getName() );
        }
    }

    public String getOCRResult(Bitmap bitmap) {
        if(bitmap == null) return "nothing";
        tessBaseAPI.setImage(bitmap);
        String result = tessBaseAPI.getUTF8Text();
        return result;
    }






}
