package com.capa.capa.capa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashBoardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashBoardFragment extends Fragment {

    View dashboardView;
    private Button checkinBtn;
    private Button checkoutBtn;
    Timestamp checkInTime;
    Timestamp checkOutTime;
    private TextView displayCheckInTime;
    private TextView getDisplayCheckOutTime;
    private TextView displayPrice;
    private TextView displayFee;
    long checkInTimeStamp;
    long checkOutTimeStamp;
    int pricePerHour;
    private DatabaseReference firebaseDatabase;
    String fee;
    String startTime;
    String endTime;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DashBoardFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        char les = g;
        

        return inflater.inflate(R.layout.fragment_dash_board, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkinBtn = (Button) getActivity().findViewById(R.id.checkInBtn);
        checkoutBtn = (Button) getActivity().findViewById(R.id.checkOutBtn);
        displayCheckInTime = (TextView) getActivity().findViewById(R.id.startTimeTextView);
        getDisplayCheckOutTime = (TextView) getActivity().findViewById(R.id.endTimeTextView);
        displayPrice = getActivity().findViewById(R.id.priceTextView);
        displayFee = getActivity().findViewById(R.id.feeTextView);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();



        checkinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getRandomNumberChecker() == true){


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Have you place card on the RFID sensor ?").setPositiveButton("COMFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            checkInTimeStamp = System.currentTimeMillis();
                             startTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
                            displayCheckInTime.setText(startTime);
                            pricePerHour = getPrice();
                            String price = String.valueOf(pricePerHour);
                            displayPrice.setText(price+"per hour");
                            checkinBtn.setEnabled(false);
                            checkinBtn.setVisibility(View.GONE);


                        }
                    })
                            .setNegativeButton("NOT READY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Toast.makeText(getActivity(),"Please Place your card on the RFID sensor",Toast.LENGTH_SHORT).show();

                                }
                            });

                    builder.create().show();

                }else if (getRandomNumberChecker() == false){

                    Toast.makeText(getActivity(),"Please Place your card on the RFID sensor",Toast.LENGTH_SHORT).show();

                }
            }
        });
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getRandomNumberChecker() == true){


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Have you place card on the RFID sensor ?").setPositiveButton("COMFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            checkOutTimeStamp = System.currentTimeMillis();
                            long duration = checkOutTimeStamp-checkInTimeStamp;
                            long totalFee = (long) (duration*pricePerHour*2.8*Math.pow(10,-7));




                            endTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
                            getDisplayCheckOutTime.setText(endTime);
                            fee=String.valueOf(totalFee);
                            displayFee.setText("$ "+String.valueOf(totalFee));
                            updateHistory();
                            checkinBtn.setEnabled(false);
                            checkoutBtn.setVisibility(View.GONE);




                        }
                    })
                            .setNegativeButton("NOT READY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Toast.makeText(getActivity(),"Please Place your card on the RFID sensor",Toast.LENGTH_SHORT).show();

                                }
                            });

                    builder.create().show();

                }else if (getRandomNumberChecker() == false){

                    Toast.makeText(getActivity(),"Please Place your card on the RFID sensor",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //To get current time
    private String getCurrentTime(){
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        return ts;
    }
    //To get a random number
    private boolean getRandomNumberChecker(){

        Random randomGenerator = new Random();
        int randomNum = randomGenerator.nextInt(10);
        if (randomNum<8){
            return false;
        }else {
            return true;
        }

    }
    private int getPrice(){
        Random randomGenerator = new Random();
        int randomNum = (randomGenerator.nextInt(5)+15);
        return randomNum;
    }



    private void updateHistory(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        if(user != null){

            firebaseDatabase.child("users").child(uid).child("History").child(String.valueOf(checkInTimeStamp)).child("startTime").setValue(startTime);
            firebaseDatabase.child("users").child(uid).child("History").child(String.valueOf(checkInTimeStamp)).child("endTime").setValue(endTime);
            firebaseDatabase.child("users").child(uid).child("History").child(String.valueOf(checkInTimeStamp)).child("fee").setValue(fee);

        }

    }





}
