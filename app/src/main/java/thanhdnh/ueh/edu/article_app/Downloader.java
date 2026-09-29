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

  public static void downloadWithProgress(
          String inputurl,
          Handler mainHandler,
          Context context,
          File where2store,
          ProgressBar progressBar,
          ImageView imageView) {

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

                mainHandler.post(
                        new Runnable() {

                          @Override
                          public void run() {

                            progressBar.setVisibility(
                                    ProgressBar.INVISIBLE
                            );
                          }
                        }
                );
              }

              @Override
              public void onResponse(
                      Call call,
                      Response response) {

                if (!response.isSuccessful()) {
                  return;
                }

                long totalBytes =
                        response.body().contentLength();

                InputStream inputStream =
                        response.body().byteStream();

                try {

                  File file =
                          File.createTempFile(
                                  "user_avatar",
                                  ".jpg",
                                  where2store
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

                    downloadedBytes +=
                            bytesRead;

                    if (totalBytes > 0) {

                      int progress =
                              (int) (
                                      (downloadedBytes * 100)
                                              / totalBytes
                              );

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

                  outputStream.flush();
                  outputStream.close();
                  inputStream.close();

                  cached_file_path =
                          file.getAbsolutePath();

                  mainHandler.post(
                          new Runnable() {

                            @Override
                            public void run() {

                              imageView.setImageURI(
                                      Uri.parse(
                                              cached_file_path
                                      )
                              );

                              progressBar.setVisibility(
                                      ProgressBar.INVISIBLE
                              );
                            }
                          }
                  );

                } catch (Exception e) {

                  e.printStackTrace();
                }
              }
            }
    );
  }
}