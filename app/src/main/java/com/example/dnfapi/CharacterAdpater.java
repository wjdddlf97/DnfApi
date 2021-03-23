package com.example.dnfapi;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.dnfapi.function.VOS.ApiForm;
import com.example.dnfapi.function.ApiInterface;
import com.example.dnfapi.function.VOS.CharacterListView;
import com.example.dnfapi.function.FirebaseFunction;

import java.util.ArrayList;
import java.util.function.Function;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CharacterAdpater extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<CharacterListView> sample;
    public String baseUrl = "https://api.neople.co.kr/df/servers/";

    public CharacterAdpater(Activity activity, ArrayList<CharacterListView> characterList) {
        mContext = activity;
        sample = characterList;
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
        View characterView = mLayoutInflater.inflate(R.layout.character_list_view, null);

        TextView characterName = (TextView)characterView.findViewById(R.id.characterName);
        TextView serverName = (TextView)characterView.findViewById(R.id.serverName);
        Button delButton = (Button)characterView.findViewById(R.id.delButton);

        characterName.setText(sample.get(position).getCharacterName());
        serverName.setText(sample.get(position).getServerName());

        delButton.setFocusable(false);

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofitInput(sample.get(position).getCharacterName(),sample.get(position).getServerName(),(result)->{
                    FirebaseFunction firebaseFunction = new FirebaseFunction();
                    firebaseFunction.characterDel(result);
                    notifyDataSetChanged();
                    return null;
                });

            }
        });

        return characterView;
    }

    public void addItem(String characterName, String serverName){
        CharacterListView item = new CharacterListView();
        item.setCharacterName(characterName);
        item.setServerName(serverName);

        sample.add(item);
    }

    // 캐릭터 name 이랑 server를 입력받아 정보를 가져와 삭제기능에 사용
    public void retrofitInput(String characterName, String serverName,Function<String, Void> complete){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.neople.co.kr/df/servers/"+serverName+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service1 = retrofit.create(ApiInterface.class);
        Call<ApiForm> call = service1.getPosts(characterName);

        call.enqueue(new Callback<ApiForm>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse( Call<ApiForm> call, Response<ApiForm> response) {
                if(response.isSuccessful()) {
                  complete.apply(response.body().rows.get(0).getCharacterId());
                }else{

                }
            }
            @Override
            public void onFailure(Call<ApiForm> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
