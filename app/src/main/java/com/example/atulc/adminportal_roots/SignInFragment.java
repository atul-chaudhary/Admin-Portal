package com.example.atulc.adminportal_roots;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class SignInFragment extends Fragment {

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private static final String TAG = "SignInFragment";
    String pinMain = "7819";
    private int currentApiVersion;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signin_fragment, container, false);
        Log.d(TAG, "onCreateView: ");


        //to hide the status bar inside the fragment
//        getActivity().getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        currentApiVersion = android.os.Build.VERSION.SDK_INT;
//        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
//            getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
//            final View decorView = getActivity().getWindow().getDecorView();
//            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//                @Override
//                public void onSystemUiVisibilityChange(int visibility) {
//                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
//                        decorView.setSystemUiVisibility(flags);
//                    }
//                }
//            });
//        }

        mPinLockView = (PinLockView) view.findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) view.findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);

        return view;
    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {

            Log.d(TAG, "onComplete: >>>>"+pin);
            Log.d(TAG, "onComplete: main >>>"+pinMain);
            if (pin.equals(pinMain)) {
                Log.d(TAG, "correct pin ");
                Fragment fragment = new MainLayoutFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            } else {
                Log.d(TAG, " incorrect pin ");
            }

        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };


}
