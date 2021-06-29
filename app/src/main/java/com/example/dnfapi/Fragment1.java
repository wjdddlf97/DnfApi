package com.example.dnfapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dnfapi.board.BoardAdpater;
import com.example.dnfapi.board.BoardInfoActivity;
import com.example.dnfapi.function.FirebaseFunction;
import com.example.dnfapi.function.VOS.BoardListView;

import java.util.ArrayList;

public class Fragment1 extends Fragment {

    private View view;
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseFunction firebaseFunction = new FirebaseFunction();
    ArrayList<BoardListView> boardList;

    public static Fragment1 newInstance() {
        return new Fragment1();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_1,container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        return view;
    }


    public void onResume() {
        super.onResume();
        InitializeBoardData();
    }

    public void refresh() { // 새로고침 작동
        InitializeBoardData();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void InitializeBoardData()
    {
        boardList = new ArrayList<BoardListView>();
        firebaseFunction.getBoardList("free",(result)->{
            ListView listView = (ListView)view.findViewById(R.id.board_item_view);
            final BoardAdpater boardAdpater = new BoardAdpater(getActivity(),boardList);
            listView.setAdapter(boardAdpater);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    Intent intent = new Intent(getActivity(), BoardInfoActivity.class);
                    /* putExtra의 첫 값은 식별 태그, 뒤에는 다음 화면에 넘길 값 */
                    intent.putExtra("boardWriteDate",boardList.get(position).getWriteDate());
                    intent.putExtra("boardWriterId", boardList.get(position).getBoardWriterId());
                    startActivity(intent);
                }
            });

            if(result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                    boardAdpater.addItem(result.get(i).getTitle(), result.get(i).getWriter(),result.get(i).getWriteDate(), result.get(i).getWriterId());

                }
            }else{
                boardAdpater.addItem("현재 게시글이 없습니다", " ", " ", " ");
            }
            boardAdpater.notifyDataSetChanged();
            return null;
        });

    }
}
