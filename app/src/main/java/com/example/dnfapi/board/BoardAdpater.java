package com.example.dnfapi.board;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.dnfapi.R;
import com.example.dnfapi.function.ApiInterface;
import com.example.dnfapi.function.FirebaseFunction;
import com.example.dnfapi.function.VOS.ApiForm;
import com.example.dnfapi.function.VOS.BoardListView;
import com.example.dnfapi.function.VOS.CharacterListView;

import java.util.ArrayList;
import java.util.function.Function;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoardAdpater extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<BoardListView> sample;
    public String baseUrl = "https://api.neople.co.kr/df/servers/";
    FirebaseFunction firebaseFunction = new FirebaseFunction();

    public BoardAdpater(Activity activity, ArrayList<BoardListView> boardList) {
        mContext = activity;
        sample = boardList;
        mLayoutInflater = LayoutInflater.from(mContext);
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
        View boardView = mLayoutInflater.inflate(R.layout.board_list_view, null);

        TextView boardName = (TextView)boardView.findViewById(R.id.boardName);
        TextView boardWriteDate = (TextView)boardView.findViewById(R.id.boardWriteDate);
        TextView boardWriter = (TextView)boardView.findViewById(R.id.boardWriter);


        boardWriter.setText(sample.get(position).getBoardWriter());
        boardWriteDate.setText(sample.get(position).getWriteDate());
        String boardTitleFull = sample.get(position).getBoardName();
        firebaseFunction.getReplyCount(sample.get(position).getBoardName(),(result)->{
            boardName.setText(boardTitleFull + "  (" + result.get(0) + ")" );
            return null;
        });

        return boardView;
    }

    public void addItem(String boardName, String boardWriter, String boardWriteDate, String boardWriterId){
        BoardListView item = new BoardListView();
        item.setBoardName(boardName);
        item.setBoardWriter(boardWriter);
        item.setWriteDate(boardWriteDate);
        item.setBoardWriterId(boardWriterId);

        sample.add(item);
    }


}
