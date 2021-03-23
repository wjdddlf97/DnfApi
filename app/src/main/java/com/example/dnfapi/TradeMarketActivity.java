package com.example.dnfapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dnfapi.function.ApiInterface;
import com.example.dnfapi.function.VOS.ApiItemForm;
import com.example.dnfapi.function.VOS.ItemInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TradeMarketActivity extends AppCompatActivity {

    String baseUrl = "https://api.neople.co.kr/df/";
    List<Integer> priceResult = new ArrayList<Integer>();
    public String ImgUrl;
    int Price[] = new int[10];
    String itemExplain = "";
    public String enableSlotCard = "";
    public String enableEnchantCard = "";
    public int reinforce = 0;
    public List<JSONObject> slotFormVOS = new ArrayList<JSONObject>();
    public List<JSONObject> status = new ArrayList<JSONObject>();
    ImageView image_item;
    TextView text_itemName;
    TextView text_minimumPrice;
    TextView text_averagePrice;
    TextView text_explain;
    TextView text_slot_enable;
    TextView text_enchant_enable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_market);

        image_item = findViewById(R.id.image_item);
        text_itemName = findViewById(R.id.text_itemName);
        text_averagePrice = findViewById(R.id.text_averagePrice);
        text_minimumPrice =findViewById(R.id.text_minimumPrice);
        text_explain = findViewById(R.id.text_explain);
        text_slot_enable = findViewById(R.id.text_slot_enable);
        text_enchant_enable = findViewById(R.id.text_enchant_enable);

        SearchView searchView = findViewById(R.id.search_view);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.trade_toolbar);
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
                itemExplain = status.get(i).getString("upgrade");
                Log.d("ㄹ질ㅈ",itemExplain);
                jsonParseArray( jsonParseObject2.getJSONArray("status"), str);

            }

            text_slot_enable.setText(enableSlotCard);

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
    //itemId로 '경매장'에서 세부적인 정보 받아옴
    public void retrofitInput(String ItemName){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service1 = retrofit.create(ApiInterface.class);
        Call<ApiItemForm> call = service1.getItemPrice(ItemName);

        call.enqueue(new Callback<ApiItemForm>() {
            @Override
            public void onResponse( Call<ApiItemForm> call, Response<ApiItemForm> response) {
                if(response.isSuccessful()) {
                    reinforce = 0;
                    status.clear();
                    slotFormVOS.clear();
                    enableEnchantCard="";
                    enableSlotCard="";
                    if(response.body().rows.size() != 0) {
                        Log.d("", "" + response.body().rows.get(0).getAveragePrice());
                        if (response.body().rows.size() >= 10) {
                            for (int i = 0; i < Price.length; i++) {
                                Price[i] = response.body().rows.get(i).getUnitPrice();
                            }
                            Arrays.sort(Price);
                            text_minimumPrice.setText("최소가 : " + Price[0]);
                        } else if (response.body().rows.size() < 10) {
                            for (int i = 0; i < response.body().rows.size(); i++) {
                                Price[i] = response.body().rows.get(i).getUnitPrice();
                            }
                            Arrays.sort(Price);
                            text_minimumPrice.setText("최소가 : " + Price[0]);
                        }
                        ImgUrl = "https://img-api.neople.co.kr/df/items/" + response.body().rows.get(0).getItemId();

                        retrofitItemDetail(response.body().rows.get(0).getItemId());
                        text_itemName.setText(response.body().rows.get(0).getItemName());
                        text_averagePrice.setText("평균 시세 : " + response.body().rows.get(0).getAveragePrice());
                        image_item.setVisibility(View.VISIBLE);
                    }else{
                        image_item.setVisibility(View.INVISIBLE);
                        text_itemName.setText("아이템 이름을 확인해 주세요");
                        text_minimumPrice.setText(" ");
                        text_averagePrice.setText(" ");
                        text_explain.setText(" ");
                    }
                }else{
                    Log.d("오류","오류");
                }
                new DownloadFilesTask().execute(ImgUrl);
            }
            @Override
            public void onFailure(Call<ApiItemForm> call, Throwable t) {
                t.printStackTrace();
                Log.d("오류","오류2");
            }
        });

    }

    //retrofit 통신
    //카드정보를 사전에서 받아와 반영
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
                   text_explain.setText(response.body().getItemExplain());
                   if(response.body().cardInfo != null){
                       jsonParse(response.body().cardInfo.toString());
                   }
                }else{
                    Log.d("오류","오류");
                }
                new DownloadFilesTask().execute(ImgUrl);
            }
            @Override
            public void onFailure(Call<ItemInfo> call, Throwable t) {
                t.printStackTrace();
                Log.d("오류","오류2");
            }
        });

    }

    //url를 이용하여 아이템에 대한 이미지 정보를 받아와 반영
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
            image_item.setImageBitmap(result);
        }
    }
}