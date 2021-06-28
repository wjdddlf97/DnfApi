package com.example.dnfapi.aboutCharacter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dnfapi.R;
import com.example.dnfapi.function.ApiInterface;
import com.example.dnfapi.function.VOS.ApiStatForm;
import com.example.dnfapi.function.VOS.CharacterDetailListVO;
import com.example.dnfapi.function.FirebaseFunction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CharacterInfoActivity extends AppCompatActivity {
    FirebaseFunction firebaseFunction = new FirebaseFunction();
    TextView characterDetailName;
    TextView characterDetailJobName;
    TextView characterDetailLevel;
    TextView characterDetailServerName;
    TextView characterDetailAdventureName;
    TextView characterDetailGuildName;
    ImageView characterImg;
    TextView text_title;

    List<CharacterDetailListVO> characterDetailListVO;
    public String ImgUrl;
    public String characterId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_info);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        characterDetailName = findViewById(R.id.characterDetailName);
        characterDetailJobName = findViewById(R.id.characterDetailJobName);
        characterDetailLevel = findViewById(R.id.characterDetailLevel);
        characterDetailServerName = findViewById(R.id.characterDetailServerName);
        characterDetailAdventureName = findViewById(R.id.characterDetailAdventureName);
        characterDetailGuildName = findViewById(R.id.characterDeatailGuildName);
        characterImg = findViewById(R.id.characterImg);
        text_title = findViewById(R.id.text_title);

        getDetailCharacterInfo();
        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        findViewById(R.id.viewCharacterDetailStat).setOnClickListener(onClickListener);
        findViewById(R.id.viewCharacterDetailItem).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.viewCharacterDetailStat:
                    showCharacterStat();
                    break;
                case R.id.viewCharacterDetailItem:
                    showCharacterItem();
                    break;
            }
        }


    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showCharacterItem() {
        Intent intent = new Intent(this, CharacterItemActivity.class);
        intent.putExtra("characterId", characterId);
        startActivity(intent);
    }

    public void showCharacterStat() {
        Intent intent = new Intent(this, CharacterStatActivity.class);
        intent.putExtra("characterId", characterId);
        startActivity(intent);
    }

    //데이터 베이스에서 캐릭터에 대한 정보를 가져와 반영
    public void getDetailCharacterInfo(){
        Intent intent = getIntent();
        String characterName = intent.getStringExtra("characterName");
        String serverName = intent.getStringExtra("serverName");
        firebaseFunction.getCharacterDetailInfo(characterName,serverName, (result)->{
            if(result.size() != 0) {
                String level = String.valueOf(result.get(0).getLevel());
                characterDetailName.setText(result.get(0).getCharacterName());
                characterDetailJobName.setText(result.get(0).getJobGrowName());
                characterDetailLevel.setText(level);
                characterDetailServerName.setText(result.get(0).getServerId());
                text_title.setText(result.get(0).getCharacterName());
                ImgUrl = "https://img-api.neople.co.kr/df/servers/" + result.get(0).getServerId() + "/characters/" + result.get(0).getCharacterId() + "?zoom=3";
                characterId = result.get(0).getCharacterId();
                new DownloadFilesTask().execute(ImgUrl);


                retrofitDetailStatInput(result.get(0).getCharacterId());
                retrofitDetailInput(result.get(0).getCharacterId());
            }else{
                finish();
            }
            return null;
        });
    }

    //url 통신으로 캐릭터 이미지를 받아와 반영
    public class DownloadFilesTask extends AsyncTask<String,Void, Bitmap>  {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = ImgUrl; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            // doInBackground 에서 받아온 total 값 사용 장소
            characterImg.setImageBitmap(result);
        }
    }





    public void retrofitDetailInput(String characterId){ //id로 정보 받아오기

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.neople.co.kr/df/servers/hilder/characters/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service1 = retrofit.create(ApiInterface.class);
        Call<CharacterDetailListVO> call = service1.getDetailCharacter(characterId);
        Log.d("   ",characterId);
        call.enqueue(new Callback<CharacterDetailListVO>() {
            @Override
            public void onResponse( Call<CharacterDetailListVO> call, Response<CharacterDetailListVO> response) {
                if(response.isSuccessful()) {
                    characterDetailAdventureName.setText(response.body().getAdventureName());
                    characterDetailGuildName.setText(response.body().getGuildName());
                }else{

                }
            }
            @Override
            public void onFailure(Call<CharacterDetailListVO> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void retrofitDetailStatInput(String characterId){ //캐릭터 스탯 받아오기

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.neople.co.kr/df/servers/hilder/characters/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service1 = retrofit.create(ApiInterface.class);
        Call<ApiStatForm> call = service1.getDetailCharacterStat(characterId);
        Log.d("   ",characterId);
        call.enqueue(new Callback<ApiStatForm>() {
            @Override
            public void onResponse( Call<ApiStatForm> call, Response<ApiStatForm> response) {
                if(response.isSuccessful()) {
                    for(int i=0;i<19;i++)
                    Log.d("결과값 테스트 id로 호출", response.body().status.get(i).getName()+ " : " + response.body().status.get(i).getValue());
                }else{

                }
            }
            @Override
            public void onFailure(Call<ApiStatForm> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}