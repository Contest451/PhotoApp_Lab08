package thanhdnh.ueh.edu.article_app;

import android.app.Activity;
import android.content.Context;
import android.widget.GridView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class UserData {

  public static UserList data;

  private Context context;
  private GridView gridview;

  public UserData(Context context, GridView gridview) {
    this.context = context;
    this.gridview = gridview;
  }

  public static UserProfile getUserFromId(int id) {

    for (int i = 0; i < data.getUsers().size(); i++) {

      if (data.getUsers().get(i).getId() == id) {
        return data.getUsers().get(i);
      }
    }

    return null;
  }

  public void loadData(Activity activity) {

    ArrayList<UserProfile> users =
            new ArrayList<UserProfile>();

    users.add(new UserProfile(
            1,
            "Nguyen Van An",
            "an@gmail.com",
            "Sinh vien CNTT, yeu thich lap trinh va cong nghe.",
            "https://i.pravatar.cc/300?img=1",
            "Coding, Music, Gaming"
    ));

    users.add(new UserProfile(
            2,
            "Tran Thi Binh",
            "binh@gmail.com",
            "Yeu thich thiet ke va chup anh.",
            "https://i.pravatar.cc/300?img=2",
            "Design, Photography, Travel"
    ));

    users.add(new UserProfile(
            3,
            "Le Van Cuong",
            "cuong@gmail.com",
            "Quan tam den kinh doanh va khoi nghiep.",
            "https://i.pravatar.cc/300?img=3",
            "Business, Reading, Football"
    ));

    users.add(new UserProfile(
            4,
            "Pham Thi Dung",
            "dung@gmail.com",
            "Yeu thich hoc ngoai ngu va du lich.",
            "https://i.pravatar.cc/300?img=4",
            "English, Travel, Cooking"
    ));

    users.add(new UserProfile(
            5,
            "Hoang Van Em",
            "em@gmail.com",
            "Sinh vien dam me the thao va cong nghe.",
            "https://i.pravatar.cc/300?img=5",
            "Football, Technology, Movies"
    ));

    users.add(new UserProfile(
            6,
            "Vo Thi Giang",
            "giang@gmail.com",
            "Yeu thich doc sach va viet blog.",
            "https://i.pravatar.cc/300?img=6",
            "Reading, Writing, Music"
    ));

    /*
     * Tao UserList tu du lieu tinh
     */
    UserList staticData = new UserList(users);

    /*
     * Dung Gson de chuyen object thanh JSON
     */
    Gson gson = new Gson();

    String json = gson.toJson(staticData);

    /*
     * Dung Gson doc JSON thanh UserList
     */
    data = gson.fromJson(json, UserList.class);

    /*
     * Dua du lieu vao Adapter
     */
    UserAdapter adapter =
            new UserAdapter(
                    data.getUsers(),
                    context
            );

    gridview.setAdapter(adapter);
  }
}