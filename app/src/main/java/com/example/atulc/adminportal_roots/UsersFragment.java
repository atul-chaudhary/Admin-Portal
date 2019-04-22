package com.example.atulc.adminportal_roots;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

public class UsersFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         final View view = inflater.inflate(R.layout.users_layout_fragment, container, false);


        final MainLayoutFragment mainLayoutFragment = new MainLayoutFragment();

        LinearLayout addTeachers = (LinearLayout) view.findViewById(R.id.add_teacher_btn);
        LinearLayout addStudents = (LinearLayout) view.findViewById(R.id.add_student_btn);
        LinearLayout editUsers = (LinearLayout) view.findViewById(R.id.edit_users_details);
        LinearLayout searchIconDummy = (LinearLayout) view.findViewById(R.id.search_icon_dummy);

        addTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().post(new MessageEvent("1"));

            }
        });

        addStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().post(new MessageEvent("2"));
            }
        });

        searchIconDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //iOpenSearch.onOpenSearch(1);
                //onIOpenSearchListener.onOpenSearch(1);
                //FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frame_lay);
//                CoordinatorLayout coordinatorLayout = view.findViewById(R.id.users_fragment);
//                getSupportFragmentManager().beginTransaction().replace(R.id.users_fragment, new Search_for_users()).commit();

                Fragment search_for_users= new Search_for_users();
                Bundle bundle = new Bundle();
                bundle.putInt("mCase",2);
                search_for_users.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.users_fragment,search_for_users)
                        .addToBackStack(null)
                        .commit();

            }
        });

        editUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment search_for_users= new Search_for_users();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.users_fragment,search_for_users)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }



//
//    public static IOpenSearch onIOpenSearchListener;
//    public static IOpenSearch initListener (IOpenSearch listener) {
//        onIOpenSearchListener = listener;
//        return listener;
//    }

}
