package com.example.dnfapi.aboutCharacter;

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

import java.util.ArrayList;
import java.util.function.Function;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CharacterListAdapter extends RecyclerView.Adapter<CharacterListAdapter.ViewHolder> {

    private ArrayList<CharacterListView> myCharacterList=new ArrayList<CharacterListView>();

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView list_my_characterName;//내캐릭터들의 이름
        TextView list_my_characterServer;//내캐릭터들의 서버
        Button list_my_character_del_button;

        ViewHolder(View itemView){
            super(itemView);

            list_my_characterName=itemView.findViewById(R.id.characterName);
            list_my_characterServer=itemView.findViewById(R.id.serverName);
            list_my_character_del_button=itemView.findViewById(R.id.delButton);

        }
    }

//    // 생성자에서 데이터 리스트 객체를 전달받음.
//    MyRentedBookListAdapter(){
//
//    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public CharacterListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.character_list_view, parent, false);
        CharacterListAdapter.ViewHolder vh = new CharacterListAdapter.ViewHolder(view);



        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CharacterListView characterListItem=myCharacterList.get(position);
        holder.list_my_characterName.setText(characterListItem.getCharacterName());
        holder.list_my_characterServer.setText(characterListItem.getServerName());

        holder.list_my_character_del_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofitInput(myCharacterList.get(position).getCharacterName(),myCharacterList.get(position).getServerName(),(result)->{
                    FirebaseFunction firebaseFunction = new FirebaseFunction();
                    firebaseFunction.characterDel(result);
                    notifyDataSetChanged();
                    return null;
                });
            }
        });
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return myCharacterList.size();
    }

    //아이템 데이터 추가를 위한 함수
    public void addItem(String characterName, String serverName){
        CharacterListView item=new CharacterListView();

        item.setCharacterName(characterName);
        item.setServerName(serverName);

        myCharacterList.add(item);
    }


    public void retrofitInput(String characterName, String serverName, Function<String, Void> complete){

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
