package com.example.atulc.adminportal_roots;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.demo.posts.AddUsersMutation;
import com.amazonaws.demo.posts.GetInsDivQuery;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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

public class MainLayoutFragment extends Fragment {

    private static final String TAG = "MainLayoutFragment";
    public BottomSheetBehavior bottomSheetBehavior;
    private AWSAppSyncClient mAWSAppSyncClient;
    public TextView bottomSheet_TextView;
    HashMap<String, LinkedList<DivisionAws>> mData;
    ArrayList FilteredList;
    ArrayAdapter divisonAdapter;
    EditText users_newEmailid;
    Spinner select_users_division;
    Spinner select_users_subdivision;
    Spinner select_users_year;
    Spinner select_teacher_section;
    FloatingActionButton FAB_btn_bottomSheet;
    String getDivsion;
    String getSubdividion_;
    String getyear_;
    String getSection;
    String mCase = "1";
    String userTypeId = "1";
    ArrayList<String> userResponseList_fromSpinner = new ArrayList<>();
    ArrayList subdivision_list = new ArrayList();
    ArrayList subYear_list = new ArrayList();
    ArrayList subSection_list = new ArrayList();
    HashMap<String, String> stores_section = new HashMap<>();
    HashMap<String, String> stores_year = new HashMap<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_layout_fragment, container, false);

        mAWSAppSyncClient = ClientFactory.getInstance(getContext());

