package thanhdnh.ueh.edu.article_app;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.GridView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UserData {

  public static UserList data;

  private Context context;
  private GridView gridview;

  public UserData(
          Context context,
          GridView gridview) {

    this.context = context;
    this.gridview = gridview;
  }

  public static UserProfile getUserFromId(int id) {

    if (data == null) {
      return null;
    }

    for (int i = 0;
         i < data.getUsers().size();
         i++) {

      if (data.getUsers()
              .get(i)
              .getId() == id) {

        return data.getUsers().get(i);
      }
    }

    return null;
  }

  public void loadData(
          String url,
          Activity activity) {

    final Handler mainHandler =
            new Handler(
                    activity.getMainLooper()
            );

    File cacheFolder =
            context.getCacheDir();

    Downloader.downloadWithProgress(
            url,
            mainHandler,
            context,
            cacheFolder,
            null,
            null,
            new Downloader.DownloadCallback() {

              @Override
              public void onSuccess(
                      File file) {

                try {

                  Gson gson =
                          new Gson();

                  data =
                          gson.fromJson(
                                  readText(file),
                                  UserList.class
                          );

                  UserAdapter adapter =
                          new UserAdapter(
                                  data.getUsers(),
                                  context
                          );

                  gridview.setAdapter(
                          adapter
                  );

                } catch (Exception e) {

                  e.printStackTrace();
                }
              }

              @Override
              public void onFailure() {

              }
            }
    );
  }

  public String readText(File file) {

    BufferedReader reader = null;

    try {

      InputStream stream =
              new FileInputStream(file);

      reader =
              new BufferedReader(
                      new InputStreamReader(stream)
              );

      StringBuffer buffer =
              new StringBuffer();

      String line = "";

      while ((line =
              reader.readLine()) != null) {

        buffer.append(
                line + "\n"
        );
      }

      reader.close();

      return buffer.toString();

    } catch (Exception e) {

      e.printStackTrace();
    }

    return "";
  }
}