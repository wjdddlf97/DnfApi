package com.example.dnfapi.board;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.dnfapi.R;
import com.example.dnfapi.function.FirebaseFunction;
import com.example.dnfapi.function.VOS.BoardListView;
import com.example.dnfapi.function.VOS.ReplyFormVO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ReplyAdpater extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ReplyFormVO> sample;
    FirebaseFunction firebaseFunction = new FirebaseFunction();
    public String mtitle;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public String baseUrl = "https://api.neople.co.kr/df/servers/";

    public ReplyAdpater(Activity activity, ArrayList<ReplyFormVO> replyList, String title) {
        mContext = activity;
        sample = replyList;
        mLayoutInflater = LayoutInflater.from(mContext);
        mtitle = title;
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public Object getItem(int i) {
        return sample.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //메인에 있는 리스트뷰에 대한 동작
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View replyView = mLayoutInflater.inflate(R.layout.reply_list_view, null);

        TextView replyContent = (TextView)replyView.findViewById(R.id.replyContent);
        TextView replyWriteDate = (TextView)replyView.findViewById(R.id.replyWriteDate);
        TextView replyWriter = (TextView)replyView.findViewById(R.id.replyWriter);
        Button replyDelButton = (Button)replyView.findViewById(R.id.replyDelButton);
        ImageView replyProfileImage = (ImageView)replyView.findViewById(R.id.replyProfileImage);
        replyContent.setText(sample.get(position).getReplyContent());
        replyWriter.setText(sample.get(position).getWriter());
        replyWriteDate.setText(sample.get(position).getWriteDate());

        replyDelButton.setFocusable(false);

        if(sample.get(position).getWriteDate().equals(" ")){
            replyDelButton.setVisibility(View.INVISIBLE);
        }
        firebaseFunction.getUserIdFromName(sample.get(position).getWriter(),(result)->{
            if(result.size() != 0) {
                if (result.get(0).equals(user.getUid()) || user.getUid().equals(firebaseFunction.getAdministerId())) {
                    replyDelButton.setVisibility(View.VISIBLE);
                } else {
                    replyDelButton.setVisibility(View.INVISIBLE);
                }
                firebaseFunction.profileReplyImageDownload(replyProfileImage, result.get(0));

            }



            return null;
        });
        replyDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFunction.delReply(mtitle,
                        sample.get(position).getWriteDate(),
                        sample.get(position).getReplyContent(),
                        sample.get(position).getWriter());
            }
        });



        return replyView;
    }

    public void addItem(String replyContent, String replyWriter, String replyWriteDate, String replyWriterId){
        ReplyFormVO item = new ReplyFormVO();
        item.setReplyContent(replyContent);
        item.setWriter(replyWriter);
        item.setWriteDate(replyWriteDate);
        item.setWriterId(replyWriterId);

        sample.add(item);
    }



}
