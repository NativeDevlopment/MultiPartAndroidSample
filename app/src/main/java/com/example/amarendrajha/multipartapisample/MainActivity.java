package com.example.amarendrajha.multipartapisample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image_view);
        // width and height will be at least 600px long (optional).
        ImagePicker.setMinQuality(600, 600);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File bitmap = ImagePicker.getTemporalFile(this);

        if (bitmap != null) {
            Log.e("file name",bitmap.getAbsolutePath());
            new MultiPartAsync(bitmap).execute();
           // imageView.setImageBitmap(bitmap);
        }
    }

    public void onPickImage(View view) {
        ImagePicker.pickImage(this, "Select your image:");
    }
    public class MultiPartAsync extends AsyncTask<Void, Void, String> {

        private File file;
        private String message, tlastmsgId;
        private File thumbnail;





        public MultiPartAsync(File file) {
            this.file = file;

            Log.e("", "<<<<<<CALLL SERVICE PRE>>>>");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String servicesResponse = null;
            try {
                Log.e("", "<<<<<<CALLL SERVICE BACK>>>");
                servicesResponse = callUpload(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return servicesResponse;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.e("", "<<<<<<ON PROGRESS>>>>UPDATE" + values.toString());
        }

        @Override
        protected void onPostExecute(String serviceResponse) {
            super.onPostExecute(serviceResponse);
            if (serviceResponse != null)

               Log.e("response",serviceResponse);

                }
            // Log.e("RESPONSE>>>>>>>>>", ""+serviceResponse.toString());

        }

    String url = "http://172.16.10.20:8081/Api/uploadFile"; /*change this ip address to your system Address*/

    private String callUpload(File file ) throws Exception {

        MultipartUtility reqEntity = new MultipartUtility(url, "utf-8",new MultipartUtility.FileUploadListener() {

            @Override
            public void onUpdateProgress(int percentage, long kb) {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean isCanceled() {
                // TODO Auto-generated method stub
                return false;
            }
        });

/*
        reqEntity.addFormField("userId", array.getJSONObject(0).getString("userId"));
*/

        if (file != null)
        {

                reqEntity.addFilePart("file", file);
            }


        String response = reqEntity.finish();
        //   Log.e("server", "SERVER REPLIED:" + response);

        return response;
    }


}
