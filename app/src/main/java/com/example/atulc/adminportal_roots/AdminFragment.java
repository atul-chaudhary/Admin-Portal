package com.example.atulc.adminportal_roots;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

public class AdminFragment extends Fragment{
    private boolean activityStartup = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_layout_fragment,container,false);


        final MainLayoutFragment mainLayoutFragment = new MainLayoutFragment();

        LinearLayout search_icon_dummy = view.findViewById(R.id.search_icon_dum);
        LinearLayout edit_admin_details = view.findViewById(R.id.edit_Admin_details);

        LinearLayout addAdminbtn =(LinearLayout)view.findViewById(R.id.add_Admin_btn);
        addAdminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().post(new MessageEvent("3"));

            }
        });

        search_icon_dummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment search_for_users= new Search_for_users();
                Bundle bundle = new Bundle();
                bundle.putInt("mCase",1);
                search_for_users.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.users_fragment,search_for_users)
                        .addToBackStack(null)
                        .commit();

            }
        });

        edit_admin_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment search_for_users = new Search_for_users();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.users_fragment,search_for_users)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

}
