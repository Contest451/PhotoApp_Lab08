package thanhdnh.ueh.edu.article_app;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Downloader {

    public static String cached_file_path = "";

    public interface DownloadCallback {
        void onSuccess(File file);
        void onFailure();
    }

    public static void downloadWithProgress(
            String inputurl,
            Handler mainHandler,
            Context context,
            File where2store,
            ProgressBar progressBar,
            ImageView imageView) {

        downloadWithProgress(
                inputurl,
                mainHandler,
                context,
                where2store,
                progressBar,
                imageView,
                null
        );
    }

    public static void downloadWithProgress(
            String inputurl,
            Handler mainHandler,
            Context context,
            File where2store,
            ProgressBar progressBar,
            ImageView imageView,
            DownloadCallback callback) {

        OkHttpClient client =
                new OkHttpClient();

        Request request =
                new Request.Builder()
                        .url(inputurl)
                        .build();

        client.newCall(request).enqueue(
                new Callback() {

                    @Override
                    public void onFailure(
                            Call call,
                            IOException e) {

                        if (mainHandler != null) {

                            mainHandler.post(
                                    new Runnable() {

                                        @Override
                                        public void run() {

                                            if (progressBar != null) {
                                                progressBar.setVisibility(
                                                        ProgressBar.INVISIBLE
                                                );
                                            }

                                            if (callback != null) {
                                                callback.onFailure();
                                            }
                                        }
                                    }
                            );
                        }
                    }

                    @Override
                    public void onResponse(
                            Call call,
                            Response response) {

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            if (mainHandler != null) {

                                mainHandler.post(
                                        new Runnable() {

                                            @Override
                                            public void run() {

                                                if (callback != null) {
                                                    callback.onFailure();
                                                }
                                            }
                                        }
                                );
                            }

                            return;
                        }

                        long totalBytes =
                                response.body().contentLength();

                        InputStream inputStream =
                                response.body().byteStream();

                        try {

                            String extension = ".json";

                            String contentType =
                                    response.header(
                                            "Content-Type",
                                            ""
                                    );

                            if (contentType.contains("image/jpeg")) {
                                extension = ".jpg";
                            }

                            if (contentType.contains("image/png")) {
                                extension = ".png";
                            }

                            if (contentType.contains("application/json")) {
                                extension = ".json";
                            }

                            File file =
                                    new File(
                                            where2store,
                                            "downloaded_file" + extension
                                    );

                            OutputStream outputStream =
                                    new FileOutputStream(file);

                            byte[] buffer =
                                    new byte[1024];

                            long downloadedBytes = 0;

                            int bytesRead;

                            while ((bytesRead =
                                    inputStream.read(buffer)) != -1) {

                                outputStream.write(
                                        buffer,
                                        0,
                                        bytesRead
                                );

                                downloadedBytes += bytesRead;

                                if (totalBytes > 0) {

                                    final int progress =
                                            (int) (
                                                    downloadedBytes * 100
                                                            / totalBytes
                                            );

                                    if (mainHandler != null
                                            && progressBar != null) {

                                        mainHandler.post(
                                                new Runnable() {

                                                    @Override
                                                    public void run() {

                                                        progressBar.setProgress(
                                                                progress
                                                        );
                                                    }
                                                }
                                        );
                                    }
                                }
                            }

                            outputStream.flush();
                            outputStream.close();
                            inputStream.close();

                            cached_file_path =
                                    file.getAbsolutePath();

                            if (mainHandler != null) {

                                mainHandler.post(
                                        new Runnable() {

                                            @Override
                                            public void run() {

                                                if (progressBar != null) {
                                                    progressBar.setVisibility(
                                                            ProgressBar.INVISIBLE
                                                    );
                                                }

                                                if (imageView != null) {

                                                    imageView.setImageURI(
                                                            Uri.parse(
                                                                    cached_file_path
                                                            )
                                                    );
                                                }

                                                if (callback != null) {
                                                    callback.onSuccess(file);
                                                }
                                            }
                                        }
                                );
                            }

                        } catch (Exception e) {

                            e.printStackTrace();

                            if (mainHandler != null) {

                                mainHandler.post(
                                        new Runnable() {

                                            @Override
                                            public void run() {

                                                if (callback != null) {
                                                    callback.onFailure();
                                                }
                                            }
                                        }
                                );
                            }
                        }
                    }
                }
        );
    }
}