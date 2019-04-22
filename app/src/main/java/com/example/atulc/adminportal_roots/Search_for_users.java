package com.example.atulc.adminportal_roots;

import android.content.ClipData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.amazonaws.demo.posts.GetDetailsQuery;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class Search_for_users extends Fragment {

    private static final String TAG = "Search_for_users";
    private AWSAppSyncClient mAWSAppSyncClient;
    FastItemAdapter<RecyclerViewFastAdapter> mFastAdapter;
    RecyclerView recyclerView;
    LottieAnimationView animationView;
    TextView notFound_TextView;
    int mCase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_for_users, container, false);


        RelativeLayout main_relative_layout_search_found = view.findViewById(R.id.main_relative_layout_search_found);
        animationView = view.findViewById(R.id.animation_view);
        notFound_TextView = view.findViewById(R.id.not_found_textView);

        mAWSAppSyncClient = ClientFactory.getInstance(getContext());

        final SearchView searchView = view.findViewById(R.id.searchView_for_usersAndAdmin);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG, "onQueryTextSubmit: >>>" + query);
                runQuery(query);
                searchView.clearFocus();
                animationView.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mFastAdapter.clear();
                animationView.setVisibility(View.INVISIBLE);
                notFound_TextView.setVisibility(View.GONE);
                animationView.setScaleX(1);
                animationView.setScaleY(1);
                animationView.setAnimation(R.raw.ripple_loading);
                animationView.playAnimation();

                return false;
            }
        });

        mFastAdapter = new FastItemAdapter<RecyclerViewFastAdapter>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_for_users_search);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(mFastAdapter);
