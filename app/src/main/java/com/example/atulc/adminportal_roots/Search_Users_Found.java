package com.example.atulc.adminportal_roots;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.amazonaws.demo.posts.GetInsDivQuery;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.annotation.Nonnull;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class Search_Users_Found extends Fragment {
    private static final String TAG = "Search_Users_Found";
    private AWSAppSyncClient mAWSAppSyncClient;
    LottieAnimationView animationView;
    int userId;
    String userName;
    String userhashName;
    String userPicUrl50dp;
    String userEmailId;
    int userTypeId;
    int accKey;
    ArrayList<String> userDivision = new ArrayList<>();
    ArrayList<String> userSubDivision = new ArrayList<>();
    ArrayList<String> userYear = new ArrayList<>();
    ArrayList<String> userSection = new ArrayList<>();

    Spinner user_found_course;
    Spinner user_found_branch;
    Spinner user_found_year;
    Spinner user_found_section;
    FloatingActionButton user_found_done_btn;
    FloatingActionButton user_found_editButton;


    //for query edit button
    HashMap<String, LinkedList<DivisionAws>> mData;
    ArrayList FilteredList;
    ArrayAdapter divisonAdapter;
    String getDivsion;
    String getSubdividion_;
    String getyear_;
    String getSection;
    HashMap<String, String> stores_section = new HashMap<>();
    HashMap<String, String> stores_year = new HashMap<>();
    ArrayList subdivision_list = new ArrayList();
    ArrayList subYear_list = new ArrayList();
    ArrayList subSection_list = new ArrayList();
    ArrayList<String> userResponseList_fromSpinner = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_users_found, container, false);

        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getContext())
                .awsConfiguration(new AWSConfiguration(getContext()))
                .build();

        animationView = view.findViewById(R.id.animation_view1);
        user_found_done_btn = view.findViewById(R.id.user_found_done_btn);


        RelativeLayout search_users_found_main_layout = view.findViewById(R.id.relative_layout_for_search_users_found);
        search_users_found_main_layout.setEnabled(false);

        TextView user_name_found = view.findViewById(R.id.users_name_found);
        TextView user_hashname_found = view.findViewById(R.id.users_hashname_found);
        ImageView teacher_or_student_icon = view.findViewById(R.id.teacher_or_student_icon_searchFound);
        ImageView profile_imaage_users_found = view.findViewById(R.id.profile_image_users_found);
        TextView userEmailIdTextView = view.findViewById(R.id.users_found_emailid_rds);
        user_found_editButton = view.findViewById(R.id.user_found_editButton_FAB);

        user_found_course = view.findViewById(R.id.users_found_course);
        user_found_course.setEnabled(false);
        user_found_branch = view.findViewById(R.id.users_found_branch);
        user_found_branch.setEnabled(false);
        user_found_year = view.findViewById(R.id.users_found_year);
        user_found_year.setEnabled(false);
        user_found_section = view.findViewById(R.id.users_found_section);
        user_found_section.setEnabled(false);


        Bundle args = getArguments();
        if (args != null) {

            userId = args.getInt("userId");
            //Log.e(TAG, "userId "+userId );
            userhashName = args.getString("hashname");
            userName = args.getString("name");
            userPicUrl50dp = args.getString("picurl50dp");
            userEmailId = args.getString("email");
            userDivision.add(args.getString("userDivison"));
            userSubDivision.add(args.getString("userSubDivision"));
            userYear.add(args.getString("userYear"));
            userSection.add(args.getString("userSection"));
            userTypeId = args.getInt("userTypeId");
            accKey = args.getInt("accKey");

            user_name_found.setText(userName);
            user_hashname_found.setText("@" + userhashName);
            Picasso.get().load(userPicUrl50dp).transform(new CircleTransform()).into(profile_imaage_users_found);
            if (userTypeId == 1) {
                teacher_or_student_icon.setVisibility(View.VISIBLE);
            } else {
                teacher_or_student_icon.setVisibility(View.GONE);
            }

            Log.e(TAG, "onCreateView: user_year" + userYear.get(0));
            Log.e(TAG, "onCreateView: user_year" + userSection.get(0));
            if (userYear.get(0).equals("") || userYear.get(0).equals("null")) {
                user_found_year.setVisibility(View.GONE);
            }
            if (userSection.get(0).equals("") || userSection.get(0).equals("null")) {
                user_found_section.setVisibility(View.GONE);
            }


            userEmailIdTextView.setText(userEmailId);
            setAdapter();

            Log.e(TAG, "subyear and section of teachers " + userYear.get(0) + ">>" + userSection.get(0));


        }

        user_found_editButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                animationView.setVisibility(View.VISIBLE);

                userDivision.clear();
                userSubDivision.clear();
                userYear.clear();
                userSection.clear();

                user_found_branch.setVisibility(View.GONE);
                user_found_branch.setAdapter(null);
                user_found_section.setVisibility(View.GONE);
                user_found_section.setAdapter(null);
                user_found_year.setVisibility(View.GONE);
                user_found_year.setVisibility(View.GONE);

                user_found_editButton.setImageResource(R.drawable.done_icon);

                runQuery();

                user_found_done_btn.setVisibility(View.VISIBLE);
                user_found_editButton.setVisibility(View.GONE);

                func_for_spinner();


            }
        });

        user_found_done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getDivsion != null && getSubdividion_ != null) {
                    if (getyear_ == null && getSection == null) {
                        //runMutation();
                    } else {
                        //runMutation();
                    }
                }

            }
        });

        return view;
    }

    public void setAdapter() {

        ArrayAdapter divisionAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, userDivision);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_found_course.setAdapter(divisionAdapter);

        ArrayAdapter subDivisionAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, userSubDivision);
        subDivisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_found_branch.setAdapter(subDivisionAdapter);

        ArrayAdapter YearAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, userYear);
        YearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_found_year.setAdapter(YearAdapter);

        ArrayAdapter sectionAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, userSection);
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_found_section.setAdapter(sectionAdapter);

    }

    public void runQuery() {
        Log.e(TAG, "runQuery: inside the query" +mAWSAppSyncClient);
        mAWSAppSyncClient.query(GetInsDivQuery.builder().accKey("7").build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(getInsDiv);
    }

    private GraphQLCall.Callback<GetInsDivQuery.Data> getInsDiv = new GraphQLCall.Callback<GetInsDivQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<GetInsDivQuery.Data> response) {
            //Log.i("Results", response.data().listTodos().items().toString());
            Log.e(TAG, "onResponse: " + response.data().instituteDivision().result());
            if (response.data() != null) {
                String mResult = response.data().instituteDivision().result();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        animationView.setVisibility(View.GONE);
                        user_found_course.setEnabled(true);
                        user_found_branch.setEnabled(true);
                        user_found_year.setEnabled(true);
                        user_found_section.setEnabled(true);
                    }
                });
                try {
                    JSONArray jsonArray = new JSONArray(mResult.toString());
                    awsDataStore(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("ERROR", e.toString());
        }
    };

    private void awsDataStore(JSONArray jsonArray) throws JSONException {

        mData = new HashMap<String, LinkedList<DivisionAws>>();
        LinkedList<DivisionAws> mSubList = new LinkedList<DivisionAws>();

        for (int i = 0; i < jsonArray.length(); i++) {
            HashMap<String, String> mResultMap = jsonToMap(jsonArray.getJSONObject(i).toString());
            String div1name = mResultMap.get("div1name");
            String div1sec = mResultMap.get("div1sections");
            String div1yr = mResultMap.get("div1years");
            String div2name = mResultMap.get("div2name");
            String div2sec = mResultMap.get("div2sections");
            String div2yr = mResultMap.get("div2years");


            LinkedList<DivisionAws> mList = new LinkedList<DivisionAws>();

            if (mData.get(div1name) == null)
                mSubList = new LinkedList<DivisionAws>();
            DivisionAws mDivSubAws = new DivisionAws();
            mDivSubAws.setDivision1Name(div2name);
            mDivSubAws.setDivision1Sections(div2sec);
            mDivSubAws.setDivision1Years(div2yr);
            mSubList.add(mDivSubAws);

            DivisionAws mDivAws = new DivisionAws();
            mDivAws.setDivisionSub(mSubList);

            if (mData.get(div1name) == null) {
                mList.add(mDivAws);
                mData.put(div1name, mList);
            } else {
                mDivAws.setDivision1Name(div1name);
                mDivAws.setDivision1Sections(div1sec);
                mDivAws.setDivision1Years(div1yr);
                mData.get(div1name).add(mDivAws);
            }
            Log.e(TAG, "data1**>> " + mData.get(div1name).get(0).getDivisionSub().get(0).getDivision1Sections());
            Log.e(TAG, "data2**>> " + mData.get(div1name).get(0).getDivision1Sections());
        }
        Log.e("SahajLOG1234", "mData>> " + mData.size());
        if (mData.size() != 0)
            if (mData.keySet().iterator().next() != null)
                awsDataFiller1(mData);
    }

    private void awsDataFiller1(HashMap<String, LinkedList<DivisionAws>> map1) {

        for (Map.Entry<String,LinkedList<DivisionAws>> entry : map1.entrySet()) {
            Log.e(TAG, "<><><><><>><<><>><>< "+"Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());

        }

        FilteredList = new ArrayList();
        FilteredList.add("-:select:-");
        for (Map.Entry<String, LinkedList<DivisionAws>> entry : map1.entrySet()) {
            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            Log.e(TAG, "awsDataFiller1:  keys" + entry.getKey() + " values >>" + entry.getValue());
            FilteredList.add(entry.getKey());
        }




        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // Stuff that updates the UI
                divisonAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, FilteredList);
                divisonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                user_found_course.setAdapter(divisonAdapter);

            }
        });


        Log.e(TAG, "yooolist" + FilteredList);

    }

    public static HashMap jsonToMap(String t) throws JSONException {

        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = jObject.getString(key);
            map.put(key, value);

        }
        return map;
    }

    public void checkFAB(String userResponse) {

        userResponseList_fromSpinner.add(userResponse);

    }

    public void changeFAB() {

        Log.e(TAG, "changeFAB: " + userResponseList_fromSpinner.size());
        user_found_done_btn.setImageResource(R.drawable.done_icon_after);

    }


    public void func_for_spinner(){

        user_found_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                subdivision_list.clear();
                stores_year.clear();
                stores_section.clear();
                user_found_year.setVisibility(View.GONE);
                user_found_section.setVisibility(View.GONE);
                user_found_done_btn.setImageResource(R.drawable.done_icon);
                getDivsion = parent.getItemAtPosition(position).toString();
                if (!(getDivsion.equals("-:select:-"))) {

                    //users_newEmailid.clearFocus();
                    checkFAB(getDivsion);
                    LinkedList<DivisionAws> linkedList = mData.get(getDivsion);
                    DivisionAws divisionAws = linkedList.element();
                    LinkedList<DivisionAws> myarraylist = (LinkedList<DivisionAws>) divisionAws.getDivisionSub();
                    subdivision_list.add("-:select:-");
                    for (int k = 0; k < myarraylist.size(); k++) {

                        DivisionAws divisionAws1 = myarraylist.get(k);
                        stores_section.put(divisionAws1.getDivision1Name(), divisionAws1.getDivision1Sections());
                        stores_year.put(divisionAws1.getDivision1Name(), divisionAws1.getDivision1Years());
                        Log.e(TAG, "subdivsion on getdivison" + divisionAws1.getDivision1Name());
                        Log.e(TAG, "subDivision on getDivision  " + divisionAws1.getDivision1Years());
                        Log.e(TAG, "subDivision on getDivision " + divisionAws1.getDivision1Sections());

                        String sub_yo = (String) divisionAws1.getDivision1Name();
                        subdivision_list.add(sub_yo);

                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            ArrayAdapter subDivision_adapetr = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, subdivision_list);
                            subDivision_adapetr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            user_found_branch.setAdapter(subDivision_adapetr);
                            user_found_branch.setVisibility(View.VISIBLE);


                        }
                    }, 200);
                } else {

                    subdivision_list.clear();
                    user_found_branch.setVisibility(View.GONE);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        user_found_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                subYear_list.clear();
                subYear_list.clear();
                subSection_list.clear();
                user_found_section.setVisibility(View.GONE);
                user_found_done_btn.setImageResource(R.drawable.done_icon);
                user_found_year.setVisibility(View.GONE);
                getSubdividion_ = parent.getItemAtPosition(position).toString();
                Log.e(TAG, "onItemSelected: " + getSubdividion_);

                if (!getSubdividion_.equals("-:select:-")) {

                    subYear_list.add("-:select:-");
                    checkFAB(getSubdividion_);
                    String year_String = stores_year.get(getSubdividion_);

                    if (year_String.equals("null")) {

                        Log.e(TAG, "official management ");
                        user_found_section.setVisibility(View.GONE);
                        user_found_year.setVisibility(View.GONE);
                        changeFAB();

                    } else {

                        Log.e(TAG, "in else part ");
                        try {

                            for (int k = 1; k <= Integer.parseInt(stores_year.get(getSubdividion_)); k++) {

                                subYear_list.add(k);

                            }

                        } catch (Exception e) {

                            e.printStackTrace();

                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                ArrayAdapter storeYear_adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, subYear_list);
                                storeYear_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                user_found_year.setAdapter(storeYear_adapter);
                                user_found_year.setVisibility(View.VISIBLE);


                            }
                        }, 200);
                    }


                    subSection_list.add("-:select:-");

                    String section_String = stores_section.get(getSubdividion_);

                    if (section_String.equals("null")) {

                        Log.e(TAG, "official management");
                        user_found_year.setVisibility(View.GONE);
                        user_found_section.setVisibility(View.GONE);

                    } else {

                        int number_section = Integer.parseInt(stores_section.get(getSubdividion_));
                        for (int numSec = 65; numSec <= 65 + number_section; numSec++) {

                            subSection_list.add((char) numSec);
                            Log.e(TAG, "alphabet of english " + (char) numSec);

                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter storeSection_adapetr = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, subSection_list);
                                storeSection_adapetr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                user_found_section.setAdapter(storeSection_adapetr);
                                //select_teacher_section.setVisibility(View.VISIBLE);
                            }
                        }, 200);

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        user_found_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                subSection_list.clear();
//                select_teacher_section.setVisibility(View.GONE);
                //select_teacher_section.setVisibility(View.VISIBLE);
                //FAB_btn_bottomSheet.setImageResource(R.drawable.done_icon);
                getyear_ = parent.getItemAtPosition(position).toString();
                if (!getyear_.equals("-:select:-")) {

                    user_found_section.setVisibility(View.VISIBLE);
                    checkFAB(getyear_);
                    //String getNumberOfSection = stores_section.get(get)

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        user_found_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                getSection = parent.getItemAtPosition(position).toString();

                if (!getSection.equals("-:select:-")) {

                    checkFAB(getSection);
                    changeFAB();
                    //FAB_btn_bottomSheet.setEnabled(true);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
