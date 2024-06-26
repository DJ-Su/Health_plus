package com.example.djsu;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    // 파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    //DatabaseReference는 데이터베이스의 특정 위치로 연결하는 거라고 생각하면 된다.
    //현재 연결은 데이터베이스에만 딱 연결해놓고
    //키값(테이블 또는 속성)의 위치 까지는 들어가지는 않은 모습이다.
    private DatabaseReference databaseReference = database.getReference();
    private EditText et_id, et_pass, et_pass2, et_name, et_age;
    private Button btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        et_id = findViewById(R.id.email_editText);
        et_pass = findViewById(R.id.password_editText);
        et_pass2 = findViewById(R.id.password_editText2);
        et_name = findViewById(R.id.name_editText);
        et_age = findViewById(R.id.birth_editText);

        // 회원가입 버튼 클릭 시 수행
        btn_register = findViewById(R.id.signup_btn);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                String UserID = et_id.getText().toString();
                String UserPass = et_pass.getText().toString();
                String UserPass2 = et_pass2.getText().toString();
                String UserName = et_name.getText().toString();
                String UserAge = et_age.getText().toString();
                String profile = "http://enejd0613.dothome.co.kr/photo/null.jpg";
                String State = "상태 메시지를 입력해주세요";
                String Nondisclosure = "공개";

                if (UserID.length() >= 5 && UserID.length() <= 20) { // 아이디가 5자 - 20자
                    if (UserPass.length() >= 8 && UserPass.length() <= 16) { // 비밀번호가 8자 - 16자
                        if (UserPass.equals(UserPass2)){ // 비밀번호, 비밀번호 확인이 같은지
                            addUser(et_id.getText().toString(),et_pass.getText().toString(), et_age.getText().toString(), et_name.getText().toString(), profile, State, Nondisclosure);

                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        System.out.println("hongchul" + response);
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if (success) { // 회원등록에 성공한 경우
                                            Toast.makeText(getApplicationContext(),"회원 등록에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(signup.this, login.class);
                                            startActivity(intent);
                                        } else { // 회원등록에 실패한 경우
                                            Toast.makeText(getApplicationContext(),"회원 등록에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            // 서버로 Volley를 이용해서 요청을 함.
                            signupRequest signupRequest1 = new signupRequest(UserID,UserPass,UserName,UserAge,responseListener);
                            RequestQueue queue = Volley.newRequestQueue(signup.this);
                            queue.add(signupRequest1);
                        } else if (!UserPass.equals(UserPass2)) {
                            Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                        }
                    } else if (UserPass.length() < 8) {
                        Toast.makeText(getApplicationContext(),"비밀번호를 8자 이상 입력해주세요.",Toast.LENGTH_SHORT).show();
                    } else if (UserPass.length() > 16) {
                        Toast.makeText(getApplicationContext(),"비밀번호를 16자 이하로 입력해주세요.",Toast.LENGTH_SHORT).show();
                    }
                } else  if (UserID.length() < 5){
                    Toast.makeText(getApplicationContext(),"아이디를 5자 이상 입력해주세요",Toast.LENGTH_SHORT).show();
                 }else  if (UserID.length() > 15) {
                    Toast.makeText(getApplicationContext(), "아이디를 15자 이하로 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //값을 파이어베이스 Realtime database로 넘기는 함수
    public void addUser(String UserId, String UserPass, String UserAge, String UserName, String Profile, String State, String Nondisclosure) {

        //여기에서 직접 변수를 만들어서 값을 직접 넣는것도 가능합니다.
        // ex) 갓 태어난 동물만 입력해서 int age=1; 등을 넣는 경우
        //String profileTest = "http://enejd0613.dothome.co.kr/photo/null.jpg";

        //animal.java에서 선언했던 함수.
        member member = new member(UserId,UserPass,UserAge,UserName,Profile,State,Nondisclosure);

        //child는 해당 키 위치로 이동하는 함수입니다.
        //키가 없는데 "zoo"와 name같이 값을 지정한 경우 자동으로 생성합니다.
        databaseReference.child("User").child(UserId).setValue(member);
    }
}