//        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(recyclerViewItemArrayList);
//        recyclerView.setAdapter(recyclerViewAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mFastAdapter.withSelectable(true);
        mFastAdapter.withOnClickListener(new FastAdapter.OnClickListener<RecyclerViewFastAdapter>() {
            @Override
            public boolean onClick(View v, IAdapter<RecyclerViewFastAdapter> adapter, RecyclerViewFastAdapter item, int position) {

                //Toast.makeText(v.getContext(),"yoyo  >>"+item.getName()+">>>"+item.getUserId(),Toast.LENGTH_LONG).show();

                Fragment search_users_found = new Search_Users_Found();
                Bundle bundle = new Bundle();
                bundle.putInt("userId",item.getUserId());
                bundle.putString("name",item.getName());
                bundle.putString("picurl50dp",item.getImageUrl());
                bundle.putString("hashname",item.getHashname());
                bundle.putString("email",item.getEmail());
                bundle.putInt("userTypeId",item.getTeacher_or_student());
                bundle.putString("userDivison",item.getDivisionA());
                bundle.putString("userSubDivision",item.getSubdivision());
                bundle.putString("userYear",item.getYear());
                bundle.putString("userSection",item.getSection());
                bundle.putInt("accKey",7);
                search_users_found.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_relative_layout_search_found,search_users_found)
                        .addToBackStack(null)
                        .commit();


                return false;
            }
        });

        Bundle args = getArguments();
        if(args!=null){

            mCase = args.getInt("mCase");
            Log.e(TAG, "onCreateView: +++++++++++"+mCase );

        }

        return view;
    }


    public void runQuery(String mName) {

        mAWSAppSyncClient.query(GetDetailsQuery.builder().email("drophouse0199@gmail.com").accKey(7).mCase(mCase).name(mName).build())
                .responseFetcher(AppSyncResponseFetchers.NETWORK_ONLY)
                .enqueue(getDetails);
    }

    private GraphQLCall.Callback<GetDetailsQuery.Data> getDetails = new GraphQLCall.Callback<GetDetailsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<GetDetailsQuery.Data> response) {
            if (response.data() != null) {
                String mResult = response.data().getDetails().result();
                Log.e(TAG, "onResponse: " +response.data());
                try {
                    JSONArray jsonArray = new JSONArray(mResult.toString());
                    if(jsonArray.length()==0) {
                        Log.e(TAG, "onResponse: empyt+++++++++++++++++++++++++");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                animationView.setAnimation(R.raw.not_found_face);
                                animationView.playAnimation();
                                animationView.setScaleX(2);
                                animationView.setScaleY(2);
                                //animationView.setLayoutParams(new RelativeLayout.LayoutParams(400,400));
                                notFound_TextView.setVisibility(View.VISIBLE);
                            }
                        });
                    }else {
                        awsMessDataStore(jsonArray);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, "ApolloException" + e.toString());
        }
    };

    private void awsMessDataStore(JSONArray jsonArray) throws JSONException {

        for (int i = 0; i < jsonArray.length(); i++) {

            try {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                final int userId = (int)jsonObject.getInt("userId");
                final String hashname  =(String)jsonObject.getString("hashname");
                final String email = (String)jsonObject.getString("email");
                final String imageUrl = (String) jsonObject.getString("picurl50dp");
                final String name = (String) jsonObject.getString("name");
                final int teacher_or_student = (int) jsonObject.getInt("userTypeId");    //for student or teacher icon in recyclerview
                final String divisionA_chip1 = (String) jsonObject.getString("div1Name");
                final String divisionB_chip2 = (String) jsonObject.getString("div2Name");
                final String divisionB_year_chip2 = (String) jsonObject.getString("divYear");
                final String divisionB_main = getFirstLetters(divisionB_chip2) + " "+yearSuffix(divisionB_year_chip2);
                final String divisionC_sec_chip3 = (String) jsonObject.getString("divSection");

                //recyclerViewItemArrayList.add(new RecyclerViewItem(imageUrl, name, teacher_or_student, divisionA_chip1, divisionB_main, divisionC_sec_chip3));

                Log.e(TAG, "\n awsMessDataStore: "+userId );
                Log.e(TAG, "awsMessDataStore: "+imageUrl );
                Log.e(TAG, "awsMessDataStore: "+name );
                Log.e(TAG, "awsMessDataStore: "+teacher_or_student );
                Log.e(TAG, "awsMessDataStore: "+divisionB_year_chip2 );
                Log.e(TAG, "awsMessDataStore: "+divisionC_sec_chip3+"\n" );

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI

                        animationView.setVisibility(View.GONE);
                        RecyclerViewFastAdapter recyclerViewFastAdapter = new RecyclerViewFastAdapter();
                        //RecyclerViewItem mCurrentRecyclerViewItem = recyclerViewItemArrayList.get(i);
                        recyclerViewFastAdapter.setUserId(userId);
                        recyclerViewFastAdapter.setHashname(hashname);
                        recyclerViewFastAdapter.setEmail(email);
                        recyclerViewFastAdapter.setImageUrl(imageUrl);
                        recyclerViewFastAdapter.setName(name);
                        recyclerViewFastAdapter.setTeacher_or_student(teacher_or_student);
                        recyclerViewFastAdapter.setSubdivision(divisionB_chip2);
                        recyclerViewFastAdapter.setYear(divisionB_year_chip2);
                        recyclerViewFastAdapter.setSection(divisionC_sec_chip3);
                        recyclerViewFastAdapter.setDivisionA(divisionA_chip1);
                        recyclerViewFastAdapter.setDivisionB(divisionB_main);
                        recyclerViewFastAdapter.setDivisionC("Sec "+divisionC_sec_chip3);
                        mFastAdapter.add(recyclerViewFastAdapter);



                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private String yearSuffix(String year){

        if (year!=null) {
            switch (year) {
                case "1":
                    return "1st Yr";

                case "2":
                    return "2nd Yr";

                case "3":
                    return "3rd Yr";

                case "4":
                    return "4th Yr";

                default:
                    return "";
            }
        }else{
            return "";
        }
    }

    public String getFirstLetters(String text) {
        String firstLetters = "";
        if (text.contains(" ")) {
            for (String s : text.split(" ")) {
                firstLetters +=s.charAt(0);
                firstLetters = firstLetters + ".";
            }
        }else{
            return text;
        }
        return firstLetters;
    }

}
