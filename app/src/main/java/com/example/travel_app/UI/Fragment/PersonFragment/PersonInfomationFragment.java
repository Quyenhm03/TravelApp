package com.example.travel_app.UI.Fragment.PersonFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.travel_app.R;

import com.example.travel_app.UI.Activity.FavoriteLocationActivity;
import com.example.travel_app.UI.Login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class PersonInfomationFragment extends Fragment {
    private CardView userInformation, favoriteTravel, roomBooked, ticketsBooked, logoutAccount;
    private FirebaseAuth mAuth;

    public PersonInfomationFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_infomation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        initViews();
    }

    private void initViews() {
        userInformation = requireView().findViewById(R.id.userInformation);
        favoriteTravel = requireView().findViewById(R.id.favoriteTravel);
        roomBooked = requireView().findViewById(R.id.roomBooked);
        ticketsBooked = requireView().findViewById(R.id.ticketsBooked);
        logoutAccount = requireView().findViewById(R.id.logoutAccount);
        initActions();
    }

    private void initActions() {
        userInformation.setOnClickListener(view -> openUserDetailFragment());
        favoriteTravel.setOnClickListener(view -> openFavoriteTravelFragment());
        roomBooked.setOnClickListener(view -> openRoomBookedFragment());
        ticketsBooked.setOnClickListener(view -> openTicketsBookedFragment());
        logoutAccount.setOnClickListener(view -> LogoutAccount());

    }

    private void openUserDetailFragment() {
        Intent intent = new Intent(getActivity(), UpdateInfoUserActivity.class);
        startActivity(intent);
    }

    private void openFavoriteTravelFragment() {
        Intent intent = new Intent(getActivity(), FavoriteLocationActivity.class);
        startActivity(intent);
    }

    private void openRoomBookedFragment(){
        
    }

    private void openTicketsBookedFragment(){

    }

    private void LogoutAccount(){
        mAuth.signOut();
        Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finishAffinity();
    }
}