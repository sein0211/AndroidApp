package com.starrynight.tourapiproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.myPage.ChangeProfileActivity;
import com.starrynight.tourapiproject.myPage.SettingActivity;
import com.starrynight.tourapiproject.myPage.myPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.myPage.myPageRetrofit.User2;
import com.starrynight.tourapiproject.myPage.myPost.MyPost;
import com.starrynight.tourapiproject.myPage.myPost.MyPostAdapter;
import com.starrynight.tourapiproject.myPage.myPost.OnMyPostItemClickListener;
import com.starrynight.tourapiproject.myPage.myWish.observation.MyObWishAdapter;
import com.starrynight.tourapiproject.myPage.myWish.post.MyPostWish;
import com.starrynight.tourapiproject.myPage.myWish.post.MyPostWishAdapter;
import com.starrynight.tourapiproject.myPage.myWish.post.OnMyPostWishItemClickListener;
import com.starrynight.tourapiproject.myPage.myWish.touristPoint.MyTpWishAdapter;
import com.starrynight.tourapiproject.postWritePage.PostWriteActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.BitmapFactory.decodeFile;

public class PersonFragment extends Fragment {

    Long userId;
    User2 user;

    ImageView profileImage;
    TextView nickName;
    TextView hashTagNameList;

    RecyclerView myPostWishList;
    RecyclerView myObWishList;
    RecyclerView myTpWishList;
    MyPostWishAdapter myPostWishAdapter;
    MyTpWishAdapter myTpWishAdapter;
    MyObWishAdapter myObWishAdapter;

