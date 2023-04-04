package com.example.djsu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FoodUpdate extends AppCompatActivity {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    EditText NameText,KcalText,CarbohydratText,ProteinText,FatText,SodiumText,SugarText,KgText,DateText,setText,searchText;
    String Namestr,Kcalstr,Carbohydratestr,Proteinstr,Fatstr,Sodiumstr,Sugarstr,Kgstr,Datestr;
    String s = "0",Time;
    Double setSum;
    ImageButton searchBtn;
    Double KcalSum,CarbohydratSum, ProteinSum, FatSum ,SodiumSum,SugarSum,KgSum;
    private List<User> userList;
    private FoodAddAdapter userFoodAdapter;
    DatePickerDialog datePickerDialog;
    Bundle extras;
    Double kgSum;
    int Cood,FcCode;
    private Button addButton,backButton;
    String Date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_update);
        extras = getIntent().getExtras();
        Date = extras.getString("Date");
        Datestr = Date;
        userList = new ArrayList<>();
        userFoodAdapter = new FoodAddAdapter(FoodUpdate.this, userList);
        ListView userfoodListView = (ListView) findViewById(R.id.FoodView);
        userfoodListView.setAdapter(userFoodAdapter);
        Intent intent = getIntent();
        User user1 = new User();
        try {
            userFoodAdapter.notifyDataSetChanged();
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("UserFood"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0, FcCode;
            String eatingTime, Date, UserID, FoodName, FoodKcal, FoodCarbohydrate, FoodProtein, FoodFat, FoodSodium, FoodSugar, FoodKg;
            //JSON 배열 길이만큼 반복문을 실행
            while (count < jsonArray.length()) {
                //count는 배열의 인덱스를 의미f
                JSONObject object = jsonArray.getJSONObject(count);
                Date = object.getString("Date");
                FoodName = object.getString("FoodName");
                FoodKcal = object.getString("FoodKcal");
                FoodCarbohydrate = object.getString("FoodCarbohydrate");
                FoodProtein = object.getString("FoodProtein");
                FoodFat = object.getString("FoodFat");
                FoodSodium = object.getString("FoodSodium");
                FoodSugar = object.getString("FoodSugar");
                FoodKg = object.getString("FoodKg");
                eatingTime = object.getString("eatingTime");
                FcCode = object.getInt("FcCode");
                //값들을 User클래스에 묶어줍니다
                User user = new User(Date, FoodName, eatingTime, FoodKcal, FoodCarbohydrate, FoodProtein, FoodFat, FoodSodium, FoodSugar, FoodKg, FcCode);
                UserID = object.getString("UserID");
                if (UserID.equals(user1.getId())) {
                    if (Datestr.equals(Date)) {
                        userList.add(user);//리스트뷰에 값을 추가해줍니다
                    }
                }
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        searchBtn = (ImageButton) findViewById(R.id.SearchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FoodBackgroundTask().execute();
            }
        });
        searchText = (EditText) findViewById(R.id.searchText);
        ImageButton calendarBtn = findViewById(R.id.calendarbtn);
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //오늘 날짜(년,월,일) 변수에 담기
                Calendar calendar = Calendar.getInstance();
                int pYear = calendar.get(Calendar.YEAR); //년
                int pMonth = calendar.get(Calendar.MONTH);//월
                int pDay = calendar.get(Calendar.DAY_OF_MONTH);//일

                datePickerDialog = new DatePickerDialog(FoodUpdate.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                //1월은 0부터 시작하기 때문에 +1을 해준다.
                                month = month + 1;
                                Date = year + "-" + month + "-" + day;
                                DateText.setText(Date);
                            }
                        }, pYear, pMonth, pDay);
                datePickerDialog.show();
            } //onClick
        });

        addButton = findViewById(R.id.addBtn);
        backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UserFoodBackgroundTask1(Date).execute();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                UserFoodUpdate UpdateRequest = new UserFoodUpdate(Date, NameText.getText().toString(), KcalText.getText().toString(), CarbohydratText.getText().toString(), ProteinText.getText().toString()
                        , FatText.getText().toString(), SodiumText.getText().toString(), SugarText.getText().toString(), KgText.getText().toString(), Time, FcCode,setText.getText().toString(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(FoodUpdate.this);
                queue.add(UpdateRequest);
                Toast.makeText(getApplicationContext(), "음식수정이 되었습니다.", Toast.LENGTH_SHORT).show();
                new UserFoodBackgroundTask1(Date).execute();
            }

        });

        String Name = "";
        String Kcal = "";
        String Carbohydrate = "";
        String Protein = "";
        String Fat = "";
        String Sodium = "";
        String Sugar = "";
        String Kg = "";
        int quantity;

        quantity = extras.getInt("quantity");
        Name = extras.getString("FoodName");
        Kcal = extras.getString("FoodKcal");
        Carbohydrate = extras.getString("FoodCarbohydrate");
        Protein = extras.getString("FoodProtein");
        Fat = extras.getString("FoodFat");
        Sodium = extras.getString("FoodSodium");
        Sugar = extras.getString("FoodSugar");
        Kg = extras.getString("FoodKg");
        Cood = extras.getInt("FoodCood");
        Time = extras.getString("Time");
        FcCode = extras.getInt("FcCode");
        NameText = (EditText) findViewById(R.id.nametext);
        KcalText = (EditText) findViewById(R.id.kcaltext);
        CarbohydratText = (EditText) findViewById(R.id.Carbohydratetext);
        ProteinText = (EditText) findViewById(R.id.Protintext);
        FatText = (EditText) findViewById(R.id.Fattext);
        SodiumText = (EditText) findViewById(R.id.Sodiumtext);
        SugarText = (EditText) findViewById(R.id.Sugartext);
        KgText = (EditText) findViewById(R.id.Kgtext);
        DateText = (EditText) findViewById(R.id.DateText);
        setText = (EditText) findViewById(R.id.settext);
        setText.setText(String.valueOf(quantity));
        Namestr = Name;
        Kcalstr = Kcal;
        Carbohydratestr = Carbohydrate;
        Proteinstr = Protein;
        Fatstr = Fat;
        Sodiumstr = Sodium;
        Sugarstr = Sugar;
        Kgstr = Kg;
        kgSum =  Double.valueOf(Kgstr);
        NameText.setText(Namestr);
        KcalText.setText(Kcalstr);
        CarbohydratText.setText(Carbohydratestr);
        ProteinText.setText(Proteinstr);
        FatText.setText(Fatstr);
        SodiumText.setText(Sodiumstr);
        SugarText.setText(Sugarstr);
        KgText.setText(Kgstr);
        DateText.setText(Datestr);
        setSum =  Double.valueOf(quantity);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.BreakFast) {
                    s = "아침";
                } else if (id == R.id.BreakFastTo) {
                    s = "아점";
                } else if (id == R.id.Lunch) {
                    s = "점심";
                } else if (id == R.id.LunchTo) {
                    s = "점저";
                } else if (id == R.id.Dinner) {
                    s = "저녁";
                } else if (id == R.id.DinnerTo) {
                    s = "야식";
                }
            }
        });
        ImageButton plus = (ImageButton) findViewById(R.id.plusBtn);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSum = setSum + 1;
                setText.setText(String.valueOf(setSum));
                KcalSum = Double.valueOf(Kcalstr) * setSum;
                KcalText.setText(String.valueOf(KcalSum));
                CarbohydratSum = Double.valueOf(Carbohydratestr) * setSum;
                CarbohydratText.setText(String.valueOf(CarbohydratSum));
                ProteinSum = Double.valueOf(Proteinstr) * setSum;
                ProteinText.setText(String.valueOf(ProteinSum));
                FatSum = Double.valueOf(Fatstr) * setSum;
                FatText.setText(String.valueOf(FatSum));
                SodiumSum = Double.valueOf(Sodiumstr) * setSum;
                SodiumText.setText(String.valueOf(SodiumSum));
                SugarSum = Double.valueOf(Sugarstr) * setSum;
                SugarText.setText(String.valueOf(SugarSum));
                KgSum = Double.valueOf(Kgstr) * setSum;
                KgText.setText(String.valueOf(KgSum));
            }
        });

        ImageButton minus = (ImageButton) findViewById(R.id.minusBtn);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSum = setSum - 1;
                setText.setText(String.valueOf(setSum));
                KcalSum -= Double.valueOf(Kcalstr);
                KcalText.setText(String.valueOf(KcalSum));
                CarbohydratSum -= Double.valueOf(Carbohydratestr);
                CarbohydratText.setText(String.valueOf(CarbohydratSum));
                ProteinSum -= Double.valueOf(Proteinstr);
                ProteinText.setText(String.valueOf(ProteinSum));
                FatSum -= Double.valueOf(Fatstr);
                FatText.setText(String.valueOf(FatSum));
                SodiumSum -= Double.valueOf(Sodiumstr);
                SodiumText.setText(String.valueOf(SodiumSum));
                SugarSum -= Double.valueOf(Sugarstr);
                SugarText.setText(String.valueOf(SugarSum));
                KgSum -= Double.valueOf(Kgstr);
                KgText.setText(String.valueOf(KgSum));
            }
        });
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
                        Intent homeintent = new Intent(getApplicationContext(), main_user.class);
                        startActivity(homeintent);
                        return true;
                    case R.id.calender:
                        FoodUpdate.UserFoodListBackgroundTask userFoodListBackgroundTask = new FoodUpdate.UserFoodListBackgroundTask();
                        userFoodListBackgroundTask.execute();
                        return true;
                    case R.id.communety:
                        Intent communetyintent = new Intent(getApplicationContext(), community.class);
                        startActivity(communetyintent);
                        return true;
                    case R.id.mypage:
                        Intent mypageintent = new Intent(getApplicationContext(), mypage.class);
                        startActivity(mypageintent);
                        return true;
                    case R.id.map:
                        Intent mapintent = new Intent(getApplicationContext(), map.class);
                        startActivity(mapintent);
                        return true;
                    case R.id.manbogi:
                        Intent manbogiintent = new Intent(getApplicationContext(), pedometer.class);
                        startActivity(manbogiintent);
                        return true;
                    case R.id.annoucement:
                        Intent annoucementintent = new Intent(getApplicationContext(), annoucement.class);
                        startActivity(annoucementintent);
                        return true;
                    case R.id.friend:
                        Intent friend = new Intent(getApplicationContext(), chatList.class);
                        startActivity(friend);
                        return true;
                }
                return false;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class FoodaddListBackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            target = "http://enejd0613.dothome.co.kr/foodcalendarlist.php";
        }

        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(target);//URL 객체 생성
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
                }

                //사용했던 것도 다 닫아줌
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(String result) {
            Intent intent = new Intent(FoodUpdate.this, FoodAddActivity.class);
            intent.putExtra("FoodName", String.valueOf(NameText.getText()));
            intent.putExtra("FoodKcal", Kcalstr);
            intent.putExtra("FoodCarbohydrate", Carbohydratestr);
            intent.putExtra("FoodProtein", Proteinstr);
            intent.putExtra("FoodFat", Fatstr);
            intent.putExtra("FoodSodium", Sodiumstr);
            intent.putExtra("FoodSugar", Sugarstr);
            intent.putExtra("FoodKg", Kgstr);
            intent.putExtra("FoodCood", Cood);
            intent.putExtra("Date", Date);
            intent.putExtra("FcCode", FcCode);
            intent.putExtra("Time", Time);
            intent.putExtra("UserFood", result);
            startActivity(intent);
            finish();
        }
    }

    class UserFoodListBackgroundTask extends AsyncTask<Void, Void, String> {
        String target;


        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            target = "http://enejd0613.dothome.co.kr/foodcalendarlist.php";
        }
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(target);//URL 객체 생성
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
                }

                //사용했던 것도 다 닫아줌
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(String result) {
            Intent intent = new Intent(FoodUpdate.this, CalendarActivity.class);
            intent.putExtra("UserFood", result);
            startActivity(intent);
        }
    }
    class UserFoodBackgroundTask1 extends AsyncTask<Void, Void, String> {
        String target;
        String Date;
        public UserFoodBackgroundTask1(String date) {
            this.Date = date;
        }
        @Override
        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            target = "http://enejd0613.dothome.co.kr/foodcalendarlist.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(target);//URL 객체 생성
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
                }

                //사용했던 것도 다 닫아줌
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(String result) {
            Intent intent = new Intent(FoodUpdate.this, userFood.class);
            intent.putExtra("UserFood", result);
            intent.putExtra("FoodName", String.valueOf(NameText.getText()));
            intent.putExtra("FoodKcal", Kcalstr);
            intent.putExtra("FoodCarbohydrate", Carbohydratestr);
            intent.putExtra("FoodProtein", Proteinstr);
            intent.putExtra("FoodFat", Fatstr);
            intent.putExtra("FoodSodium", Sodiumstr);
            intent.putExtra("FoodSugar", Sugarstr);
            intent.putExtra("FoodKg", Kgstr);
            intent.putExtra("FoodCood", Cood);
            intent.putExtra("Date", Date);

            intent.putExtra("FcCode", FcCode);
            intent.putExtra("Time", s);
            startActivity(intent);
        }
    }
    class FoodBackgroundTask extends AsyncTask<Void, Void, String> {
        String target;
        @Override
        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            target = "http://enejd0613.dothome.co.kr/foodlist.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(target);//URL 객체 생성
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
                }

                //사용했던 것도 다 닫아줌
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(String result) {
            Intent intent = new Intent(FoodUpdate.this, Food_List.class);
            intent.putExtra("Food",result);
            intent.putExtra("Date",Datestr);
            intent.putExtra("searchText",searchText.getText().toString());
            intent.putExtra("num", 0);
            startActivity(intent);
            finish();
        }
    }
}