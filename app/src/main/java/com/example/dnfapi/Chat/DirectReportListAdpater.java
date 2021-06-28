package com.example.dnfapi.Chat;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dnfapi.R;
import com.example.dnfapi.function.ApiInterface;
import com.example.dnfapi.function.FirebaseFunction;
import com.example.dnfapi.function.VOS.ApiForm;
import com.example.dnfapi.function.VOS.CharacterListView;
import com.example.dnfapi.function.VOS.Report_DirectFormVO;

import java.util.ArrayList;
import java.util.function.Function;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DirectReportListAdpater extends RecyclerView.Adapter<DirectReportListAdpater.ViewHolder> {

    private ArrayList<Report_DirectFormVO> directReportList=new ArrayList<Report_DirectFormVO>();


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView list_directReportName;// 문의한 사용자 닉네임
        TextView list_directReportUserId;// 문의한 사용자 uid
        TextView list_directReportDate; // 문의 날짜

        ViewHolder(View itemView){
            super(itemView);

            list_directReportName=itemView.findViewById(R.id.directReportName);
            list_directReportUserId=itemView.findViewById(R.id.directReportUserId);
            list_directReportDate=itemView.findViewById(R.id.directReportDate);

        }
    }



    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public DirectReportListAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.directreport_list_view, parent, false);
        DirectReportListAdpater.ViewHolder vh = new DirectReportListAdpater.ViewHolder(view);



        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Report_DirectFormVO reportDirectItem=directReportList.get(position);
        holder.list_directReportName.setText(reportDirectItem.getName());
        holder.list_directReportUserId.setText(reportDirectItem.getUserId());
        holder.list_directReportDate.setText(reportDirectItem.getDate());

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return directReportList.size();
    }

    //아이템 데이터 추가를 위한 함수
    public void addItem(String userName, String userUid, String date){
        Report_DirectFormVO item=new Report_DirectFormVO();

        item.setName(userName);
        item.setUserId(userUid);
        item.setDate(date);

        directReportList.add(item);
    }



}
