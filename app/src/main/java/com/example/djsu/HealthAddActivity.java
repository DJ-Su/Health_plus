package com.example.djsu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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
import java.util.ArrayList;

public class HealthAddActivity extends AppCompatActivity {
    Fragment fragment1, fragment2, fragment3, fragment4, fragment5, fragment6, fragment7, fragment8;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    exerciseAdapter exerciseAdapter;
    String Date,RoutineNameText;
    UserRoutine userRoutine = new UserRoutine();
    private Button BackBtn, AddBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_add);
        exerciseAdapter = new exerciseAdapter();
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//뒤로가기버튼 이미지 적용
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_hamburger);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        Bundle extras = getIntent().getExtras();
        AddBtn = findViewById(R.id.AddBtn);
        BackBtn = findViewById(R.id.BackBtn);
        Date = extras.getString("Date");
        RoutineNameText = extras.getString("RoutineNameText");

        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                for (int i = 0; i < userRoutine.getRoutineArrayList().size(); i++){
                    RoutineRequest routineRequest = new RoutineRequest(user.getId(),RoutineNameText,
                            userRoutine.getRoutineArrayList().get(i).getExCode(),userRoutine.getRoutineArrayList().get(i).getExerciseName(),userRoutine.getRoutineArrayList().get(i).getExPart());
                    RequestQueue queue = Volley.newRequestQueue(HealthAddActivity.this);
                    queue.add(routineRequest);
                }
                Toast.makeText(HealthAddActivity.this, "등록 완료되었습니다!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HealthAddActivity.this, routine.class);
                intent.putExtra("Date", Date);
                exerciseAdapter.resetSelectedPositions();
                startActivity(intent);
                finish();
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HealthAddActivity.this, routine.class);
                intent.putExtra("Date", Date);
                startActivity(intent);
                finish();
            }
        });
        Bundle bundle = new Bundle();
        fragment1 = new Fragment1();
        //4. 프래그먼트에 데이터 넘기기
        fragment1.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment1).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment1).commit();

        TabLayout tabs = (TabLayout)findViewById(R.id.tabs);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Bundle bundle = new Bundle();
                //2. 데이터 담기
                bundle.putString("Date",Date);
                bundle.putString("RoutineNameText",RoutineNameText);
                int position = tab.getPosition();
                if(position == 0){
                    fragment1 = new Fragment1();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment1.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment1).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment1).commit();
                }else if (position == 1){
                    fragment2 = new Fragment2();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment2.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment2).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment2).commit();
                }else if (position == 2){
                    fragment3 = new Fragment3();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment3.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment3).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment3).commit();
                }else if (position == 3){
                    fragment4 = new Fragment4();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment4.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment4).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment4).commit();
                }
                else if (position == 4){
                    fragment5 = new Fragment5();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment5.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment5).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment5).commit();
                }
                else if (position == 5){
                    fragment6 = new Fragment6();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment6.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment6).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment6).commit();
                }
                else if (position == 6){
                    fragment7 = new Fragment7();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment7.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment7).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment7).commit();
                }
                else if (position == 7){
                    fragment8 = new Fragment8();
                    //4. 프래그먼트에 데이터 넘기기
                    fragment8.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment8).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment8).commit();
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
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        Intent Mainintent = new Intent(getApplicationContext(), main_user.class);
                        startActivity(Mainintent);
                        return true;
                    case R.id.calender:
                        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.communety:
                        Intent communetyintent = new Intent(getApplicationContext(), community.class);
                        startActivity(communetyintent);
                        return true;
                    case R.id.mypage:
                        Intent mypageintent = new Intent(getApplicationContext(), mypage.class);
                        startActivity(mypageintent);
                        return true;
                    case R.id.annoucement:
                        Intent Noticeintent = new Intent(getApplicationContext(), annoucement.class);
                        startActivity(Noticeintent);
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

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}