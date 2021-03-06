package com.example.dnfapi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.example.dnfapi.Chat.Report_adminActivity;
import com.example.dnfapi.Chat.Report_customerActivity;
import com.example.dnfapi.aboutCharacter.CharacterInfoActivity;
import com.example.dnfapi.aboutCharacter.CharacterListAdapter;
import com.example.dnfapi.board.BoardInfoActivity;
import com.example.dnfapi.board.BoardLimitAdpater;
import com.example.dnfapi.board.MainBoardActivity;
import com.example.dnfapi.function.VOS.ApiForm;
import com.example.dnfapi.function.ApiInterface;
import com.example.dnfapi.function.VOS.BoardListView;
import com.example.dnfapi.function.VOS.CharacterListView;
import com.example.dnfapi.function.FirebaseFunction;
import com.example.dnfapi.register.LoginActivity;
import com.example.dnfapi.register.MemberInfoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


//import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import me.angeldevil.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ApiForm apiForm;
    String baseUrl = "https://api.neople.co.kr/df/servers/";
    String serverId = "all/";
    String characterId = "all/";
    ArrayAdapter spinnerAdapter;
    TextView textView2;
    FirebaseFunction firebaseFunction = new FirebaseFunction();
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<CharacterListView> characterList;
    Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    ArrayList<BoardListView> boardList;
    TextView home_grade_main_text;
    ViewFlipper v_fllipper;
    RecyclerView home_my_character_recycler_view;
    CharacterListAdapter myCharacterListAdapter;//?????? ?????? ?????? adapter
    Button home_my_character_card_view_more_button;//?????? ?????? ?????? ????????? ??????
    AutoScrollViewPager autoEventViewPager;
    CardView autoEventCardView;
    ArrayList<String> data = new ArrayList<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String userName = "";
    public String loadingCheck = "0";
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(loadingCheck.equals("0")) {
            loadingCheck = "1";
            Intent intent = new Intent(this, LoadingActivity.class);
            startActivity(intent);
        }

        home_my_character_recycler_view=findViewById(R.id.home_my_character_recycler_view);
        home_my_character_card_view_more_button=findViewById(R.id.home_my_character_card_view_more_button);

        home_my_character_recycler_view.setNestedScrollingEnabled(false);



        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);


        home_grade_main_text = findViewById(R.id.home_grade_main_text);




        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (user == null) {
            startLoginActivity();
            finish();
        }else { //????????? ????????? ???
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        if (document.exists()) { //db??? ????????? ????????? ?????????
                            Log.d("????????? ?????? ??????",user.getUid());

                            showMyRentedBookListFunc();

                            InitializeBoardData();
                            setGrade();

                            mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
                            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                            View headerView = navigationView.getHeaderView(0);
                            TextView navUserContent = (TextView) headerView.findViewById(R.id.navi_header_nameText);
                            TextView navUserName = (TextView) headerView.findViewById(R.id.navi_header_contentText);
                            ImageView navImgView = (ImageView) headerView.findViewById(R.id.navi_header_imageView);


                            firebaseFunction.getMemberInfo((result)->{
                                if(result != null) {
                                    navUserContent.setText(result.get(0).getSubTitle());
                                    navUserName.setText(result.get(0).getMemberName());
                                    userName = result.get(0).getMemberName();
                                    firebaseFunction.profileImageDownload(navImgView);
                                }
                                return null;
                            });

                            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                                @Override
                                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                    item.setChecked(false);
                                    mDrawerLayout.closeDrawers();
                                    int id = item.getItemId();
                                    String title = item.getTitle().toString();
                                    if(id == R.id.account){
                                        startMemberInfoActivity();
                                    }
                                    else if(id == R.id.setting){
                                        Toast.makeText(MainActivity.this, title + ": ?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                                        startBoardListActivity();
                                    }
                                    else if(id == R.id.report){
                                        startReportActivity();
                                    }else if(id == R.id.logout){
                                        Logout();
                                    }
                                    return true;
                                }
                            });
                            //InitializeCharacterData();
                        } else { //db??? ????????? ????????? ?????????
                            startMemberInfoActivity();

                        }
                    } else {

                    }
                }
            });
        }



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_View);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        findViewById(R.id.plusBtn).setOnClickListener(onClickListener);
        findViewById(R.id.home_board_detail_button).setOnClickListener(onClickListener);


        firebaseFunction.getEventCount((result)->{
            setScrollImage(result.get(0).intValue());
            return null;
        });




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });


        clickRentedViewMoreButtonFunc();
    }


    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch(menuItem.getItemId())
            {
                case R.id.goTrade:
                    startTradeMarketActivity();
                    break;
                case R.id.goSearch:
                    startSearchItemActivity();
                    break;
                case R.id.goFindJob:

                    break;

            }
            return true;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setScrollImage(int number){
        for (int i = 1; i< number + 1; i++){
            firebaseFunction.eventImageDownload(i,(result)->{
                setting(result);
                return null;
            });
        }
    }
    public void setting(String imageString){

        data.add(imageString);

        autoEventViewPager = (AutoScrollViewPager)findViewById(R.id.autoEventViewPager);
        AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(this, data);
        autoEventViewPager.setAdapter(scrollAdapter); //Auto Viewpager??? Adapter ??????
        autoEventViewPager.setInterval(3000); // ????????? ????????? ?????? ?????? ??????
        autoEventViewPager.startAutoScroll(); //Auto Scroll ??????

        autoEventViewPager.setOnPageClickListener(new AutoScrollViewPager.OnPageClickListener(){


            @Override
            public void onPageClick(AutoScrollViewPager pager, int position) {
                Intent intent = new Intent(getApplicationContext(),EventWebViewActivity.class);
                startActivity(intent);
            }
        });
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{  // ?????? ????????? ???????????? ????????? ????????? ??? ??????
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    public void refresh() { // ???????????? ??????
        showMyRentedBookListFunc();
        swipeRefreshLayout.setRefreshing(false);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.plusBtn: // + ?????? ????????? ??? ????????? ?????? dialog??????
                    createServerDialog();
                    break;
                case R.id.home_board_detail_button:
                    startBoardListActivity();
                    break;

            }
        }


    };




    private void Logout() { // ???????????? ??????
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void setGrade(){
        firebaseFunction.getDailyGradeInfo((result)->{
            String gradeText = result.get(0).getDailyGrade() + "(" + result.get(0).getDailyGradePercent() + ")";
            home_grade_main_text.setText(gradeText);


            return null;
        });

    }


    public void InitializeBoardData()
    {
        boardList = new ArrayList<BoardListView>();
        firebaseFunction.getBoardListLimit((result)->{
            ListView listView = (ListView)findViewById(R.id.board_thumb_view);
            final BoardLimitAdpater boardAdpater = new BoardLimitAdpater(this,boardList);
            listView.setAdapter(boardAdpater);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), BoardInfoActivity.class);
                    /* putExtra??? ??? ?????? ?????? ??????, ????????? ?????? ????????? ?????? ??? */
                    intent.putExtra("boardWriteDate",boardList.get(position).getWriteDate());
                    intent.putExtra("boardWriterId", boardList.get(position).getBoardWriterId());
                    startActivity(intent);
                }
            });

            if(result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                    boardAdpater.addItem(result.get(i).getTitle(), result.get(i).getWriter(),result.get(i).getWriteDate(), result.get(i).getWriterId(), result.get(i).getType());

                }
            }else{
                boardAdpater.addItem("?????? ???????????? ????????????", " ", " ", " ", " ");
            }
            boardAdpater.notifyDataSetChanged();
            return null;
        });

    }



    public void showMyRentedBookListFunc(){

        FirebaseFunction firebaseTest = new FirebaseFunction();
        if (user.getUid() != null) {
            firebaseTest.getCharacterInfo((result) -> {

                myCharacterListAdapter = new CharacterListAdapter();
                home_my_character_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                //adapter ??????
                home_my_character_recycler_view.setAdapter(myCharacterListAdapter);

                home_my_character_recycler_view.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                        if (e.getAction() == MotionEvent.ACTION_UP) {
                            View child = home_my_character_recycler_view.findChildViewUnder(e.getX(), e.getY());
                            int position = home_my_character_recycler_view.getChildAdapterPosition(child);

                            Intent intent = new Intent(getApplicationContext(), CharacterInfoActivity.class);
                            /* putExtra??? ??? ?????? ?????? ??????, ????????? ?????? ????????? ?????? ??? */
                            intent.putExtra("characterName", result.get(position).getCharacterName());
                            intent.putExtra("serverName", result.get(position).getServerId());
                            startActivity(intent);
                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });

                // null check
                if (result.size() > 0) {

                    int num = result.size();
                    if (num > 3) {
                        home_my_character_card_view_more_button.setVisibility(View.VISIBLE);
                        num = 3;
                    } else {
                        home_my_character_card_view_more_button.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < num; i++) {
                        myCharacterListAdapter.addItem(result.get(i).getCharacterName(), result.get(i).getServerId());
                    }
                    myCharacterListAdapter.notifyDataSetChanged();//adapter??? ????????? ??????
                } else {
                    myCharacterListAdapter.addItem(
                            "????????? ????????? ???????????? ????????????.",
                            ""
                    );
                    home_my_character_card_view_more_button.setVisibility(View.GONE);
                    myCharacterListAdapter.notifyDataSetChanged();//adapter??? ????????? ??????
                }

                return null;

            });
        }
    }



    public void clickRentedViewMoreButtonFunc(){
        FirebaseFunction firebaseTest = new FirebaseFunction();
        home_my_character_card_view_more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (home_my_character_card_view_more_button.getText().toString()){

                    case "+ ?????????":
                        firebaseTest.getCharacterInfo((resultList) -> {
                            if(resultList.size()>3){
                                myCharacterListAdapter = new CharacterListAdapter() ;
                                home_my_character_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                //adapter ??????
                                home_my_character_recycler_view.setAdapter(myCharacterListAdapter);
                                for(int i=0;i<resultList.size();i++){
                                    myCharacterListAdapter.addItem(resultList.get(i).getCharacterName(),
                                            resultList.get(i).getServerId());
                                }
                                myCharacterListAdapter.notifyDataSetChanged();//adapter??? ????????? ??????
                                home_my_character_card_view_more_button.setText("- ?????????");
                            }
                            return null;
                        });
                        break;
                    case "- ?????????":
                        showMyRentedBookListFunc();
                        home_my_character_card_view_more_button.setText("+ ?????????");
                        break;
                    default:
                        break;
                }
            }
        });
    }



    //????????? ????????? ???????????? server????????? ?????? spiner
    public void serverPickSpinner(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch(position){
                    case 0:
                        serverId="anton/";
                        break;
                    case 1:
                        serverId="bakal/";
                        break;
                    case 2:
                        serverId="cain/";
                        break;
                    case 3:
                        serverId="casillas/";
                        break;
                    case 4:
                        serverId="diregie/";
                        break;
                    case 5:
                        serverId="hilder/";
                        break;
                    case 6:
                        serverId="prey/";
                        break;
                    case 7:
                        serverId="siroco/";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // ???????????? ????????? ?????? ????????? dialog
    // ????????? ????????? ???????????? server????????? ??????
    public void createServerDialog(){
        AlertDialog.Builder adServer = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.report_dialog,null);
        adServer.setTitle("??????");       // ?????? ??????
        adServer.setMessage("????????? ??????????????????");   // ?????? ??????

        Spinner sp = (Spinner)mView.findViewById(R.id.spinner);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.server, android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(yearAdapter);
        serverPickSpinner(sp);
        adServer.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(" ", "Yes Btn Click");
                baseUrl += serverId;
                dialog.dismiss();     //??????
                createCharacterDialog();
                // Event
            }
        });
        adServer.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v("","No Btn Click");
                dialog.dismiss();     //??????
                // Event
            }
        });

        adServer.setView(mView);
        adServer.show();

    }

    // serverDialog ????????? ????????? dialog
    // ????????? ????????? ???????????? name????????? ??????
    public void createCharacterDialog(){
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle("??????");       // ?????? ??????
        ad.setMessage("?????????????????? ??????????????????");   // ?????? ??????

        final EditText et = new EditText(MainActivity.this);
        ad.setView(et);

        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                characterId = et.getText().toString();
                dialog.dismiss();
                retrofitInput(characterId);
            }
        });

        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }

    // retrofit ??????
    // ????????? ???????????? ???????????? ?????? ????????? ?????????
    public void retrofitInput(String characterName){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service1 = retrofit.create(ApiInterface.class);
        Call<ApiForm> call = service1.getPosts(characterName);

        call.enqueue(new Callback<ApiForm>() {
            @Override
            public void onResponse( Call<ApiForm> call, Response<ApiForm> response) {
                if(response.isSuccessful()) {
                    apiForm = response.body();
                    firebaseFunction.insertCharacterInfo(apiForm.getServerId(), apiForm.getCharacterId(), apiForm.getCharacterName(),
                            apiForm.getLevel(), apiForm.getJobId(), apiForm.getJobGrowId(), apiForm.getJobName(), apiForm.getJobGrowName());
                }else{

                }
            }
            @Override
            public void onFailure(Call<ApiForm> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }






    //???????????? ?????? -> ?????????
    public void startTradeMarketActivity(){
        Intent intent = new Intent(this,TradeMarketActivity.class);
        startActivity(intent);
    }
    //???????????? ?????? -> ??????
    public void startSearchItemActivity(){
        Intent intent = new Intent(this,SearchItemActivity.class);
        startActivity(intent);
    }
    //???????????? ?????? -> ???????????? ??????
    public void startMemberInfoActivity(){
        Intent intent = new Intent(this, MemberInfoActivity.class);
        startActivity(intent);
    }
    //???????????? ?????? -> ?????????
    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    private void startBoardListActivity() {
        Intent intent = new Intent(this, MainBoardActivity.class);
        startActivity(intent);
    }
    private void startReportActivity() {
        if(user.getUid().equals(firebaseFunction.getAdministerId())){
            Intent intent = new Intent(this, Report_adminActivity.class);
            startActivity(intent);
        }else{
            firebaseFunction.insertDirectReportInfo(userName,user.getUid(),"0");
            Intent intent = new Intent(this, Report_customerActivity.class);
            startActivity(intent);
        }
    }



}