package thanhdnh.ueh.edu.article_app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class ViewUserActivity extends AppCompatActivity {

  ImageView iv_avatar;

  TextView tv_id;
  TextView tv_username;
  TextView tv_email;
  TextView tv_desc;
  TextView tv_hoppy;

  ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_view_user);

    getSupportActionBar().hide();

    iv_avatar = findViewById(R.id.iv_avatar);

    tv_id = findViewById(R.id.tv_id);
    tv_username = findViewById(R.id.tv_username);
    tv_email = findViewById(R.id.tv_email);
    tv_desc = findViewById(R.id.tv_desc);
    tv_hoppy = findViewById(R.id.tv_hoppy);

    progressBar = findViewById(R.id.progressBar);

    int id = (int) getIntent().getLongExtra("id", 0);

    UserProfile user =
            UserData.getUserFromId(id);

    if (user != null) {

      tv_id.setText(
              "ID: " + user.getId()
      );

      tv_username.setText(
              "Username: " + user.getUsername()
      );

      tv_email.setText(
              "Email: " + user.getEmail()
      );

      tv_desc.setText(
              "Description: " + user.getDesc()
      );

      tv_hoppy.setText(
              "Hoppy: " + user.getHoppy()
      );

      progressBar.setProgress(0);
      progressBar.setVisibility(
              ProgressBar.VISIBLE
      );

      Handler handler =
              new Handler(Looper.getMainLooper());

      Downloader.downloadWithProgress(
              user.getAvatar_url(),
              handler,
              this,
              getCacheDir(),
              progressBar,
              iv_avatar
      );
    }
  }
}