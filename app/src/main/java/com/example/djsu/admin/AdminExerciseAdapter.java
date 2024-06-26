package com.example.djsu.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.djsu.ExDelete;
import com.example.djsu.R;
import com.example.djsu.exerciseLsit;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminExerciseAdapter extends BaseAdapter {
    private Context context;
    private List<exerciseLsit> exerciseLsit;
    private Activity parentActivity;

    public AdminExerciseAdapter(Context context, List<exerciseLsit> exerciseLsit) {
        this.context = context;
        this.exerciseLsit = exerciseLsit;
        this.parentActivity = parentActivity;
    }
    @Override
    public int getCount () {
        return exerciseLsit.size();//리스트뷰의 총 갯수
    }

    @Override
    public Object getItem (int position){
        return exerciseLsit.get(position);//해당 위치의 값을 리스트뷰에 뿌려줌
    }
    @Override
    public long getItemId (int position){
        return position;
    }
    public void setItems(ArrayList<exerciseLsit> list) {
        exerciseLsit = list;
        notifyDataSetChanged();
    }
    //리스트뷰에서 실질적으로 뿌려주는 부분임
    @Override
    public View getView (final int position, View convertView, ViewGroup parent){

        View v = View.inflate(context, R.layout.item_admin_ex, null);
        // final TextView noticeText = (TextView) v.findViewById(R.id.userContent);
        TextView ExName = (TextView) v.findViewById(R.id.ExName);
        ExName.setText(exerciseLsit.get(position).getExerciseName());
        v.setTag(exerciseLsit.get(position).getExerciseName());
        Button deleteBtn = (Button) v.findViewById(R.id.delete);
        Button detailBtn = (Button) v.findViewById(R.id.detail);

        detailBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                AlertDialog ad = dlg.create();
                view = LayoutInflater.from(context).inflate(R.layout.dialog_admin_exercise, null, false);
                ad.setView(view);
                TextView exeName = view.findViewById(R.id.exeName);
                TextView ExPart = view.findViewById(R.id.ExPart);
                TextView ExExplanation = view.findViewById(R.id.ExExplanation);
                Button backBtn = view.findViewById(R.id.backBtn);
                exeName.setText(exerciseLsit.get(position).getExerciseName().toString()); //제목
                ExPart.setText(String.valueOf(exerciseLsit.get(position).getExPart()));
                ExExplanation.setText(String.valueOf(exerciseLsit.get(position).getExerciseExplanation()));
                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                    }
                });
                ad.show();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            //받아온 값이 success면 정상적으로 서버로부터 값을 받은 것을 의미함
                            if (success) {
                                Toast.makeText(context.getApplicationContext(), "삭제 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                exerciseLsit.remove(position);//리스트에서 해당부분을 지워줌
                                notifyDataSetChanged();//데이터가 변경된 것을 어댑터에 알려줌
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                ExDelete deleteRequest = new ExDelete(ExName.getText().toString(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(deleteRequest);

            }
        });
        return v;

    }
}