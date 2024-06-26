package com.example.djsu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class chatList extends AppCompatActivity {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private DatabaseReference mDatabase;

//    private ListView mChatRoomListView;
//
//    private DatabaseReference mDatabase;

    private chatListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        ListView listview = findViewById(R.id.recycler_messages);

        User user = new User();
        String userName = user.getName();

        List<ChatRoom> postList = new ArrayList<>();

        mAdapter = new chatListAdapter(chatList.this, postList);
        listview.setAdapter(mAdapter);

        ChatRoom chatRoom = new ChatRoom();
        String chatRoomId = chatRoom.getChatRoomId();

        DatabaseReference chatRoomRef = FirebaseDatabase.getInstance().getReference("ChatRoom");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot chatRoomSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot memberSnapshot : chatRoomSnapshot.child("member").getChildren()) {
                        String name = memberSnapshot.child("name").getValue(String.class);

                        if (name != null && name.equals(userName)) {
                            ChatRoom chatRoom = chatRoomSnapshot.getValue(ChatRoom.class);
                            if (chatRoom != null) {
                                postList.add(chatRoom);
                            }
                            break;
                        }
                    }
                }

                // 리스트뷰에 데이터를 표시하는 작업 수행
                // 예를 들어, 어댑터를 생성하고 데이터를 설정한 뒤, 리스트뷰에 어댑터를 연결하는 등의 작업을 수행

                mAdapter.notifyDataSetChanged(); // 어댑터에 데이터 변경을 알려줍니다.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 읽기가 취소된 경우 호출됩니다.
            }
        };

        chatRoomRef.addListenerForSingleValueEvent(valueEventListener);

        DatabaseReference roomList = FirebaseDatabase.getInstance().getReference("ChatRoom");

        roomList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // 채팅방이 추가되었을 때 필요한 작업 수행
                // 예를 들어, 추가된 채팅방을 postList에 추가하고 리스트뷰 업데이트
                String roomId = dataSnapshot.getKey();
                if (roomId != null && roomId.contains(userName)) {
                    ChatRoom chatRoom = dataSnapshot.getValue(ChatRoom.class);
                    postList.add(chatRoom);
                    //sortPostListByTimestamp(); // 최신 메시지 순으로 정렬
                    mAdapter.notifyDataSetChanged(); // 리스트뷰 업데이트
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // 채팅방이 변경되었을 때 필요한 작업 수행
                // 예를 들어, 변경된 채팅방을 postList에서 찾아 업데이트
                String roomId = dataSnapshot.getKey();
                if (roomId != null && roomId.contains(userName)) {
                    ChatRoom updatedChatRoom = dataSnapshot.getValue(ChatRoom.class);
                    for (int i = 0; i < postList.size(); i++) {
                        ChatRoom chatRoom = postList.get(i);
                        if (chatRoom.getChatRoomId().equals(updatedChatRoom.getChatRoomId())) {
                            postList.set(i, updatedChatRoom);
                            mAdapter.notifyDataSetChanged(); // 리스트뷰 업데이트
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // 채팅방이 제거되었을 때 필요한 작업 수행
                // 예를 들어, 제거된 채팅방을 postList에서 찾아 제거
                String roomId = dataSnapshot.getKey();
                if (roomId != null && roomId.contains(userName)) {
                    ChatRoom removedChatRoom = dataSnapshot.getValue(ChatRoom.class);
                    for (int i = 0; i < postList.size(); i++) {
                        ChatRoom chatRoom = postList.get(i);
                        if (chatRoom.getChatRoomId().equals(removedChatRoom.getChatRoomId())) {
                            postList.remove(i);
                            mAdapter.notifyDataSetChanged(); // 리스트뷰 업데이트
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // 채팅방의 순서가 변경되었을 때 필요한 작업 수행
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 취소되었을 때 처리
            }
        });

        // postList를 최신 메시지 순으로 정렬하는 메서드 추가
//        private void sortPostListByTimestamp() {
//            Collections.sort(postList, new Comparator<ChatRoom>() {
//                @Override
//                public int compare(ChatRoom chatRoom1) {
//                    long timestamp1 = chatRoom1.getTimestamp();
//                    long timestamp2 = chatRoom2.getTimestamp();
//                    return Long.compare(timestamp2, timestamp1); // 내림차순 정렬
//                }
//            });
//        }

        Button imageButton = (Button) findViewById(R.id.button2);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), friend_invite.class);
                startActivity(intent);
            }
        });

        Button imageButton2 = (Button) findViewById(R.id.btn_friend_list);
        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), friends_list.class);
                startActivity(intent);
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
}