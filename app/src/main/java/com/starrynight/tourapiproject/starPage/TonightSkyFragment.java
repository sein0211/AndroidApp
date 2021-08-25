package com.starrynight.tourapiproject.starPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.starrynight.tourapiproject.MainActivity;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.starPage.starItemPage.OnStarItemClickListener;
import com.starrynight.tourapiproject.starPage.starItemPage.StarItem;
import com.starrynight.tourapiproject.starPage.starItemPage.StarViewAdapter;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.Constellation;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TonightSkyFragment extends Fragment {
    //bottomSheet 관련
    private LinearLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    private EditText editText;

    //recyclerview 관련
    RecyclerView constList;
    StarViewAdapter constAdapter;

    RecyclerView allConstList;
    TextView allConstBtn;

    Long constId;
    Constellation constellation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tonight_sky, container, false);

        // bottomSheet 설정
        editText = v.findViewById(R.id.edit_search);
        editText.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        editText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        bottomSheetBehavior.setPeekHeight(editText.getBottom() + 50);
                    }
                }
        );

        bottomSheet = v.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setPeekHeight(editText.getBottom());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        // 뒤로 가기 버튼 클릭 이벤트
        ImageButton backBtn = (ImageButton) v.findViewById(R.id.star_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
//                startActivity(intent);
                
            }
        });

        // 모든 천체 보기 버튼 클릭 이벤트
        allConstList = v.findViewById(R.id.all_const_recycler);
        allConstBtn = v.findViewById(R.id.all_const_btn);

        allConstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constAdapter = new StarViewAdapter();
                Intent intent = new Intent(getActivity().getApplicationContext(), StarAllActivity.class);
                startActivity(intent);
            }
        });


        // recyclerview 설정
        constList = v.findViewById(R.id.today_cel_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        constList.setLayoutManager(gridLayoutManager);
        constAdapter = new StarViewAdapter();
        constList.setAdapter(constAdapter);


        // 오늘의 별자리 리스트 불러오는 api
        Call<List<StarItem>> todayConstCall = RetrofitClient.getApiService().getTodayConst();
        todayConstCall.enqueue(new Callback<List<StarItem>>() {
            @Override
            public void onResponse(Call<List<StarItem>> call, Response<List<StarItem>> response) {
                if (response.isSuccessful()) {
                    List<StarItem> result = response.body();
                    for (StarItem si : result) {
                        constAdapter.addItem(new StarItem(si.getConstId(), si.getConstName(), si.getConstImage()));
                    }
                    constList.setAdapter(constAdapter);
                } else {
                    System.out.println("오늘의 별자리 불러오기 실패");
                }
            }

            @Override
            public void onFailure(Call<List<StarItem>> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });

        // item 클릭 시 해당 아이템 constId 넘겨주기
        constAdapter.setOnItemClickListener(new OnStarItemClickListener() {
            @Override
            public void onItemClick(StarViewAdapter.ViewHolder holder, View view, int position) {
                StarItem item = constAdapter.getItem(position);
                Intent intent = new Intent(getActivity().getApplicationContext(), StarActivity.class);
                intent.putExtra("constId", item.getConstId());
                //Log.d("constId출력", item.getConstId().toString());
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}