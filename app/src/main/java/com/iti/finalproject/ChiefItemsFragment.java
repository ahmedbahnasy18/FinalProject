package com.iti.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChiefItemsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChiefItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChiefItemsFragment extends Fragment {

    private static final int ITEM_REQCODE = 1;

    private List<Item> itemsList;
    private RecyclerView recyclerView;
    private ItemAdapter iAdapter;
    FloatingActionButton floatingActionButton;
    //private List<Item> currentBasket = new ArrayList<>();
    TextView basketNo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAMOBJ = "paramObj";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Chief mChief;

    private OnFragmentInteractionListener mListener;

    public ChiefItemsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChiefItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChiefItemsFragment newInstance(String param1, String param2) {
        ChiefItemsFragment fragment = new ChiefItemsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ChiefItemsFragment objInstance(Chief cheif ) {
        ChiefItemsFragment fragment = new ChiefItemsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAMOBJ,cheif);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this method must be invoked in fragment to perform onCreateOptionsMenu and all optionsMenu methods
        setHasOptionsMenu(true);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mChief = getArguments().getParcelable(ARG_PARAMOBJ);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.mainmenu, menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.info){
//            Intent intent = new Intent(getActivity(),ChiefInfo.class);
//            intent.putExtra("CHEIF_INFO",mChief);
//            startActivity(intent);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ITEM_REQCODE) {
            if(resultCode == RESULT_OK){
                getView().findViewById(R.id.basket_button_layout).setVisibility(View.VISIBLE);
                basketNo.setText(String.valueOf(Integer.parseInt(basketNo.getText().toString()) + data.getIntExtra("QUANTITY",0)));
                for(int i = 0; i < data.getIntExtra("QUANTITY",0); i++){
                    ((HomeActivity) getActivity()).currentBasket.add((Item) data.getParcelableExtra("RETURN_ITEM"));
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(mChief.getName());
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chief_items, container, false);
        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.fab);
        basketNo = (TextView) v.findViewById(R.id.basket_no);
        //setTitle(cheif.getName());
        basketNo.setText(String.valueOf(((HomeActivity) getActivity()).currentBasket.size()));
        if(((HomeActivity) getActivity()).currentBasket.size() > 0){
            v.findViewById(R.id.basket_button_layout).setVisibility(View.VISIBLE);
        }
        ((HomeActivity) getActivity()).isItemsFragmentVisible = true;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Basket_fragment basket_fragment = Basket_fragment.objInstance(((ArrayList<Item>) ((HomeActivity) getActivity()).currentBasket), mChief);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home,basket_fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemsList = new ArrayList<>(mChief.getMenuItems());
        iAdapter = new ItemAdapter(getActivity(), itemsList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {


                Intent i = new Intent(getActivity(),BasketDialogActivity.class);
                i.putExtra("ITEM_INTENT",itemsList.get(position));
                startActivityForResult(i,ITEM_REQCODE);
            }
        });
        recyclerView.setAdapter(iAdapter);

        return v;
    }

    @Override
    public void onStop() {
        ((HomeActivity) getActivity()).isItemsFragmentVisible = false;
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
