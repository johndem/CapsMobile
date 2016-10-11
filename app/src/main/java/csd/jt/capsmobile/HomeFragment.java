package csd.jt.capsmobile;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);



        //Set Categories Listeners
        ImageButton cat1 = (ImageButton) rootView.findViewById(R.id.cat1);
        ImageButton cat2 = (ImageButton) rootView.findViewById(R.id.cat2);
        ImageButton cat3 = (ImageButton) rootView.findViewById(R.id.cat3);
        ImageButton cat4 = (ImageButton) rootView.findViewById(R.id.cat4);
        ImageButton cat5 = (ImageButton) rootView.findViewById(R.id.cat5);
        ImageButton cat6 = (ImageButton) rootView.findViewById(R.id.cat6);

        cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                intent.putExtra("category", "Healthcare");
                startActivity(intent);
            }
        });

        cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                intent.putExtra("category", "Animals");
                startActivity(intent);
            }
        });

        cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                intent.putExtra("category", "Communities");
                startActivity(intent);
            }
        });

        cat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                intent.putExtra("category", "Education");
                startActivity(intent);
            }
        });

        cat5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                intent.putExtra("category", "Environment");
                startActivity(intent);
            }
        });

        cat6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                intent.putExtra("category", "Emergency");
                startActivity(intent);
            }
        });



        return rootView;
    }

}
