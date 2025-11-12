package com.akiet.fragmentex_replace;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class FooterFragment extends Fragment {



    public FooterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_footer, container, false);

        // find view
        Button b1 = v.findViewById(R.id.button1);
        Button b2 = v.findViewById(R.id.button2);
        Button b3 = v.findViewById(R.id.button3);

        //
        FragmentManager fragmentManager = getParentFragmentManager();

        //event
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code here
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView_Content, new Fragment1())
                        .commit();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code here
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView_Content, new Fragment2())
                        .commit();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code here
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView_Content, new Fragment3())
                        .commit();
            }
        });
        return v;
    }
}