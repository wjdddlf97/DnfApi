package com.example.dnfapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dnfapi.function.ApiInterface;
import com.example.dnfapi.function.VOS.ApiItemForm;
import com.example.dnfapi.function.VOS.CharacterListStatVO;
import com.example.dnfapi.function.VOS.ItemInfo;
import com.example.dnfapi.function.VOS.SetItemInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchItemActivity extends AppCompatActivity {

    ImageView image_item_search;
    TextView text_itemName_search;
    TextView text_itemRarity_search;
    TextView text_itemTypeDetail_search;
    TextView text_explainDetail_search;
    TextView text_setItemName_search;
    TextView text_slot_enable;
    TextView text_enchant_enable;

    public List<JSONObject> slotFormVOS = new ArrayList<JSONObject>();
    public List<JSONObject> status = new ArrayList<JSONObject>();
    public List<JSONObject> status2 = new ArrayList<JSONObject>();
    public List<CharacterListStatVO> statusDetail = new ArrayList<>();
    public String ImgUrl;

    public String enableSlotCard = "";
    public String enableEnchantCard = "";
    public int reinforce = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        image_item_search = findViewById(R.id.image_item_search);
        text_itemName_search = findViewById(R.id.text_itemName_search);
        text_itemRarity_search = findViewById(R.id.text_itemRarity_search);
        text_itemTypeDetail_search = findViewById(R.id.text_itemTypeDetail_search);
        text_explainDetail_search = findViewById(R.id.text_explainDetail_search);
        text_setItemName_search = findViewById(R.id.text_setItemName_search);
        text_slot_enable = findViewById(R.id.text_slot_enable);
        text_enchant_enable = findViewById(R.id.text_enchant_enable);


        SearchView searchView = findViewById(R.id.search_view_searchPage);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                retrofitInput(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
    }

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

    //retrofit 통신
    //아이템 name으로 대략적인 정보를 받아옴
    public void retrofitInput(String ItemName){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.neople.co.kr/df/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service1 = retrofit.create(ApiInterface.class);
        Call<ApiItemForm> call = service1.getSearchItem(ItemName);

        call.enqueue(new Callback<ApiItemForm>() {
            @Override
            public void onResponse( Call<ApiItemForm> call, Response<ApiItemForm> response) {
                if(response.isSuccessful()) {
                    enableSlotCard = "";
                    enableEnchantCard = "";
                    reinforce = 0;
                    slotFormVOS.clear();
                    status.clear();
                    text_slot_enable.setText("");
                    text_enchant_enable.setText("");
                    if(response.body().rows.size() != 0) {
                        retrofitItemDetail(response.body().rows.get(0).getItemId());
                       ImgUrl = "https://img-api.neople.co.kr/df/items/" + response.body().rows.get(0).getItemId();
                        image_item_search.setVisibility(View.VISIBLE);
                    }else{
                        image_item_search.setVisibility(View.INVISIBLE);
                        text_itemName_search.setText("아이템 이름을 확인해주세요");
                        text_explainDetail_search.setText(" ");
                        text_itemRarity_search.setText(" ");
                        text_itemTypeDetail_search.setText(" ");
                        text_setItemName_search.setText(" ");
                    }
                }else{

                }

            }
            @Override
            public void onFailure(Call<ApiItemForm> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }

    //retrofit 통신
    //아이템 name으로 해당하는 아이템 세트 id를 받아옴 ( 에픽일 경우에만 작동 )
    public void retrofitSearchSet(String setItemId, String itemName){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.neople.co.kr/df/setItems/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service1 = retrofit.create(ApiInterface.class);
        Call<SetItemInfo> call = service1.getSetItemDetailInfo(setItemId);

        call.enqueue(new Callback<SetItemInfo>() {
            @Override
            public void onResponse( Call<SetItemInfo> call, Response<SetItemInfo> response) {
                if(response.isSuccessful()) {
                    for(int i =0;i<response.body().setItems.size();i++) {
                        if (response.body().setItems.get(i).getItemName().equals(itemName)){
                            text_itemTypeDetail_search.setText(response.body().setItems.get(i).getSlotName());
                        }
                    }
                }else{

                }

            }
            @Override
            public void onFailure(Call<SetItemInfo> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }

    // item 등급에 따른 색 변경
    public void itemColor(String str){
        String strColor = "#000000";
        if(str.equals("에픽")){
            strColor = "#FFB400";
            text_itemRarity_search.setTextColor(Color.parseColor(strColor));
        }else if(str.equals("레어")){
            strColor = "#B36BFF";
            text_itemRarity_search.setTextColor(Color.parseColor(strColor));
        }else if(str.equals("유니크")){
            strColor = "#FF00FF";
            text_itemRarity_search.setTextColor(Color.parseColor(strColor));
        }else if(str.equals("노말")) {
            strColor = "#FFFFFF";
            text_itemRarity_search.setTextColor(Color.parseColor(strColor));
        }else if(str.equals("레전더리")) {
            strColor = "#FF7800";
            text_itemRarity_search.setTextColor(Color.parseColor(strColor));
        }else if(str.equals("언커먼")) {
            strColor = "#68D5ED";
            text_itemRarity_search.setTextColor(Color.parseColor(strColor));
        }else{
            strColor = "#000000";
            text_itemRarity_search.setTextColor(Color.parseColor(strColor));
        }
    }

    //json 형태로 반환된 값들을 변환
    public void jsonParse(String str){
        try{
            Log.d("wffw", str);
            String ext = str.replace(" ","");
            Log.d("wrwrwr",ext);
            JSONObject jsonObject = new JSONObject(ext);

            JSONArray jsonArray = jsonObject.getJSONArray("slots");
            JSONArray jsonArray2 = jsonObject.getJSONArray("enchant");

            for(int i=0; i<jsonArray.length(); i++)
            {
                JSONObject jsonParseObject = jsonArray.getJSONObject(i);
                slotFormVOS.add(i,jsonParseObject);
                enableSlotCard += slotFormVOS.get(i).getString("slotName");
                if(i+1 < jsonArray.length()) {
                   enableSlotCard += " , ";
                }
            }
            for(int i=0; i<jsonArray2.length(); i++)
            {
                JSONObject jsonParseObject2 = jsonArray2.getJSONObject(i);
                status.add(i,jsonParseObject2);
                jsonParseArray( jsonParseObject2.getJSONArray("status"), str);
            }

            text_slot_enable.setText(enableSlotCard);
            text_explainDetail_search.setText("");
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //json 배열로 반환된 값들을 변환
    public void jsonParseArray(JSONArray jsonArray, String str){

        try {
            Log.d("wffw", str);
            String ext = str.replace(" ","");
            JSONObject jsonObject = new JSONObject(ext);

            int count = 0;

            for(int i=0; i<jsonArray.length(); i++)
            {
                JSONObject jsonParseObject = jsonArray.getJSONObject(i);
                String cardStat = ""+ jsonParseObject.get("name") + "+" + jsonParseObject.get("value");
                enableEnchantCard += cardStat;
                count++;
                if(i != jsonArray.length() - 1) {
                    enableEnchantCard += " , ";
                }
            }
            enableEnchantCard += " ( + " + reinforce + " )";
            reinforce++;
            text_enchant_enable.setText(enableEnchantCard);
            enableEnchantCard+="\n";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //retrofit 통신
    //itemId로 '사전'에서 세부적인 정보 받아옴
    public void retrofitItemDetail(String ItemId){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.neople.co.kr/df/items/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service1 = retrofit.create(ApiInterface.class);
        Call<ItemInfo> call = service1.getItemDetailInfo(ItemId);



        call.enqueue(new Callback<ItemInfo>() {
            @Override
            public void onResponse( Call<ItemInfo> call, Response<ItemInfo> response) {
                if(response.isSuccessful()) {
                    text_itemName_search.setText(response.body().getItemName());
                    text_explainDetail_search.setText(response.body().getItemExplainDetail());
                    text_itemRarity_search.setText(response.body().getItemRarity());
                    if(response.body().getItemRarity().equals("에픽")){
                        retrofitSearchSet(response.body().getSetItemId(),response.body().getItemName());
                    }else {
                        text_itemTypeDetail_search.setText(response.body().getItemTypeDetail());
                    }
                    text_setItemName_search.setText(response.body().getSetItemName());
                    if(response.body().cardInfo != null) {
                        Log.d("",response.body().cardInfo.toString());
                        jsonParse(response.body().cardInfo.toString());
                        Log.d("  wrw", ""+response.body().getCardInfo());
                    }
                    new DownloadFilesTask().execute(ImgUrl);
                    itemColor(response.body().getItemRarity());
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

    //url로 통신을 하여 아이템에 대한 이미지를 받아옴
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
            image_item_search.setImageBitmap(result);
        }
    }
}