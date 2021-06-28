package com.example.dnfapi.function;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.example.dnfapi.function.VOS.BoardFormVO;
import com.example.dnfapi.function.VOS.CharacterListVO;
import com.example.dnfapi.function.VOS.DailyGradeFormVO;
import com.example.dnfapi.function.VOS.LogFormVO;
import com.example.dnfapi.function.VOS.MemberFormat;
import com.example.dnfapi.function.VOS.ReplyFormVO;
import com.example.dnfapi.function.VOS.ReportFormVO;
import com.example.dnfapi.function.VOS.Report_DirectFormVO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class FirebaseFunction {


    MemberFormat memberFormat;

    /**
     * 캐릭터에 대한 정보를 입력시 데이터베이스에 저장됩니다.
     * api 통신(getPosts)으로 해당하는 정보를 모두 받아올 수 있습니다.
     * @param serverId 입력할 캐릭터의 서버정보
     * @param charcterId 입력할 캐릭터의 id정보 (name아님)
     * @param characterName 입력할 캐릭터의 name정보 (id아님)
     * @param level 입력할 캐릭터의 level정보 (int)
     * @param jobId 입력할 캐릭터의 직업 id 정보
     * @param jobGrowId 입력할 캐릭터의 최종 직업 id
     * @param jobName 입력할 캐릭터의 직업 이름
     * @param jobGrowName 입력할 캐릭터의 최종 직업 이름
     */
    public static void insertCharacterInfo(String serverId, String charcterId, String characterName,
                                          int level, String jobId, String jobGrowId, String jobName, String jobGrowName){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        CharacterListVO characterListVO = new CharacterListVO(serverId, charcterId, characterName,
                level, jobId, jobGrowId, jobName, jobGrowName);

        db.collection("users/"+user.getUid()+"/캐릭터목록/").document(charcterId).set(characterListVO) // 책 정보 (북네임, 이미지, 저자, 학과) 저장
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d("testing", "성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public static void insertBoardInfo( String title, String boardContent, String writer){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        long now = System.currentTimeMillis();
        Date dateNow = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatDate = sdfNow.format(dateNow);

        BoardFormVO boardFormVO = new BoardFormVO(title, boardContent, formatDate,writer,user.getUid());
        db.collection("boards/").document(title).set(boardFormVO)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        insertBoardLogInfo("게시글 입력",title, boardContent, formatDate);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public static void updateBoardInfo(String title, String boardContent){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        long now = System.currentTimeMillis();
        Date dateNow = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatDate = sdfNow.format(dateNow);

        DocumentReference boardRef = db.collection("boards/").document(title);

        boardRef
                .update("boardContent", boardContent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        insertBoardLogInfo("게시글 수정",title, boardContent, formatDate);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


    public static void insertReplyInfo( String title, String writer,String replyContent){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        long now = System.currentTimeMillis();
        Date dateNow = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatDate = sdfNow.format(dateNow);

        ReplyFormVO replyFormVO = new ReplyFormVO(replyContent, formatDate, writer, user.getUid());

        db.collection("boards/"+title+"/댓글목록").document().set(replyFormVO)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        insertReplyLogInfo("댓글 입력","댓글",replyContent, formatDate);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public static void getBoardList(Function<List<BoardFormVO>, Void> complete){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> boardSaveInit = new ArrayList<Map<String, Object>>();
        final List<BoardFormVO> boardList = new ArrayList<>();

        db.collection("boards/")
                .orderBy("writeDate", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("testtetetet", document.getId());
                                boardSaveInit.add(document.getData());
                            }
                            for (int i=0;i<boardSaveInit.size();i++) {
                                BoardFormVO bookSaveFormProto = new BoardFormVO(
                                        (String)boardSaveInit.get(i).get("title"),
                                        (String) boardSaveInit.get(i).get("boardContent"),
                                        (String) boardSaveInit.get(i).get("writeDate"),
                                        (String) boardSaveInit.get(i).get("writer"),
                                        (String) boardSaveInit.get(i).get("writerId"));
                                boardList.add(bookSaveFormProto);
                            }
                            complete.apply(boardList);

                        } else {

                        }
                    }
                });

    }

    public static void getBoardListLimit(Function<List<BoardFormVO>, Void> complete){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> boardSaveInit = new ArrayList<Map<String, Object>>();
        final List<BoardFormVO> boardList = new ArrayList<>();

        db.collection("boards/")
                .orderBy("writeDate", Query.Direction.DESCENDING).limit(4)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("testtetetet", document.getId());
                                boardSaveInit.add(document.getData());
                            }
                            for (int i=0;i<boardSaveInit.size();i++) {
                                BoardFormVO bookSaveFormProto = new BoardFormVO(
                                        (String)boardSaveInit.get(i).get("title"),
                                        (String) boardSaveInit.get(i).get("boardContent"),
                                        (String) boardSaveInit.get(i).get("writeDate"),
                                        (String) boardSaveInit.get(i).get("writer"),
                                        (String) boardSaveInit.get(i).get("writerId"));
                                boardList.add(bookSaveFormProto);
                            }
                            complete.apply(boardList);

                        } else {

                        }
                    }
                });

    }


    public static void getReplyList(String title,Function<List<ReplyFormVO>, Void> complete){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> replySaveInit = new ArrayList<Map<String, Object>>();
        final List<ReplyFormVO> replyList = new ArrayList<>();

        db.collection("boards/"+title+"/댓글목록")
                .orderBy("writeDate", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                replySaveInit.add(document.getData());
                            }
                            for (int i=0;i<replySaveInit.size();i++) {
                                ReplyFormVO replySaveFormProto = new ReplyFormVO(
                                        (String)replySaveInit.get(i).get("replyContent"),
                                        (String)replySaveInit.get(i).get("writeDate"),
                                        (String)replySaveInit.get(i).get("writer"),
                                        (String)replySaveInit.get(i).get("writerId"));
                                replyList.add(replySaveFormProto);
                            }
                            complete.apply(replyList);

                        } else {

                        }
                    }
                });

    }

    public static void getBoardInfo(String writerId,String writeDate,Function<List<BoardFormVO>, Void> complete){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> boardSaveInit = new ArrayList<Map<String, Object>>();
        final List<BoardFormVO> boardList = new ArrayList<>();

        db.collection("boards/")
                .whereEqualTo("writerId",writerId)
                .whereEqualTo("writeDate",writeDate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                boardSaveInit.add(document.getData());
                            }
                            for (int i=0;i<boardSaveInit.size();i++) {
                                BoardFormVO bookSaveFormProto = new BoardFormVO(
                                        (String)boardSaveInit.get(i).get("title"),
                                        (String) boardSaveInit.get(i).get("boardContent"),
                                        (String) boardSaveInit.get(i).get("writeDate"),
                                        (String) boardSaveInit.get(i).get("writer"),
                                        (String) boardSaveInit.get(i).get("writerId"));
                                boardList.add(bookSaveFormProto);
                            }
                            complete.apply(boardList);

                        } else {

                        }
                    }
                });

    }


    public void profileReplyImageDownload(ImageView profile_image_button, String userUid)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child("users/" + userUid +"/" + "Profile Image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onSuccess(Uri uri) {
                //이미지를 불러오는데 성공
                //Glide를 사용
                Glide.with(getApplicationContext() /* context */)
                        .load(uri)
                        .into(profile_image_button);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }





    public static void delBoard(String title, String boardContent){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        long now = System.currentTimeMillis();
        Date dateNow = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatDate = sdfNow.format(dateNow);



        db.collection("boards/").document(title)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        insertBoardLogInfo("게시글 삭제",title, boardContent, formatDate);
                        delAllReply(title);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


    public static void delAllReply(String title){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        getReplyIdList(title,(result)->{
            for(int i =0;i<result.size();i++) {
                db.collection("boards/" + title + "/댓글목록").document(result.get(i))
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("삭제", "1");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
            }
            return null;
        });

    }

    public static void delReply(String title,  String writeDate,String replyContent, String replyWriterName){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        long now = System.currentTimeMillis();
        Date dateNow = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatDate = sdfNow.format(dateNow);

        getUserIdFromName(replyWriterName, (nameResult)->{
            if(nameResult.get(0).equals(user.getUid())) {
                searchDelReply(title, writeDate, replyContent, (result) -> {
                    db.collection("boards/" + title + "/댓글목록").document(result.get(0))
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("삭제성공", "" + title);
                                    Log.d("삭제성공", "" + user.getUid() + replyContent);
                                    insertReplyLogInfo("댓글 삭제","댓글",replyContent, formatDate);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                    return null;
                });
            }

            return null;
        });

    }
    public static void getReplyIdList(String title,Function<List<String>, Void> complete){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> replySaveInit = new ArrayList<Map<String, Object>>();
        final List<ReplyFormVO> replyList = new ArrayList<>();
        final List<String> replyIdSaveInit = new ArrayList<>();

        db.collection("boards/"+title+"/댓글목록")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                replyIdSaveInit.add(document.getId());
                            }
                            complete.apply(replyIdSaveInit);
                        } else {

                        }
                    }
                });

    }


    public static void getReplyCount(String title,Function<List<Integer>, Void> complete){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> replySaveInit = new ArrayList<Map<String, Object>>();
        final List<ReplyFormVO> replyList = new ArrayList<>();
        final List<Integer> replyIdSaveInit = new ArrayList<>();

        db.collection("boards/"+title+"/댓글목록")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count =0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            replyIdSaveInit.add(count);
                            complete.apply(replyIdSaveInit);
                        } else {

                        }
                    }
                });

    }

    public static void searchDelReply(String title,String writeDate,String replyContent,Function<List<String>, Void> complete){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> replySaveInit = new ArrayList<Map<String, Object>>();
        final List<ReplyFormVO> replyList = new ArrayList<>();
        final List<String> replyIdSaveInit = new ArrayList<>();
        final List<String> replyIdDummy = new ArrayList<String>(Collections.singleton("dummy"));

        db.collection("boards/"+title+"/댓글목록")
                .whereEqualTo("writeDate",writeDate)
                .whereEqualTo("replyContent",replyContent)
                .whereEqualTo("writerId",user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                replyIdSaveInit.add(document.getId());
                            }
                            complete.apply(replyIdSaveInit);
                        } else {

                        }
                    }
                });

    }


    public static void getBoardLogCount(Function<List<Integer>, Void> complete){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> replySaveInit = new ArrayList<Map<String, Object>>();
        final List<ReplyFormVO> replyList = new ArrayList<>();
        final List<Integer> replyIdSaveInit = new ArrayList<>();

        db.collection("logs/"+user.getUid()+"/게시글로그목록")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count =0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            replyIdSaveInit.add(count);
                            complete.apply(replyIdSaveInit);
                        } else {

                        }
                    }
                });

    }

    public static void getReplyLogCount(Function<List<Integer>, Void> complete){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> replySaveInit = new ArrayList<Map<String, Object>>();
        final List<ReplyFormVO> replyList = new ArrayList<>();
        final List<Integer> replyIdSaveInit = new ArrayList<>();

        db.collection("logs/"+user.getUid()+"/댓글로그목록")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count =0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            replyIdSaveInit.add(count);
                            complete.apply(replyIdSaveInit);
                        } else {

                        }
                    }
                });

    }

    public static void insertReplyLogInfo(String logKinds, String logTitle,String logContent, String date){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        LogFormVO logFormVO = new LogFormVO(logKinds,logTitle,logContent,date);


        getReplyLogCount((result)->{
            db.collection("logs/"+user.getUid()+"/댓글로그목록/").document(String.valueOf(result)).set(logFormVO)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void avoid) {
                            Log.d("testing", "성공");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
            return null;
        });

    }

    public static void insertBoardLogInfo(String logKinds, String logTitle,String logContent, String date){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        LogFormVO logFormVO = new LogFormVO(logKinds,logTitle,logContent,date);


        getBoardLogCount((result)->{
            db.collection("logs/"+user.getUid()+"/게시글로그목록/").document(String.valueOf(result)).set(logFormVO)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void avoid) {
                            Log.d("testing", "성공");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
            return null;
        });

    }




    public static void updateDailyGradeInfo(String grade, String gradePercent){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        DailyGradeFormVO dailyGradeFormVO = new DailyGradeFormVO(grade,gradePercent + "%");

        db.collection("grades").document("오늘의 등급").set(dailyGradeFormVO)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }

    public static void getDailyGradeInfo(Function<List<DailyGradeFormVO>, Void> complete){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> dailyGradeSaveInit = new ArrayList<Map<String, Object>>();
        final List<DailyGradeFormVO> dailyGradeList = new ArrayList<>();

        DocumentReference docRef = db.collection("grades").document("오늘의 등급");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        dailyGradeSaveInit.add(document.getData());
                        DailyGradeFormVO dailyGrade = new DailyGradeFormVO(
                                dailyGradeSaveInit.get(0).get("dailyGrade").toString(),
                                dailyGradeSaveInit.get(0).get("dailyGradePercent").toString());
                        dailyGradeList.add(0, dailyGrade);
                        complete.apply(dailyGradeList);
                    }
                } else {

                }
            }
        });

    }


    public static void insertReportInfo( String kinds, String title,String Content, String type, String reportedMemberId){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        long now = System.currentTimeMillis();
        Date dateNow = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatDate = sdfNow.format(dateNow);

        ReportFormVO reportFormVO = new ReportFormVO(kinds, title, Content, type, reportedMemberId, user.getUid());
        db.collection("reports/").document(formatDate).set(reportFormVO)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }


    /**
     * 사용자 정보 저장하기 (MemberInfoActivity에서 사용)
     * @param memberName 사용자 이름
     * @param subTitle 사용자 소개글
     */
    public static void insertMemberInfo(String memberName, String subTitle){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        MemberFormat memberFormat = new MemberFormat(memberName,subTitle);

        db.collection("users/").document(user.getUid()).set(memberFormat)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d("testing", "성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    /**
     * 사용자 정보 받아오기 ( list형식으로 memberName과 subTitle 반환 )
     * @param complete 결과값 / complete.get(0).get.. 으로 사용
     */
    public static void getMemberInfo(Function<List<MemberFormat>, Void> complete){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> memberSaveInit = new ArrayList<Map<String, Object>>();
        final List<MemberFormat> memberList = new ArrayList<>();

        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                      memberSaveInit.add(document.getData());
                      MemberFormat memberFormat = new MemberFormat(
                              memberSaveInit.get(0).get("memberName").toString(),
                              memberSaveInit.get(0).get("subTitle").toString());
                      memberList.add(0,memberFormat);
                      complete.apply(memberList);
                    }
                } else {

                }
            }
        });

    }

    /**
     * 캐릭터 정보받아오기 ( list형식으로 기존에 저장했던 항목들 전부 호출 )
     * @param complete 결과값 / complete.get(i).get.. 으로 사용
     */
    public static void getCharacterInfo(Function<List<CharacterListVO>, Void> complete){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> characterSaveInit = new ArrayList<Map<String, Object>>();
        final List<CharacterListVO> characterList = new ArrayList<>();

        db.collection("users/"+user.getUid()+"/캐릭터목록")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("testtetetet", document.getId());
                                characterSaveInit.add(document.getData());
                            }
                            for (int i=0;i<characterSaveInit.size();i++) {
                                CharacterListVO bookSaveFormProto = new CharacterListVO(
                                        (String)characterSaveInit.get(i).get("serverId"),
                                        (String)characterSaveInit.get(i).get("characterId"),
                                        (String)characterSaveInit.get(i).get("characterName"),
                                        Math.toIntExact((long) characterSaveInit.get(i).get("level")),
                                        (String)characterSaveInit.get(i).get("jobId"),
                                        (String)characterSaveInit.get(i).get("jobGrowId"),
                                        (String)characterSaveInit.get(i).get("jobName"),
                                        (String)characterSaveInit.get(i).get("jobGrowName"));
                                characterList.add(bookSaveFormProto);
                            }
                            if(characterList.size() != 0) {
                                complete.apply(characterList);
                            }
                        } else {

                        }
                    }
                });

    }

    public static void getDirectReportInfo(Function<List<Report_DirectFormVO>, Void> complete){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> directReportSaveInit = new ArrayList<Map<String, Object>>();
        final List<Report_DirectFormVO> directReportList = new ArrayList<>();

        db.collection("directReport")
                .whereEqualTo("isEnd","0")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("testtetetet", document.getId());
                                directReportSaveInit.add(document.getData());
                            }
                            for (int i=0;i<directReportSaveInit.size();i++) {
                                Report_DirectFormVO directReportFormProto = new Report_DirectFormVO(
                                        (String)directReportSaveInit.get(i).get("name"),
                                        (String)directReportSaveInit.get(i).get("userId"),
                                        (String)directReportSaveInit.get(i).get("date"),
                                        (String)directReportSaveInit.get(i).get("isEnd"));
                                directReportList.add(directReportFormProto);
                            }
                            if(directReportList.size() != 0) {
                                complete.apply(directReportList);
                            }
                        } else {

                        }
                    }
                });

    }
    public static void updateIsEnd(String userId){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("directReport").document(userId);

        documentReference
                .update("isEnd", "1")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }




    /**
     * 캐릭터 이름으로 특정한 캐릭터의 정보 세부사항을 받아옴
     * @param characterName 받아올 캐릭터의 name
     * @param serverName 받아올 캐릭터의 serverName
     * @param complete 결과값 / complete.get(i).get.. 으로 사용
     */
    public static void getCharacterDetailInfo(String characterName, String serverName,Function<List<CharacterListVO>, Void> complete){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> characterSaveInit = new ArrayList<Map<String, Object>>();
        final List<CharacterListVO> characterList = new ArrayList<>();

        db.collection("users/"+user.getUid()+"/캐릭터목록/")
              //  .whereEqualTo("jobGrowId", "c9b492038ee3ca8d27d7004cf58d59f3")
                .whereEqualTo("characterName",characterName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("testtetetet", document.getId());
                                characterSaveInit.add(document.getData());
                            }
                            for (int i=0;i<characterSaveInit.size();i++) {
                                CharacterListVO bookSaveFormProto = new CharacterListVO(
                                        (String)characterSaveInit.get(i).get("serverId"),
                                        (String)characterSaveInit.get(i).get("characterId"),
                                        (String)characterSaveInit.get(i).get("characterName"),
                                        Math.toIntExact((long) characterSaveInit.get(i).get("level")),
                                        (String)characterSaveInit.get(i).get("jobId"),
                                        (String)characterSaveInit.get(i).get("jobGrowId"),
                                        (String)characterSaveInit.get(i).get("jobName"),
                                        (String)characterSaveInit.get(i).get("jobGrowName"));
                                characterList.add(bookSaveFormProto);
                            }
                            complete.apply(characterList);
                        } else {

                        }
                    }
                });

    }

    /**
     * 캐릭터 id로 데이터베이스를 비교해 해당하는 필드 삭제
     * @param characterId 삭제하고자 할 캐릭터 id
     */
    public static void characterDel(String characterId){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users/"+user.getUid()+"/캐릭터목록/").document(characterId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"삭제 성공", Toast.LENGTH_SHORT);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void eventImageDownload(int number, Function<String, Object> complete)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        List<String> eventList = new ArrayList<>();
        storageRef.child("events/eventLists/event"+number+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onSuccess(Uri uri) {

                eventList.add(uri.toString());

                complete.apply(eventList.get(0));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    /**
     * 사용자 프로필 사진 받아오기
     * 프로필 사진만 받아오기 때문에 imageView를 넣으면 그 imageView에 프로필 사진 할당
     * @param home_profile_image_button 프로필 사진을 붙이고 싶은 imageView
     */
    public void profileImageDownload(ImageView home_profile_image_button)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child("users/" + user.getUid() +"/" + "Profile Image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onSuccess(Uri uri) {
                //이미지를 불러오는데 성공
                //Glide를 사용
                Log.d("반환형식", "" + uri);
                Glide.with(getApplicationContext() /* context */)
                        .load(uri)
                        .into(home_profile_image_button);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



    public static void getUserIdFromName(String userName,Function<List<String>, Void> complete){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> replySaveInit = new ArrayList<Map<String, Object>>();
        final List<ReplyFormVO> replyList = new ArrayList<>();
        final List<String> userIdSaveInit = new ArrayList<>();

        db.collection("users/")
                .whereEqualTo("memberName",userName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userIdSaveInit.add(document.getId());
                            }
                            complete.apply(userIdSaveInit);
                        } else {

                        }
                    }
                });

    }




    public static void getEventCount(Function<List<Integer>, Void> complete){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Map<String, Object>> characterSaveInit = new ArrayList<Map<String, Object>>();
        final List<ReplyFormVO> replyList = new ArrayList<>();
        final List<Integer> replyIdSaveInit = new ArrayList<>();
        final List<Map<String, Object>> eventCountInit = new ArrayList<>();


        db.collection("events/")
                //  .whereEqualTo("jobGrowId", "c9b492038ee3ca8d27d7004cf58d59f3")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count =0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                eventCountInit.add(document.getData());
                                Log.d("event", String.valueOf(eventCountInit.get(0).get("count")));
                                count = Integer.parseInt(String.valueOf(eventCountInit.get(0).get("count")));
                            }
                            replyIdSaveInit.add(count);
                            complete.apply(replyIdSaveInit);

                        } else {

                        }
                    }
                });

    }




    public static void insertDirectReportInfo(String memberName, String userId, String isEnd){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        long now = System.currentTimeMillis();
        Date dateNow = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatDate = sdfNow.format(dateNow);

        Report_DirectFormVO report_directFormVO = new Report_DirectFormVO(memberName, userId, formatDate, isEnd);

        db.collection("directReport/").document(user.getUid()).set(report_directFormVO)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d("testing", "성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public static String getAdministerId(){
        return "tljCYHNOZiegp8vEAG6yiak2Yy32";
    }
}