//        UsersFragment.initListener(this);
        //for changing names logo and icon of institute
        ImageView institutImage = (ImageView) view.findViewById(R.id.institute_logo);
        TextView instituteName = (TextView) view.findViewById(R.id.institute_name);
        ImageView vertDots = (ImageView) view.findViewById(R.id.indicator_dots);

        //for changing the access level and profile of admin
        TextView accessLevel = (TextView) view.findViewById(R.id.access_level);
        ImageView admin_profile = (ImageView) view.findViewById(R.id.admin_image);

        //main cardview layout in fragment
        CardView card_main_layout = (CardView) view.findViewById(R.id.card_view_main);


        //bottom sheet layout for users
        View cardView_teachers = view.findViewById(R.id.cardview_layout_teachers);
        ImageView bottomSheet_close_btn = (ImageView) view.findViewById(R.id.close_btn_bottomSheet);
        users_newEmailid = (EditText) view.findViewById(R.id.new_users_emailid_bottomSheet);
        bottomSheet_TextView = (TextView) view.findViewById(R.id.bottomSheet_textView);
        FAB_btn_bottomSheet = view.findViewById(R.id.FAB_btn_bottomSheet);


        select_users_division = (Spinner) view.findViewById(R.id.select_users_division);
        select_users_subdivision = (Spinner) view.findViewById(R.id.select_users_subdivision);
        select_users_year = (Spinner) view.findViewById(R.id.select_users_year);
        select_teacher_section = (Spinner) view.findViewById(R.id.select_users_section);


        select_users_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                subdivision_list.clear();
                stores_year.clear();
                stores_section.clear();
                select_users_year.setVisibility(View.GONE);
                select_teacher_section.setVisibility(View.GONE);
                FAB_btn_bottomSheet.setImageResource(R.drawable.done_icon);
                getDivsion = parent.getItemAtPosition(position).toString();
                if (!(getDivsion.equals("-:select:-"))) {

                    users_newEmailid.clearFocus();
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
                            select_users_subdivision.setAdapter(subDivision_adapetr);
                            select_users_subdivision.setVisibility(View.VISIBLE);


                        }
                    }, 200);
                } else {

                    subdivision_list.clear();
                    select_users_subdivision.setVisibility(View.GONE);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        select_users_subdivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                subYear_list.clear();
                subYear_list.clear();
                subSection_list.clear();
                select_teacher_section.setVisibility(View.GONE);
                FAB_btn_bottomSheet.setImageResource(R.drawable.done_icon);
                select_users_year.setVisibility(View.GONE);
                getSubdividion_ = parent.getItemAtPosition(position).toString();
                Log.e(TAG, "onItemSelected: " + getSubdividion_);

                if (!getSubdividion_.equals("-:select:-")) {

                    subYear_list.add("-:select:-");
                    checkFAB(getSubdividion_);
                    String year_String = stores_year.get(getSubdividion_);

                    if (year_String.equals("null")) {

                        Log.e(TAG, "official management ");
                        select_teacher_section.setVisibility(View.GONE);
                        select_users_year.setVisibility(View.GONE);
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
                                select_users_year.setAdapter(storeYear_adapter);
                                select_users_year.setVisibility(View.VISIBLE);


                            }
                        }, 200);
                    }


                    subSection_list.add("-:select:-");

                    String section_String = stores_section.get(getSubdividion_);

                    if (section_String.equals("null")) {

                        Log.e(TAG, "official management");
                        select_users_year.setVisibility(View.GONE);
                        select_teacher_section.setVisibility(View.GONE);

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
                                select_teacher_section.setAdapter(storeSection_adapetr);
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

        select_users_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                subSection_list.clear();
//                select_teacher_section.setVisibility(View.GONE);
                //select_teacher_section.setVisibility(View.VISIBLE);
                FAB_btn_bottomSheet.setImageResource(R.drawable.done_icon);
                getyear_ = parent.getItemAtPosition(position).toString();
                if (!getyear_.equals("-:select:-")) {

                    select_teacher_section.setVisibility(View.VISIBLE);
                    checkFAB(getyear_);
                    //String getNumberOfSection = stores_section.get(get)

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        select_teacher_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                getSection = parent.getItemAtPosition(position).toString();

                if (!getSection.equals("-:select:-")) {

                    checkFAB(getSection);
                    changeFAB();
                    FAB_btn_bottomSheet.setEnabled(true);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        FAB_btn_bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (users_newEmailid.getText().toString().matches("")) {
                    Toast.makeText(getContext(), "Please Enter Email ID", Toast.LENGTH_SHORT).show();
                } else {
                    if (getDivsion != null && getSubdividion_ != null) {
                        if (getyear_ == null && getSection == null) {
                            runMutation();
                        } else {
                            runMutation();
                        }
                    }

                    //Log.e(TAG, "onClick: <>>>>>>>>>>>>>>>>>>>>>>"+getyear_+".,,,,,,,,," +getSection);
                }
            }
        });

        bottomSheetBehavior = BottomSheetBehavior.from(cardView_teachers);

        bottomSheet_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

                switch (i) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        users_newEmailid.getText().clear();
                        users_newEmailid.clearFocus();
                        select_users_division.setSelection(0);
                        select_users_subdivision.setVisibility(View.GONE);
                        select_users_year.setVisibility(View.GONE);
                        select_teacher_section.setVisibility(View.GONE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        //changes the fragment
        Fragment fragment = new StatsFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.card_view_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        runQuery();

        return view;
    }

    public void runQuery() {
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
            Log.e("SahajLOG1234", "data1**>> " + mData.get(div1name).get(0).getDivisionSub().get(0).getDivision1Sections());
            Log.e("SahajLOG1234", "data2**>> " + mData.get(div1name).get(0).getDivision1Sections());
        }
        Log.e("SahajLOG1234", "mData>> " + mData.size());
        if (mData.size() != 0)
            if (mData.keySet().iterator().next() != null)
                awsDataFiller1(mData);
    }

    private void awsDataFiller1(HashMap<String, LinkedList<DivisionAws>> map1) {

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
                select_users_division.setAdapter(divisonAdapter);

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

    BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment selectedFragment = null;
            switch (menuItem.getItemId()) {

                case R.id.home:
                    selectedFragment = new StatsFragment();
                    break;

                case R.id.add_ban_user:
                    selectedFragment = new UsersFragment();
                    break;

                case R.id.add_edit_admin:
                    selectedFragment = new AdminFragment();
                    break;

            }

            getFragmentManager().beginTransaction().replace(R.id.card_view_main, selectedFragment).commit();

            return true;

        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        Log.e(TAG, "event buses<<<>>>" + event.message);
        int i = Integer.valueOf(event.message);

        switch (i) {
            case 1:
                bottomSheet_TextView.setText("Add Teachers");
                mCase = "1";
                userTypeId = "1";
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case 2:
                bottomSheet_TextView.setText("Add Student");
                mCase = "2";
                userTypeId = "2";
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case 3:
                bottomSheet_TextView.setText("Add Admin");
                mCase = "3";
                userTypeId = "1";
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;


        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void runMutation() {

        //macse value 1 for teachers
        //2 for student
        //3 for admin

        Log.e(TAG, "runMutation: ");
        AddUsersMutation addUsersMutation = AddUsersMutation.builder().
                mCase(mCase).
                accKey("7").
                userTypeId(userTypeId).
                email(users_newEmailid.getText().toString()).
                div1Name(getDivsion).
                div2Name(getSubdividion_).
                divYear(getyear_).
                divSection(getSection).
                build();

        mAWSAppSyncClient.mutate(addUsersMutation).enqueue(mutationCallback);
    }

    private GraphQLCall.Callback<AddUsersMutation.Data> mutationCallback = new GraphQLCall.Callback<AddUsersMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<AddUsersMutation.Data> response) {

            Log.e(TAG, "add users techer " + response.data().newUser().result());
            final String mResultMutation = response.data().newUser().result();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getContext(), mResultMutation, Toast.LENGTH_SHORT).show();
                    //FAB_btn_bottomSheet.setEnabled(false);
                    users_newEmailid.getText().clear();
                    users_newEmailid.clearFocus();
                    select_users_division.setSelection(0);
                    select_users_subdivision.setVisibility(View.GONE);
                    select_users_year.setVisibility(View.GONE);
                    select_teacher_section.setVisibility(View.GONE);

                }
            });

        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };

    public void checkFAB(String userResponse) {

        userResponseList_fromSpinner.add(userResponse);

    }

    public void changeFAB() {

        Log.e(TAG, "changeFAB: " + userResponseList_fromSpinner.size());
        FAB_btn_bottomSheet.setImageResource(R.drawable.done_icon_after);

    }

}
