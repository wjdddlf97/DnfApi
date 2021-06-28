package com.example.dnfapi.aboutCharacter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.dnfapi.R;
import com.example.dnfapi.function.ApiInterface;
import com.example.dnfapi.function.VOS.ApiStatForm;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CharacterStatActivity extends AppCompatActivity {
    TextView characterHp;
    TextView characterMp;
    TextView characterPhysicalDefense;
    TextView characterMagicDefense;
    TextView characterPower;
    TextView characterMagicPower;
    TextView characterHealthStat;
    TextView characterMentalStat;
    TextView characterPhysicalAttack;
    TextView characterMagicAttack;
    TextView characterPhysicalCritical;
    TextView characterMagicCritical;
    TextView characterIndependentAttack;
    TextView characterCastSpeed;
    TextView characterHangMa;
    TextView characterHitRate;
    TextView characterFirePower;
    TextView characterFireResistance;
    TextView characterWaterPower;
    TextView characterWaterResistance;
    TextView characterLightPower;
    TextView characterLightResistance;
    TextView characterDarkPower;
    TextView characterDarkResistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_stat);

        Intent intent = getIntent();
        String characterId = intent.getStringExtra("characterId");

        retrofitDetailStatInput(characterId);

        characterHp = findViewById(R.id.characterHp);
        characterMp = findViewById(R.id.characterMp);
        characterPhysicalDefense = findViewById(R.id.characterPhysicalDefense);
        characterMagicDefense = findViewById(R.id.characterMagicDefense);
        characterPower = findViewById(R.id.characterPower);
        characterMagicPower = findViewById(R.id.characterMagicPower);
        characterHealthStat = findViewById(R.id.characterHealthStat);
        characterMentalStat = findViewById(R.id.characterMentalStat);
        characterPhysicalAttack = findViewById(R.id.characterPhysicalAttack);
        characterMagicAttack = findViewById(R.id.characterMagicAttack);
        characterPhysicalCritical = findViewById(R.id.characterPhysicalCritical);
        characterMagicCritical = findViewById(R.id.characterMagicCritical);
        characterIndependentAttack = findViewById(R.id.characterIndependentAttack);
        characterCastSpeed = findViewById(R.id.characterCastSpeed);
        characterHangMa = findViewById(R.id.characterHangMa);
        characterHitRate = findViewById(R.id.characterHitRate);
        characterFirePower = findViewById(R.id.characterFirePower);
        characterFireResistance = findViewById(R.id.characterFireResistance);
        characterWaterPower = findViewById(R.id.characterWaterPower);
        characterWaterResistance = findViewById(R.id.characterWaterResistance);
        characterLightPower = findViewById(R.id.characterLightPower);
        characterLightResistance = findViewById(R.id.characterLightResistance);
        characterDarkPower = findViewById(R.id.characterDarkPower);
        characterDarkResistance = findViewById(R.id.characterDarkResistance);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.characterStat_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
                    characterHp.setText(response.body().status.get(0).getValue());
                    characterMp.setText(response.body().status.get(1).getValue());
                    characterPhysicalDefense.setText(response.body().status.get(2).getValue());
                    characterMagicDefense.setText(response.body().status.get(3).getValue());
                    characterPower.setText(response.body().status.get(4).getValue());
                    characterMagicPower.setText(response.body().status.get(5).getValue());
                    characterHealthStat.setText(response.body().status.get(6).getValue());
                    characterMentalStat.setText(response.body().status.get(7).getValue());
                    characterPhysicalAttack.setText(response.body().status.get(8).getValue());
                    characterMagicAttack.setText(response.body().status.get(9).getValue());

                    characterPhysicalCritical.setText(response.body().status.get(10).getValue());
                    characterMagicCritical.setText(response.body().status.get(11).getValue());
                    characterIndependentAttack.setText(response.body().status.get(12).getValue());
                    characterCastSpeed.setText(response.body().status.get(14).getValue());
                    //characterHangMa.setText(response.body().status.get(16).getValue()); // 사라진 능력치
                    characterHitRate.setText(response.body().status.get(16).getValue());

                    characterFirePower.setText(response.body().status.get(22).getValue());
                    characterFireResistance.setText(response.body().status.get(23).getValue());
                    characterWaterPower.setText(response.body().status.get(24).getValue());
                    characterWaterResistance.setText(response.body().status.get(25).getValue());
                    characterLightPower.setText(response.body().status.get(26).getValue());
                    characterLightResistance.setText(response.body().status.get(27).getValue());
                    characterDarkPower.setText(response.body().status.get(28).getValue());
                    characterDarkResistance.setText(response.body().status.get(29).getValue());
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