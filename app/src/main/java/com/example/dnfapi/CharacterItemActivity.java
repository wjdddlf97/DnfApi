package com.example.dnfapi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dnfapi.function.ApiInterface;
import com.example.dnfapi.function.VOS.ApiItemForm;
import com.example.dnfapi.function.VOS.ApiSetItemForm;
import com.example.dnfapi.function.VOS.ItemInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CharacterItemActivity extends AppCompatActivity {

    TextView characterTop;
    TextView characterTopReinforce;
    TextView characterBottom;
    TextView characterBottomReinforce;
    TextView characterWaist;
    TextView characterWaistReinforce;
    TextView characterShoulder;
    TextView characterShoulderReinforce;
    TextView characterShoe;
    TextView characterShoeReinforce;
    TextView characterWeapon;
    TextView characterWeaponReinforce;
    TextView characterNecklace;
    TextView characterNecklaceReinforce;
    TextView characterRing;
    TextView characterRingReinforce;
    TextView characterBracelet;
    TextView characterBraceletReinforce;
    TextView characterMagicStone;
    TextView characterMagicStoneReinforce;
    TextView characterSubWeapon;
    TextView characterSubWeaponReinforce;
    TextView characterEarrings;
    TextView characterEarringsReinforce;

    ImageView viewItemImage;
    TextView viewItemName;
    TextView viewItemType;
    TextView viewItemEffect;
    TextView viewItemRarity;
    TextView viewItemGrade;
    public String ImgUrl;
    public String itemGrade[] = new String[15];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_item);

        characterTop = findViewById(R.id.characterTop);
        characterTopReinforce = findViewById(R.id.characterTopReinforce);
        characterBottom = findViewById(R.id.characterBottom);
        characterBottomReinforce = findViewById(R.id.characterBottomReinforce);
        characterWaist = findViewById(R.id.characterWaist);
        characterWaistReinforce = findViewById(R.id.characterWaistReinforce);
        characterShoulder = findViewById(R.id.characterShoulder);
        characterShoulderReinforce = findViewById(R.id.characterShoulderReinforce);
        characterShoe = findViewById(R.id.characterShoes);
        characterShoeReinforce = findViewById(R.id.characterShoesReinforce);
        characterWeapon = findViewById(R.id.characterWeapon);
        characterWeaponReinforce = findViewById(R.id.characterWeaponReinforce);
        characterNecklace = findViewById(R.id.characterNecklace);
        characterNecklaceReinforce = findViewById(R.id.characterNecklaceReinforce);
        characterRing = findViewById(R.id.characterRing);
        characterRingReinforce = findViewById(R.id.characterRingReinforce);
        characterBracelet = findViewById(R.id.characterBracelet);
        characterBraceletReinforce = findViewById(R.id.characterBraceletReinforce);
        characterMagicStone = findViewById(R.id.characterMagicStone);
        characterMagicStoneReinforce = findViewById(R.id.characterMagicStoneReinforce);
        characterSubWeapon = findViewById(R.id.characterSubWeapon);
        characterSubWeaponReinforce = findViewById(R.id.characterSubWeaponReinforce);
        characterEarrings = findViewById(R.id.characterEarrings);
        characterEarringsReinforce = findViewById(R.id.characterEarringsReinforce);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.characterItem_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        String characterId = intent.getStringExtra("characterId");

        retrofitDetailItemInput(characterId);

        characterTop.setOnClickListener(onClickListener);
        characterBottom.setOnClickListener(onClickListener);
        characterWaist.setOnClickListener(onClickListener);
        characterShoulder.setOnClickListener(onClickListener);
        characterShoe.setOnClickListener(onClickListener);
        characterWeapon.setOnClickListener(onClickListener);
        characterNecklace.setOnClickListener(onClickListener);
        characterRing.setOnClickListener(onClickListener);
        characterBracelet.setOnClickListener(onClickListener);
        characterMagicStone.setOnClickListener(onClickListener);
        characterSubWeapon.setOnClickListener(onClickListener);
        characterEarrings.setOnClickListener(onClickListener);


    }

    //각각 textView 클릭시 세부사항 호출
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.characterWeapon:
                    openDialog(characterWeapon.getText().toString(), 0);
                    break;
                case R.id.characterTop:
                    openDialog(characterTop.getText().toString(),2);
                case R.id.characterShoulder:
                    openDialog(characterShoulder.getText().toString(), 3);
                    break;
                case R.id.characterBottom:
                    openDialog(characterBottom.getText().toString(), 4);
                    break;
                case R.id.characterShoes:
                    openDialog(characterShoe.getText().toString(), 5);
                    break;
                case R.id.characterWaist:
                    openDialog(characterWaist.getText().toString(), 6);
                    break;
                case R.id.characterNecklace:
                    openDialog(characterNecklace.getText().toString(),7);
                    break;
                case R.id.characterBracelet:
                    openDialog(characterBracelet.getText().toString(),8);
                    break;
                case R.id.characterRing:
                    openDialog(characterRing.getText().toString(),9);
                    break;
                case R.id.characterSubWeapon:
                    openDialog(characterSubWeapon.getText().toString(),10);
                    break;
                case R.id.characterMagicStone:
                    openDialog(characterMagicStone.getText().toString(),11);
                    break;
                case R.id.characterEarrings:
                    openDialog(characterEarrings.getText().toString(),12);
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

    //dialog 형태로 아이템에 대한 세부사항 보여줌
    public void openDialog(String itemName, int itemPlace){

        Dialog dialog=new Dialog(CharacterItemActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.item_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        Window window = dialog.getWindow();
        window.setAttributes(lp);

        viewItemName = (TextView)dialog.findViewById(R.id.itemName);
        viewItemType = (TextView)dialog.findViewById(R.id.itemType);
        viewItemEffect = (TextView)dialog.findViewById(R.id.itemEffect);
        viewItemRarity = (TextView)dialog.findViewById(R.id.itemRarity);
        viewItemImage = (ImageView)dialog.findViewById(R.id.itemImage);
        viewItemGrade =(TextView)dialog.findViewById(R.id.itemGrade);
        retrofitItemNameSearch(itemName,(nameResult)->{

            retrofitItemIdSearch(nameResult, (idResult)->{
                viewItemName.setText(itemName);
                viewItemEffect.setText(idResult.get(0));
                viewItemRarity.setText(idResult.get(1));
                viewItemType.setText(idResult.get(2));
                ImgUrl = "https://img-api.neople.co.kr/df/items/" + idResult.get(3);
                stringRarityColor(idResult.get(1),viewItemRarity);

                new DownloadFilesTask().execute(ImgUrl);

                switch (itemPlace){
                    case 0:
                        viewItemGrade.setText(itemGrade[0]);
                        stringGradeColor(itemGrade[0],viewItemGrade);
                        break;
                    case 2:
                        viewItemGrade.setText(itemGrade[2]);
                        stringGradeColor(itemGrade[2],viewItemGrade);
                        break;
                    case 3:
                        viewItemGrade.setText(itemGrade[3]);
                        stringGradeColor(itemGrade[3],viewItemGrade);
                        break;
                    case 4:
                        viewItemGrade.setText(itemGrade[4]);
                        stringGradeColor(itemGrade[4],viewItemGrade);
                        break;
                    case 5:
                        viewItemGrade.setText(itemGrade[5]);
                        stringGradeColor(itemGrade[5],viewItemGrade);
                        break;
                    case 6:
                        viewItemGrade.setText(itemGrade[6]);
                        stringGradeColor(itemGrade[6],viewItemGrade);
                        break;
                    case 7:
                        viewItemGrade.setText(itemGrade[7]);
                        stringGradeColor(itemGrade[7],viewItemGrade);
                        break;
                    case 8:
                        viewItemGrade.setText(itemGrade[8]);
                        stringGradeColor(itemGrade[8],viewItemGrade);
                        break;
                    case 9:
                        viewItemGrade.setText(itemGrade[9]);
                        stringGradeColor(itemGrade[9],viewItemGrade);
                        break;
                    case 10:
                        viewItemGrade.setText(itemGrade[10]);
                        stringGradeColor(itemGrade[10],viewItemGrade);
                        break;
                    case 11:
                        viewItemGrade.setText(itemGrade[11]);
                        stringGradeColor(itemGrade[11],viewItemGrade);
                        break;
                    case 12:
                        viewItemGrade.setText(itemGrade[12]);
                        stringGradeColor(itemGrade[12],viewItemGrade);
                        break;
                }


                return null;
            });

            return null;
        });

        dialog.show();

    }

    //등급에 따른 색
    public void stringRarityColor(String str, TextView textView){
        if(str.equals("커먼")){
            textView.setTextColor(Color.parseColor("#FFFFFF"));
        }else if(str.equals("언커먼")){
            textView.setTextColor(Color.parseColor("#68D5ED"));
        }else if(str.equals("레어")){
            textView.setTextColor(Color.parseColor("#B36BFF"));
        }else if(str.equals("유니크")){
            textView.setTextColor(Color.parseColor("#FF00FF"));
        }else if(str.equals("에픽")){
            textView.setTextColor(Color.parseColor("#FFB400"));
        }else if(str.equals("레전더리")){
            textView.setTextColor(Color.parseColor("#FF7800"));
        }
    }

    public void stringGradeColor(String str, TextView textView){
        if(str.equals("최하급")){
            textView.setTextColor(Color.parseColor("#B4C1D4"));
        }else if(str.equals("하급")){
            textView.setTextColor(Color.parseColor("#B4C1D4"));
        }else if(str.equals("중급")){
            textView.setTextColor(Color.parseColor("#7EB3EB"));
        }else if(str.equals("상급")){
            textView.setTextColor(Color.parseColor("#E6E14A"));
        }else if(str.equals("최상급")){
            textView.setTextColor(Color.parseColor("#E6E14A"));
        }
    }



    public void jsonParse(String str){
        try{
            JSONObject jsonObject = new JSONObject(str);

            JSONArray jsonArray = jsonObject.getJSONArray("status");

            for(int i=0; i<jsonArray.length(); i++)
            {
                JSONObject jsonParseObject = jsonArray.getJSONObject(i);
                Log.d("fwfwwfw", "" + jsonParseObject.get("name"));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //아이템 name으로 아이템 id 받아오기
    public void retrofitItemNameSearch(String itemName, Function<String,Void> complete){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.neople.co.kr/df/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service1 = retrofit.create(ApiInterface.class);
        Call<ApiItemForm> call = service1.getSearchItem(itemName);
        call.enqueue(new Callback<ApiItemForm>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse( Call<ApiItemForm> call, Response<ApiItemForm> response) {
                if(response.isSuccessful()) {
                    Log.d("아이템 이름", "" + itemName);
                    complete.apply(response.body().rows.get(0).getItemId());
                }else{

                }
            }
            @Override
            public void onFailure(Call<ApiItemForm> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    //아이템 id로 아이템 효과 받아오기
    public void retrofitItemIdSearch(String itemId, Function<List<String>, Void> complete){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.neople.co.kr/df/items/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        List<String> data1 = new ArrayList<>();

        ApiInterface service1 = retrofit.create(ApiInterface.class);
        Call<ItemInfo> call = service1.getItemDetailInfo(itemId);

        call.enqueue(new Callback<ItemInfo>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse( Call<ItemInfo> call, Response<ItemInfo> response) {
                if(response.isSuccessful()) {
                    Log.d("결과", "" + response.body().getItemExplainDetail());
                    data1.add(0,response.body().getItemExplainDetail());
                    data1.add(1,response.body().getItemRarity());
                    data1.add(2,response.body().getItemTypeDetail());
                    data1.add(3,response.body().getItemId());
                    complete.apply(data1);
                }else{
                    Log.d("오류","오류");
                }

            }
            @Override
            public void onFailure(Call<ItemInfo> call, Throwable t) {
                t.printStackTrace();
                Log.d("오류","오류2");
            }
        });


    }

    //캐릭터 id로 캐릭터 장비 받아오기
    public void retrofitDetailItemInput(String characterId){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.neople.co.kr/df/servers/hilder/characters/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service1 = retrofit.create(ApiInterface.class);
        Call<ApiSetItemForm> call = service1.getDetailCharacterItem(characterId);
        Log.d("   ",characterId);
        call.enqueue(new Callback<ApiSetItemForm>() {
            @Override
            public void onResponse( Call<ApiSetItemForm> call, Response<ApiSetItemForm> response) {
                if(response.isSuccessful()) {
                    Log.d(" ",response.body().equipment.get(0).getItemName());
                  //  Log.d(" ",response.body().equipment.get(0).getEnchant());
                    jsonParse(response.body().equipment.get(0).getEnchant().toString());

                    //무기 , 칭호 , 상의 , 머리어깨 , 하의 , 신발 , 허리 , 목걸이 , 팔찌 , 반지
                    //보조장비 , 마법석 , 귀걸이
                    characterWeapon.setText(response.body().equipment.get(0).getItemName());
                    characterWeaponReinforce.setText("+" + response.body().equipment.get(0).getReinforce());
                    itemGrade[0] = response.body().equipment.get(0).getItemGradeName();

                    characterTop.setText(response.body().equipment.get(2).getItemName());
                    characterTopReinforce.setText("+" + response.body().equipment.get(2).getReinforce());
                    itemGrade[2] = response.body().equipment.get(2).getItemGradeName();

                    characterShoulder.setText(response.body().equipment.get(3).getItemName());
                    characterShoulderReinforce.setText("+" + response.body().equipment.get(3).getReinforce());
                    itemGrade[3] = response.body().equipment.get(3).getItemGradeName();

                    characterBottom.setText(response.body().equipment.get(4).getItemName());
                    characterBottomReinforce.setText("+" + response.body().equipment.get(4).getReinforce());
                    itemGrade[4] = response.body().equipment.get(4).getItemGradeName();

                    characterShoe.setText(response.body().equipment.get(5).getItemName());
                    characterShoeReinforce.setText("+" + response.body().equipment.get(5).getReinforce());
                    itemGrade[5] = response.body().equipment.get(5).getItemGradeName();

                    characterWaist.setText(response.body().equipment.get(6).getItemName());
                    characterWaistReinforce.setText("+" + response.body().equipment.get(6).getReinforce());
                    itemGrade[6] = response.body().equipment.get(6).getItemGradeName();

                    characterNecklace.setText(response.body().equipment.get(7).getItemName());
                    characterNecklaceReinforce.setText("+"+response.body().equipment.get(7).getReinforce());
                    itemGrade[7] = response.body().equipment.get(7).getItemGradeName();

                    characterRing.setText(response.body().equipment.get(9).getItemName());
                    characterRingReinforce.setText("+"+response.body().equipment.get(9).getReinforce());
                    itemGrade[9] = response.body().equipment.get(9).getItemGradeName();

                    characterBracelet.setText(response.body().equipment.get(8).getItemName());
                    characterBraceletReinforce.setText("+"+response.body().equipment.get(8).getReinforce());
                    itemGrade[8] = response.body().equipment.get(8).getItemGradeName();

                    characterSubWeapon.setText(response.body().equipment.get(10).getItemName());
                    characterSubWeaponReinforce.setText("+"+response.body().equipment.get(10).getReinforce());
                    itemGrade[10] = response.body().equipment.get(10).getItemGradeName();

                    characterMagicStone.setText(response.body().equipment.get(11).getItemName());
                    characterMagicStoneReinforce.setText("+"+response.body().equipment.get(11).getReinforce());
                    itemGrade[11] = response.body().equipment.get(11).getItemGradeName();

                    characterEarrings.setText(response.body().equipment.get(12).getItemName());
                    characterEarringsReinforce.setText("+"+response.body().equipment.get(12).getReinforce());
                    itemGrade[12] = response.body().equipment.get(12).getItemGradeName();

                }else{

                }
            }
            @Override
            public void onFailure(Call<ApiSetItemForm> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    //url을 이용한 통신으로 캐릭터 장착 장비 이미지를 가져옴
    public class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
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
            viewItemImage.setImageBitmap(result);
        }
    }
}