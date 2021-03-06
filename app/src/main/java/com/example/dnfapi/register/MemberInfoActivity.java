package com.example.dnfapi.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dnfapi.MainActivity;
import com.example.dnfapi.R;
import com.example.dnfapi.function.FirebaseFunction;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class MemberInfoActivity extends AppCompatActivity {
    TextView memberName;
    TextView subTitle;
    Button submitButton;
    Button setGradeButton;

    String profileLink;
    Uri selectedImageUri;
    ImageButton profileImg;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFunction firebaseFunction = new FirebaseFunction();
    public String userName;
    public String dailyGrade;
    public String dailyGradePercent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);

        memberName = findViewById(R.id.memberName);
        subTitle = findViewById(R.id.memberSubtitle);
        submitButton = findViewById(R.id.submitButton);
        setGradeButton = findViewById(R.id.setGradeButton);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.member_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        firebaseFunction.getMemberInfo((result)->{
            if(result != null) {
                userName = result.get(0).getMemberName();
                memberName.setText(userName);
                subTitle.setText(result.get(0).getSubTitle());
                firebaseFunction.profileImageDownload(profileImg);

                getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            }
           return null;
        });

        findViewById(R.id.profileImg).setOnClickListener(onClickListener);
        profileImg = findViewById(R.id.profileImg);


        checkAdmin();

        findViewById(R.id.submitButton).setOnClickListener(onClickListener);
        findViewById(R.id.cancelButton).setOnClickListener(onClickListener);
        findViewById(R.id.setGradeButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.submitButton:
                    firebaseFunction.insertMemberInfo(memberName.getText().toString(),subTitle.getText().toString());
                    if(selectedImageUri != null) {
                        localUpoad();
                    }else{
                        startMainActivity();
                        finish();
                    }
                    break;
                case R.id.cancelButton:
                    if(userName != null){
                        finish();
                    }
                    break;
                case R.id.profileImg:
                    showGallery();
                    break;
                case R.id.setGradeButton:
                    if(user.getUid().equals(firebaseFunction.getAdministerId())){
                        gradeSelectSpinnerShow();
                    }
                    break;

            }
        }


    };



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar??? back??? ????????? ??? ??????
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        if(userName == null) {
            // super.onBackPressed();
        }else{
            super.onBackPressed();
        }
    }

    //galley ?????? , ?????? ?????? ?????? ????????? ??????
    private void showGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent. setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 200);
    }

    // ????????? ????????? uri ????????? ?????????
    public String getPath(Uri uri){
        String[]proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }

    // ?????? ????????? ????????? ???????????? ????????? ??????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            profileImg.setImageURI(selectedImageUri);

        }
    }
    //????????? ?????? ????????? ????????????????????? ?????????
    private void localUpoad() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Uri file = Uri.fromFile(new File(getPath(selectedImageUri)));
        final StorageReference riversRef = storageRef.child("users/" + user.getUid() + "/" + "Profile Image");
        UploadTask uploadTask = riversRef.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    profileLink = downloadUri.toString();
                    startMainActivity();
                    finish();
                }
            }
        });
    }


    public void checkAdmin(){
        if(user.getUid().equals(firebaseFunction.getAdministerId())){
            setGradeButton.setVisibility(View.VISIBLE);

        }
    }

    public void gradeSelectSpinnerShow(){
        AlertDialog.Builder adServer = new AlertDialog.Builder(MemberInfoActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.grade_type_spinner,null);

        Spinner sp = (Spinner)mView.findViewById(R.id.dailyGradeSpinner);
        ArrayAdapter reportAdapter = ArrayAdapter.createFromResource(this, R.array.gradeList, android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(reportAdapter);
        reportTypePickSpinner(sp);

        adServer.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //??????
                gradeInputDialog();
                // Event
            }
        });
        adServer.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //??????
                // Event
            }
        });

        adServer.setView(mView);
        adServer.show();
    }


    public void gradeInputDialog(){
        AlertDialog.Builder ad = new AlertDialog.Builder(MemberInfoActivity.this);
        ad.setTitle("?????? ????????????");       // ?????? ??????

        final EditText et = new EditText(MemberInfoActivity.this);
        ad.setView(et);

        ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dailyGradePercent = et.getText().toString();
                dialog.dismiss();
                firebaseFunction.updateDailyGradeInfo(dailyGrade,dailyGradePercent);
            }
        });

        ad.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }




    public void reportTypePickSpinner(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch(position){
                    case 0:
                        dailyGrade="?????????";
                        break;
                    case 1:
                        dailyGrade="??????";
                        break;
                    case 2:
                        dailyGrade="??????";
                        break;
                    case 3:
                        dailyGrade="??????";
                        break;
                    case 4:
                        dailyGrade="?????????";
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dailyGrade="??????????????????";
            }
        });
    }


    //?????? ??????????????? ?????? ??????
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}