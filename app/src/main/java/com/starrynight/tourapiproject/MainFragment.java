package com.starrynight.tourapiproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);



        Button button =(Button) v.findViewById(R.id.weather_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),weatherActivity.class);
                startActivity(intent);

                getActivity().finish();
            }
        });

        // 게시물 작성 페이지로 넘어가는 이벤트
        Button postWrite =(Button) v.findViewById(R.id.postWrite);
        postWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PostWriteActivity.class);
                startActivityForResult(intent, 101);
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}