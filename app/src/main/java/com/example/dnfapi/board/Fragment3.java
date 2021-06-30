package com.example.dnfapi.board;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dnfapi.R;
import com.example.dnfapi.function.FirebaseFunction;
import com.example.dnfapi.function.VOS.BoardListView;

import java.util.ArrayList;

public class Fragment3 extends Fragment {

    private View view;
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseFunction firebaseFunction = new FirebaseFunction();
    ArrayList<BoardListView> boardList;
    Spinner classSpinner;
    String classType = "warrior(m)";
    public static Fragment3 newInstance() {
        return new Fragment3();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_3,container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        classSpinner = (Spinner)view.findViewById(R.id.classSpinner);
        classType = classSpinner.getSelectedItem().toString();
        serverPickSpinner(classSpinner);
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
        Log.d("refresh","refresh");
    }

    public void InitializeBoardData()
    {
        Log.d("init", classType);
        if(classType.equals("귀검사(남)")){}
        else {
            boardList = new ArrayList<BoardListView>();
            firebaseFunction.getClassBoardList(classType, (result) -> {
                ListView listView = (ListView) view.findViewById(R.id.board_item_view);
                final BoardAdpater boardAdpater = new BoardAdpater(getActivity(), boardList);
                listView.setAdapter(boardAdpater);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        Intent intent = new Intent(getActivity(), BoardInfoActivity.class);
                        /* putExtra의 첫 값은 식별 태그, 뒤에는 다음 화면에 넘길 값 */
                        intent.putExtra("boardWriteDate", boardList.get(position).getWriteDate());
                        intent.putExtra("boardWriterId", boardList.get(position).getBoardWriterId());
                        startActivity(intent);
                    }
                });

                if (result.size() > 0) {
                    for (int i = 0; i < result.size(); i++) {
                        boardAdpater.addItem(result.get(i).getTitle(), result.get(i).getWriter(), result.get(i).getWriteDate(), result.get(i).getWriterId());

                    }
                } else {
                    boardAdpater.addItem("현재 게시글이 없습니다", " ", " ", " ");
                }
                boardAdpater.notifyDataSetChanged();
                return null;
            });
        }

    }


    public void serverPickSpinner(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch(position){
                    case 0:
                        classType="warrior(m)";
                        InitializeBoardData();
                        break;
                    case 1:
                        classType="warrior(w)";
                        InitializeBoardData();
                        break;
                    case 2:
                        classType="fighter(m)";
                        InitializeBoardData();
                        break;
                    case 3:
                        classType="fighter(w)";
                        InitializeBoardData();
                        break;
                    case 4:
                        classType="gunner(m)";
                        InitializeBoardData();
                        break;
                    case 5:
                        classType="gunner(w)";
                        InitializeBoardData();
                        break;
                    case 6:
                        classType="magician(m)";
                        InitializeBoardData();
                        break;
                    case 7:
                        classType="magician(w)";
                        InitializeBoardData();
                        break;
                    case 8:
                        classType="thief";
                        InitializeBoardData();
                        break;
                    case 9:
                        classType="gunKnife";
                        InitializeBoardData();
                        break;
                    case 10:
                        classType="magicLance";
                        InitializeBoardData();
                        break;
                    case 11:
                        classType="knight";
                        InitializeBoardData();
                        break;
                    case 12:
                        classType="create";
                        InitializeBoardData();
                        break;
                    case 13:
                        classType="darknight";
                        InitializeBoardData();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                classType="warrior(m)";
            }
        });
    }

}
