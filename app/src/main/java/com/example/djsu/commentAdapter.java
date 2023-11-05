package com.example.djsu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class commentAdapter extends BaseAdapter {
    private Context context;
    private List<User> commentList;
    private ImageButton siren_btn;
    private String Date = "";
    User user = new User();
    private TextView userName,userComment,date;
    public commentAdapter(Context context, List<User> commentList) {
        this.context = context;
        this.commentList = commentList;
    }
    @Override
    public int getCount () {
        return commentList.size();//리스트뷰의 총 갯수
    }

    @Override
    public Object getItem (int position){
        return commentList.get(position);//해당 위치의 값을 리스트뷰에 뿌려줌
    }
    @Override
    public long getItemId (int position){
        return position;
    }
    public void setItems(ArrayList<User> list) {
        commentList = list;
        notifyDataSetChanged();
    }
    //리스트뷰에서 실질적으로 뿌려주는 부분임
    @Override
    public View getView (final int position, View convertView, ViewGroup parent){
        View v = View.inflate(context, R.layout.item_comment, null);
        Date = getTime();
        userName = v.findViewById(R.id.userName);
        userName.setText(commentList.get(position).getCommentUser());

        userComment = v.findViewById(R.id.userComment);
        userComment.setText(commentList.get(position).getCommentContent());

        ImageView userProfileImage = v.findViewById(R.id.imageView2);
        // Picasso 라이브러리를 사용하여 사용자 프로필 이미지를 로드합니다.
        Picasso.get().load(commentList.get(position).getUserProfile()).into(userProfileImage);

        date = v.findViewById(R.id.date);
        date.setText(commentList.get(position).getCreatedTime());
        siren_btn =  v.findViewById(R.id.siren_btn);
        if(user.getId().equals(commentList.get(position).getCommentUser())){
            siren_btn.setVisibility(View.GONE);
        }
        siren_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                view = LayoutInflater.from(context).inflate(R.layout.dialog_declaration, null, false);
                builder.setView(view);
                final Button SaveButton = (Button) view.findViewById(R.id.saveBtn);
                final Button BackButton = (Button) view.findViewById(R.id.backBtn);
                final EditText declarationText = (EditText) view.findViewById(R.id.declaration);
                final AlertDialog dialog = builder.create();
                SaveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        declarationRequest declarationRequest = new declarationRequest(user.getId(),String.valueOf(commentList.get(position).getCommentKey()),
                        commentList.get(position).getCommentUser(),declarationText.getText().toString(),Date,"댓글");
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(declarationRequest);
                        Toast.makeText(context, "신고되었습니다.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                BackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return v;
    }

    private String getTime() {
        long now = System.currentTimeMillis();
        java.util.Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = dateFormat.format(date);

        return getTime;
    }
}
