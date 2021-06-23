package com.example.bookshare.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bookshare.R;
import com.example.bookshare.UserBooks;
import com.example.bookshare.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapViewModel mapViewModel;
    private SupportMapFragment supportMapFragment;
    private GoogleMap gMap;
    private LinearLayout detailLayout;
    private Button btnHideDetail;
    private TextView markerTitle, bookCount, sellBookCount, rentBookCount, btnViewBooks;

    private ArrayList<User> userList = new ArrayList<User>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel =
                ViewModelProviders.of(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        mapViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        //initialize map fragment
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        //async map
        supportMapFragment.getMapAsync(this);
        userList.add(new User(23.747925,90.418767,"Rakib Hasan",7, 3,4));
        userList.add(new User(23.741297,90.410976,"Akbar Ali",3, 2,1));
        userList.add(new User(23.740855,90.419656,"Rudra Sen",0,0,0));
        userList.add(new User(23.743478,90.417488,"Shishir Kusum",5,3,2));
        userList.add(new User(23.7447318,90.4097382,"Rezaul Karim",2,1,1));
        userList.add(new User(23.7345727,90.414023,"Moyeen Ahmed",4,3,1));
        userList.add(new User(23.732421,90.415375,"Abdur Rahman",6,4,2));
        userList.add(new User(24,891245,90.365789,"Farhana Iffat",8,2,1 ));


        detailLayout = (LinearLayout) root.findViewById(R.id.detailLayout);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) detailLayout.getLayoutParams();
        layoutParams.setMargins( 0 , 0 , 0 , getBoottomNavHeight(root)) ;
        Button okButton= new Button( getContext() ) ;
        okButton.setText( "some text" ) ;
        okButton.setVisibility(View.GONE);
        detailLayout.addView(okButton , layoutParams) ;
        detailLayout.setMinimumHeight(getBoottomNavHeight(root));
        detailLayout.setVisibility(View.GONE);

        markerTitle = (TextView) root.findViewById(R.id.marker_title);
        bookCount = (TextView) root.findViewById(R.id.book_count);
        sellBookCount = (TextView) root.findViewById(R.id.sell_book_count);
        rentBookCount = (TextView) root.findViewById(R.id.rent_book_count);

        btnHideDetail = (Button) root.findViewById(R.id.btn_hide);
        btnHideDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailLayout.setVisibility(View.GONE);
            }
        });

        btnViewBooks = (TextView) root.findViewById(R.id.btn_view_books);

        return root;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //when map is loaded
        gMap = googleMap;
        LatLng myLocation = new LatLng(23.7433969,90.4151673);

        //showing some locations on map
        //from user list
        for(User u : userList){
            Marker marker = gMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(u.latitude,u.longitude))
                            .title(u.title)
                            .snippet("Books: "+u.bookCount)
            );
            marker.setTag(x);
            marker.showInfoWindow();
        }


        //zooming the map around my location
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                myLocation, 18
        ));

        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                markerTitle.setText(marker.getTitle());
                detailLayout.setVisibility(View.VISIBLE);
                final User u = (User) marker.getTag();
                bookCount.setText(u.bookCount+" books uploaded");
                if(u.bookCount>0){
                    sellBookCount.setVisibility(View.VISIBLE);
                    rentBookCount.setVisibility(View.VISIBLE);
                    btnViewBooks.setVisibility(View.VISIBLE);
                    sellBookCount.setText("Sell books: "+u.sellBookCount+" ; ");
                    rentBookCount.setText("Rent books: "+u.rentBookCount);
                }else {
                    sellBookCount.setVisibility(View.GONE);
                    rentBookCount.setVisibility(View.GONE);
                    btnViewBooks.setVisibility(View.GONE);
                }
                btnViewBooks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), UserBooks.class);
                        intent.putExtra("user", u);
                        startActivity(intent);
                    }
                });
                
                return false;
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                //when clicked on map
                //initialize marker options
                //MarkerOptions markerOptions = new MarkerOptions();

                //set position of marker
                //markerOptions.position(myLocation);

                //set title of marker
                //markerOptions.title("Latitude:"+latLng.latitude+" ; Longitude:"+latLng.longitude);

                //remove all marker
                //gMap.clear();

                //add marker on map
                //gMap.addMarker(markerOptions);

                //Animating to zoom the marker
                //gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));
            }
        });
    }

    private int getBoottomNavHeight(View root){
        View view = getActivity().findViewById(R.id.bottom_nav_view);
        return view.getHeight();
    }
}