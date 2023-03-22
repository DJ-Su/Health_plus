package com.example.djsu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.djsu.Fragment.Fragment1;
import com.example.djsu.Fragment.Fragment2;
import com.example.djsu.Fragment.Fragment3;
import com.example.djsu.Fragment.Fragment4;
import com.example.djsu.Fragment.Fragment5;
import com.example.djsu.Fragment.Fragment6;
import com.example.djsu.Fragment.Fragment7;
import com.example.djsu.Fragment.Fragment8;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HealthAddActivity extends AppCompatActivity {
    String target;
    int a;
    Fragment fragment1, fragment2, fragment3, fragment4, fragment5, fragment6, fragment7, fragment8;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_add);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//뒤로가기버튼 이미지 적용
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_hamburger);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);


        new ExBackgroundTask(a= 0,target = "http://enejd0613.dothome.co.kr/exlist.php").execute();

        TabLayout tabs = (TabLayout)findViewById(R.id.tabs);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();
                if(position == 0){
                    new ExBackgroundTask(a= 0,target = "http://enejd0613.dothome.co.kr/exlist.php").execute();
                }else if (position == 1){

                    new ExBackgroundTask(a= 1,target = "http://enejd0613.dothome.co.kr/rowexlist.php").execute();

                }else if (position == 2){
                    new ExBackgroundTask(a= 2,target = "http://enejd0613.dothome.co.kr/chestexlist.php").execute();

                }else if (position == 3){
                    new ExBackgroundTask(a= 3,target = "http://enejd0613.dothome.co.kr/etcexlist.php").execute();
                }
                else if (position == 4){
                    new ExBackgroundTask(a= 4,target = "http://enejd0613.dothome.co.kr/Absexlist.php").execute();
                }
                else if (position == 5){

                    new ExBackgroundTask(a= 5,target = "http://enejd0613.dothome.co.kr/shoulderexlist.php").execute();
                }
                else if (position == 6){

                    new ExBackgroundTask(a= 6,target = "http://enejd0613.dothome.co.kr/eightexlist.php").execute();
                }
                else if (position == 7){

                    new ExBackgroundTask(a= 7,target = "http://enejd0613.dothome.co.kr/aerobicexlist.php").execute();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.home:
                        Intent homeintent = new Intent(getApplicationContext(), main_user.class);
                        startActivity(homeintent);
                        return true;
                    case R.id.calender:
                        Intent calenderintent = new Intent(getApplicationContext(), CalendarActivity.class);
                        startActivity(calenderintent);
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
                        Intent friend = new Intent(getApplicationContext(), friend_list.class);
                        startActivity(friend);
                        return true;
                }
                return false;
            }
        });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class ExBackgroundTask extends AsyncTask<Void, Void, String> {
        String target;
        int a;
        public ExBackgroundTask(int i,String target) {
            this.a = i;
            this.target = target;
        }

        @Override
        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
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
            Bundle bundle = new Bundle();
            //2. 데이터 담기
            bundle.putString("exercise",result);
            switch (a){
                case 0:  fragment1 = new Fragment1();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment1.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment1).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment1).commit();
                    break;
                case 1:  fragment2 = new Fragment2();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment2.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment2).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment2).commit();
                    break;
                case 2:  fragment3 = new Fragment3();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment3.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment3).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment3).commit();
                    break;
                case 3:  fragment4 = new Fragment4();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment4.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment4).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment4).commit();
                    break;
                case 4:  fragment5 = new Fragment5();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment5.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment5).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment5).commit();
                    break;
                case 5:  fragment6 = new Fragment6();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment6.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment6).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment6).commit();
                    break;
                case 6:  fragment7 = new Fragment7();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment7.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment7).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment7).commit();
                    break;
                case 7:  fragment8 = new Fragment8();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment8.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment8).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment8).commit();
                    break;
            }

        }
    }
}