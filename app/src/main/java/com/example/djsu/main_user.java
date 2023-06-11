package com.example.djsu;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.djsu.admin.AdminMainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class main_user extends AppCompatActivity {
    // 햄버거 버튼 선언
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    // 프로필
    private TextView name, state,kcalText;
    private String profile, ID;
    private Bitmap bitmap;
    private ImageView ivImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String URL_UPLOAD = "http://enejd0613.dothome.co.kr/upload_profile.php";
    private static final String TAG = "main_user";

    // 유저 정보 출력UserId
    private static final String TAG_RESULTS = "result";
    private static final String TAG_Date = "date";
    private static final String TAG_FoodDate = "Date";
    private static final String TAG_FoodUserId  = "UserId";
    private static final String TAG_UserId  = "UserID";
    private static final String TAG_FoodKcal = "FoodKcal";
    private static final String TAG_water = "water";
    User user = new User();
    String myJSON;
    JSONArray peoples = null;
    // 물
    private TextView water;
    waterRequest waterRequest;
    RequestQueue queue;
    String date;

    // 걸음수
    private int count;
    int KcalNum = 0;

    // 공지
    String title;
    String detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        // 물 선언
        water = findViewById(R.id.water_tv);

        // 종하오빠가 뭐 해놓은거
        kcalText = findViewById(R.id.kcalText);
        date = getTime();
        // 물 + 100
        getData("http://enejd0613.dothome.co.kr/Waterlist.php");
        getFoodData("http://enejd0613.dothome.co.kr/foodcalendarlist.php");

        ImageButton plus = (ImageButton) findViewById(R.id.plusBtn);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = Integer.parseInt(water.getText().toString());
                count = count+100;
                water.setText(count+"");
            }
        });

        // 물 - 100
        ImageButton minus = (ImageButton) findViewById(R.id.minusBtn);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = Integer.parseInt(water.getText().toString());
                count = count-100;
                water.setText(count+"");
            }
        });

        // 음식 >
        ImageButton view_food = (ImageButton) findViewById(R.id.view_food);
        view_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (water.getText().toString().equals("0") == false) {
                    waterRequest = new waterRequest(ID,water.getText().toString(),date);
                    queue = Volley.newRequestQueue(main_user.this);
                    queue.add(waterRequest);
                    Intent intent = new Intent(getApplicationContext(), FoodAddActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), FoodAddActivity.class);
                    startActivity(intent);
                }
            }
        });

        // 공지
        Button announcement_btn = (Button) findViewById(R.id.announcement_btn);
        announcement_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (water.getText().toString().equals("0") == false) {
                    waterRequest = new waterRequest(ID,water.getText().toString(),date);
                    queue = Volley.newRequestQueue(main_user.this);
                    queue.add(waterRequest);
                    Intent Noticeintent = new Intent(getApplicationContext(), annoucement.class);
                    startActivity(Noticeintent);
                }else{
                    Intent Noticeintent = new Intent(getApplicationContext(), annoucement.class);
                    startActivity(Noticeintent);
                }
            }
        });

        // 프로필 선언
        User user = new User();

        name = findViewById(R.id.username); // 이름
        state = findViewById(R.id.Status_message_text); // 상태메시지
        ivImage = findViewById(R.id.imageView2); // 프로필 사진

        name.setText(user.getName()); // 이름
        state.setText(user.getState()); // 상태메시지
        ID = user.getId(); // 아이디
        profile = user.getProfile(); // 프로필 사진

        String imageUrl = profile; // Glide로 이미지 표시하기
        Glide.with(this).load(imageUrl).into(ivImage);

        // 햄버거
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //뒤로가기버튼 이미지 적용
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_hamburger);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        if (water.getText().toString().equals("0") == false) {
                            waterRequest = new waterRequest(ID,water.getText().toString(),date);
                            queue = Volley.newRequestQueue(main_user.this);
                            queue.add(waterRequest);
                            Intent intent = new Intent(getApplicationContext(), main_user.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getApplicationContext(), main_user.class);
                            startActivity(intent);
                        }
                        return true;
                    case R.id.calender:
                        if (water.getText().toString().equals("0") == false) {
                            waterRequest = new waterRequest(ID,water.getText().toString(),date);
                            queue = Volley.newRequestQueue(main_user.this);
                            queue.add(waterRequest);
                            Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    case R.id.communety:
                        if (water.getText().toString().equals("0") == false) {
                            waterRequest = new waterRequest(ID,water.getText().toString(),date);
                            queue = Volley.newRequestQueue(main_user.this);
                            queue.add(waterRequest);
                            Intent communetyintent = new Intent(getApplicationContext(), community.class);
                            startActivity(communetyintent);
                        }else{     Intent communetyintent = new Intent(getApplicationContext(), community.class);
                            startActivity(communetyintent);
                        }
                        return true;
                    case R.id.mypage:
                        if (water.getText().toString().equals("0") == false) {
                            waterRequest = new waterRequest(ID,water.getText().toString(),date);
                            queue = Volley.newRequestQueue(main_user.this);
                            queue.add(waterRequest);
                            Intent mypageintent = new Intent(getApplicationContext(), mypage.class);
                            startActivity(mypageintent);
                        }else{             Intent mypageintent = new Intent(getApplicationContext(), mypage.class);
                            startActivity(mypageintent);
                        }
                        return true;
                    case R.id.map:
                        if (water.getText().toString().equals("0") == false) {
                            waterRequest = new waterRequest(ID,water.getText().toString(),date);
                            queue = Volley.newRequestQueue(main_user.this);
                            queue.add(waterRequest);
                            Intent mapintent = new Intent(getApplicationContext(), map.class);
                            startActivity(mapintent);
                        }else{  Intent mapintent = new Intent(getApplicationContext(), map.class);
                            startActivity(mapintent);
                        }
                        return true;
                    case R.id.manbogi:
                        if (water.getText().toString().equals("0") == false) {
                            waterRequest = new waterRequest(ID,water.getText().toString(),date);
                            queue = Volley.newRequestQueue(main_user.this);
                            queue.add(waterRequest);
                            Intent manbogiintent = new Intent(getApplicationContext(), pedometer.class);
                            startActivity(manbogiintent);
                        }else{         Intent manbogiintent = new Intent(getApplicationContext(), pedometer.class);
                            startActivity(manbogiintent);
                        }
                        return true;
                    case R.id.annoucement:
                        if (water.getText().toString().equals("0") == false) {
                            waterRequest = new waterRequest(ID,water.getText().toString(),date);
                            queue = Volley.newRequestQueue(main_user.this);
                            queue.add(waterRequest);
                            Intent Noticeintent = new Intent(getApplicationContext(), annoucement.class);
                            startActivity(Noticeintent);
                        }else{
                            Intent Noticeintent = new Intent(getApplicationContext(), annoucement.class);
                            startActivity(Noticeintent);
                        }
                        return true;
                    case R.id.friend:
                        if (water.getText().toString().equals("0") == false) {
                            waterRequest = new waterRequest(ID,water.getText().toString(),date);
                            queue = Volley.newRequestQueue(main_user.this);
                            queue.add(waterRequest);
                            Intent friend = new Intent(getApplicationContext(), chatList.class);
                            startActivity(friend);
                        }else{
                            Intent friend = new Intent(getApplicationContext(), chatList.class);
                            startActivity(friend);
                        }
                        return true;
                }
                return false;
            }
        });
        // 상태메시지 변경
        TextView text1 = findViewById(R.id.Status_message_text);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                String UserID = user.getId();
                String text = text1.getText().toString();

                dialog_status alert = new dialog_status(main_user.this,text);
                alert.callFunction();
                alert.setModifyReturnListener(new dialog_status.ModifyReturnListener() {
                    @Override
                    public void afterModify(String text) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");

                                    if (success.equals("1")) { // 회원등록에 성공한 경우
                                        text1.setText(text);
                                        System.out.println("!!!!!!!!!!!!!: " +text);
                                        Toast.makeText(getApplicationContext(), "한줄소개가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                    } else { // 회원등록에 실패한 경우
                                        Toast.makeText(getApplicationContext(), "회원 정보 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        // 서버로 Volley를 이용해서 요청을 함.
                        StatusRequest stateRequest1 = new StatusRequest(UserID, text, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(main_user.this);
                        queue.add(stateRequest1);
                    }
                });
            }
        });

        // 프로필 사진 변경
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });

        fetchLatestAnnoncementTitle();

        // 공지 누르기
        TextView textView = findViewById(R.id.ann_txt);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 커스텀 다이얼로그를 생성하고 보여줍니다.
                showDialog();
            }

        });
    }

    private ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        ivImage.setImageBitmap(bitmap);
                        uploadPicture(ID, getStringImage(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    private void chooseFile()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            uploadPicture(ID, getStringImage(bitmap));
        }
    }

    private void uploadPicture(final String UserID, final String UserProfile)
    {
        // final ProgressDialog progressDialog = new ProgressDialog(this);
        // progressDialog.setMessage("Uploading...");
        // progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Log.e(TAG, response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1"))
                    {
                        // progressDialog.dismiss();
                        Toast.makeText(main_user.this, "프로필 사진을 성공적으로 변경했습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // progressDialog.dismiss();
                    Toast.makeText(main_user.this, "프로필 사진 변경에 실패했습니다. 다시 시도해주세요." + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // progressDialog.dismiss();
                Toast.makeText(main_user.this, "Error : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("UserID", UserID);
                params.put("UserProfile", UserProfile);

                return params;
            }
        };
        stringRequest.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(20000 ,
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public String getStringImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        return encodedImage;
    }

    // 햄버거
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void showFoodList() {
        try {
            if (myJSON != null && !myJSON.isEmpty()) {
                JSONObject jsonObj = new JSONObject(myJSON);
                peoples = jsonObj.getJSONArray(TAG_RESULTS);

                for(int i = 0;i < peoples.length(); i++) {
                    JSONObject c = peoples.getJSONObject(i);
                    String UserId = c.getString(TAG_FoodUserId);
                    String Date = c.getString(TAG_FoodDate);
                    if(UserId.equals(user.getId())) {
                        if(date.equals(Date)) {
                            String FoodKcal = c.getString(TAG_FoodKcal);
                            KcalNum += Integer.parseInt(FoodKcal);
                            kcalText.setText(String.valueOf(KcalNum));
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getFoodData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showFoodList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
    protected void showList() {
        try {
            if (myJSON != null && !myJSON.isEmpty()) {
                JSONObject jsonObj = new JSONObject(myJSON);
                peoples = jsonObj.getJSONArray(TAG_RESULTS);

                for(int i = 0;i < peoples.length(); i++) {
                    JSONObject c = peoples.getJSONObject(i);
                    String UserId = c.getString(TAG_UserId);
                    String Date = c.getString(TAG_Date);
                    if(UserId.equals(user.getId())) {
                        if(date.equals(Date)) {
                            String Water = c.getString(TAG_water);
                            water.setText(Water);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
    // 시간
    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = dateFormat.format(date);

        return getTime;
    }

    // 공지 불러오기
    private void fetchLatestAnnoncementTitle() {
        String url = "http://enejd0613.dothome.co.kr/fetch_announcement.php";  // PHP 파일의 URL을 여기에 입력하세요.

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            title = response.getString("title");
                            detail = response.getString("detail");

                            TextView textView = findViewById(R.id.ann_txt);
                            textView.setText(title);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Volley 요청을 큐에 추가합니다.
        Volley.newRequestQueue(this).add(request);
    }

    // 공지 커스텀다이얼로그
    private void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(main_user.this);

        // 커스텀 다이얼로그 레이아웃을 inflate하여 설정
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_announcement, null);
        dialogBuilder.setView(dialogView);

        // 커스텀 다이얼로그 내부의 뷰 요소를 찾아서 설정
        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        TextView contentTextView = dialogView.findViewById(R.id.dialog_content);

        // TextView에 받아온 데이터를 설정
        titleTextView.setText(title);
        contentTextView.setText(detail);

        // 커스텀 다이얼로그를 생성하고 보여주기
        AlertDialog customDialog = dialogBuilder.create();
        customDialog.show();
    }

}