    RecyclerView myPostList;
    MyPostAdapter myPostAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_person, container, false);

        //앱 내부 저장소의 userId 데이터 읽기
        String fileName = "userId";
        try{
            FileInputStream fis = getActivity().openFileInput(fileName);
            String line = new BufferedReader(new InputStreamReader(fis)).readLine();
            userId = Long.parseLong(line);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } System.out.println("userId = " + userId);

        nickName = v.findViewById(R.id.nickName);
        profileImage = v.findViewById(R.id.profileImage);
        hashTagNameList = v.findViewById(R.id.hashTagNameList);

        //닉네임, 프로필 사진을 불러오기 위한 get api
        Call<User2> call = RetrofitClient.getApiService().getUser2(userId);
        call.enqueue(new Callback<User2>() {
            @Override
            public void onResponse(Call<User2> call, Response<User2> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    assert user != null;
                    if (user.getProfileImage() != null){
                        profileImage.setImageBitmap(decodeFile(user.getProfileImage()));
                    }
                    nickName.setText(user.getNickName());
                } else {
                    System.out.println("사용자 정보 불러오기 실패");
                }
            }
            @Override
            public void onFailure(Call<User2> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });

        //해시태그 목록을 불러오기 위한 get api
        Call<List<String>> call2 = RetrofitClient.getApiService().getMyHashTag(userId);
        call2.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response <List<String>> response) {
                if (response.isSuccessful()) {
                    List<String> result = response.body();
                    String names = "";
                    for (String name : result){
                        names += "#" + name + " ";
                    }
                    int len = names.length();
                    hashTagNameList.setText(names.substring(0 , len-1));
                } else {
                    System.out.println("사용자 정보 불러오기 실패");
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });

        //게시물 작성 페이지로 이동
        Button goPostWrite = v.findViewById(R.id.goPostWrite);
        goPostWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostWriteActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        //설정 페이지로 이동
        Button goSetting = v.findViewById(R.id.goSetting);
        goSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, 101);
            }
        });

        //프로필 변경 페이지로 이동
        ImageView profileImage = v.findViewById(R.id.profileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeProfileActivity.class);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, 101);
            }
        });
        TextView nickName = v.findViewById(R.id.nickName);
        nickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeProfileActivity.class);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, 101);
            }
        });

        myPostList = v.findViewById(R.id.myPostList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myPostList.setLayoutManager(layoutManager);
        myPostAdapter= new MyPostAdapter();
        myPostList.setAdapter(myPostAdapter);

        //내 게시물
        Call<List<MyPost>> call3 = RetrofitClient.getApiService().getMyPost(userId);
        call3.enqueue(new Callback<List<MyPost>>() {
            @Override
            public void onResponse(Call<List<MyPost>> call, Response<List<MyPost>> response) {
                if (response.isSuccessful()) {
                    List<MyPost> result = response.body();
                    for (MyPost wp: result){
                        myPostAdapter.addItem(new MyPost(wp.getThumbnail(), wp.getTitle(), wp.getPostId()));
                    }
                    myPostList.setAdapter(myPostAdapter);
                } else {
                    System.out.println("내 게시물 불러오기 실패");
                }
            }
            @Override
            public void onFailure(Call<List<MyPost>> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });
        //내 게시물 클릭 이벤트
        myPostAdapter.setOnMyPostItemClickListener(new OnMyPostItemClickListener() {
            @Override
            public void onItemClick(MyPostAdapter.ViewHolder holder, View view, int position) {
                Toast.makeText(getActivity().getApplicationContext(), ""+"번 게시물 클릭", Toast.LENGTH_SHORT).show();
            }
        });


        //찜 관련 리사이클러
        myPostWishList = v.findViewById(R.id.myPostWishList);
        myObWishList = v.findViewById(R.id.myObWishList);
        myTpWishList = v.findViewById(R.id.myTpWishList);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myPostWishList.setLayoutManager(layoutManager1);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myObWishList.setLayoutManager(layoutManager2);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myTpWishList.setLayoutManager(layoutManager3);


        //찜(게시물) 클릭
        Button myPost = v.findViewById(R.id.myPost);
        myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPostWishAdapter = new MyPostWishAdapter();
                myPostWishList.setAdapter(myPostWishAdapter);
                myObWishList.setVisibility(View.GONE);
                myTpWishList.setVisibility(View.GONE);
                myPostWishList.setVisibility(View.VISIBLE);
                //찜(게시물) 불러오는 get api
                Call<List<MyPostWish>> call = RetrofitClient.getApiService().getMyWishPost(userId);
                call.enqueue(new Callback<List<MyPostWish>>() {
                    @Override
                    public void onResponse(Call<List<MyPostWish>> call, Response<List<MyPostWish>> response) {
                        if (response.isSuccessful()) {
                            List<MyPostWish> result = response.body();
                            for (MyPostWish wp: result){
                                myPostWishAdapter.addItem(new MyPostWish(wp.getThumbnail(), wp.getTitle(), wp.getPostId()));
                            }
                            myPostWishList.setAdapter(myPostWishAdapter);
                        } else {
                            System.out.println("찜(게시물) 불러오기 실패");
                        }
                    }
                    @Override
                    public void onFailure(Call<List<MyPostWish>> call, Throwable t) {
                        Log.e("연결실패", t.getMessage());
                    }
                });

                //찜(게시물) 클릭 이벤트
                myPostWishAdapter.setOnMyPostWishItemClickListener(new OnMyPostWishItemClickListener() {
                    @Override
                    public void onItemClick(MyPostWishAdapter.ViewHolder holder, View view, int position) {
                        Toast.makeText(getActivity().getApplicationContext(), ""+"번 게시물 클릭", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


//        //찜(관측지) 클릭
//        Button myOb = v.findViewById(R.id.myOb);
//        myOb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myObWishAdapter = new MyObWishAdapter();
//                myObWishList.setVisibility(View.VISIBLE);
//                myPostWishList.setVisibility(View.GONE);
//                myTpWishList.setVisibility(View.GONE);
//                // 찜(관측지) 불러오는 get api
//
//            }
//        });
//        //찜(관측지) 클릭 이벤트
//        myObWishAdapter.setOnMyWishItemClickListener(new OnMyPostWishItemClickListener() {
//            @Override
//            public void onItemClick(MyPostWishAdapter.ViewHolder holder, View view, int position) {
//                Toast.makeText(getActivity().getApplicationContext(), ""+"번 관측지 클릭", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        //찜(관광지) 클릭
//        Button myTour = v.findViewById(R.id.myTour);
//        myTour.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myTpWishAdapter = new MyTpWishAdapter();
//                myTpWishList.setVisibility(View.VISIBLE);
//                myPostWishList.setVisibility(View.GONE);
//                myObWishList.setVisibility(View.GONE);
//                // 찜(관광지) 불러오는 get api
//
//            }
//        });
//        //찜(관광지) 클릭 이벤트
//        myTpWishAdapter.setOnMyWishItemClickListener(new OnMyPostWishItemClickListener() {
//            @Override
//            public void onItemClick(MyPostWishAdapter.ViewHolder holder, View view, int position) {
//                Toast.makeText(getActivity().getApplicationContext(), ""+"번 관광지 클릭", Toast.LENGTH_SHORT).show();
//            }
//        });


//        RecyclerView recyclerView = v.findViewById(R.id.personrecyclerview);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//
//        Post_point_item_Adapter adapter = new Post_point_item_Adapter();
//        recyclerView.setAdapter(adapter);
//
//        adapter.addItem(new post_point_item("내 게시물","https://cdn.pixabay.com/photo/2018/08/11/20/37/cathedral-3599450_960_720.jpg"));


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            //프래그먼트 새로고침
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}