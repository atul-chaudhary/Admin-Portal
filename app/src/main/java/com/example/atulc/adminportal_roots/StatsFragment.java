package com.example.atulc.adminportal_roots;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.amazonaws.demo.posts.GetStatsQuery;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.json.JSONArray;
import org.json.JSONException;

import javax.annotation.Nonnull;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class StatsFragment extends Fragment {
    private AWSAppSyncClient mAWSAppSyncClient;
    TextView teacher_number;
    TextView student_number;
    TextView Admin_number;
    LottieAnimationView animationView_stats;
    private static final String TAG = "StatsFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stats_layout_fragment, container, false);

        animationView_stats = view.findViewById(R.id.animation_view_stats);
        animationView_stats.setVisibility(View.VISIBLE);

        mAWSAppSyncClient = ClientFactory.getInstance(getContext());

        teacher_number = view.findViewById(R.id.teacher_number);
        student_number = view.findViewById(R.id.student_number);
        Admin_number = view.findViewById(R.id.Admin_number);

        Button btn_add_users = view.findViewById(R.id.btn_add_users);
        btn_add_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment usersFragment = new UsersFragment();
                getFragmentManager().beginTransaction().replace(R.id.card_view_main, usersFragment).commit();

            }
        });


        runQuery();

        return view;
    }

    public void runQuery() {
        mAWSAppSyncClient.query(GetStatsQuery.builder().accKey("7").email("drophouse0199@gmail.com").mcase("2").build())
                .responseFetcher(AppSyncResponseFetchers.NETWORK_ONLY)
                .enqueue(getStats);
    }

    private GraphQLCall.Callback<GetStatsQuery.Data> getStats = new GraphQLCall.Callback<GetStatsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<GetStatsQuery.Data> response) {

            Log.e(TAG, "onResponse: " + response.data());
            final String teacher_numbers=response.data().getData().teachers();
            final String student_numbers = response.data().getData().students();
            final String admin_numbers = response.data().getData().admins();

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    // Stuff that updates the UI

                    teacher_number.setText(teacher_numbers);
                    student_number.setText(student_numbers);
                    Admin_number.setText(admin_numbers);
                    animationView_stats.setVisibility(View.GONE);


                }
            });


        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
            //Toast.makeText(getContext(), "SomeThing went Wrong", Toast.LENGTH_SHORT).show();
        }
    };

}
