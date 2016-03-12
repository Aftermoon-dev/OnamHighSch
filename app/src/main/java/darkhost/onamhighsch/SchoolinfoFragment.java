package darkhost.onamhighsch;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by 민재 on 2016-03-08.
 */
public class SchoolinfoFragment extends Fragment {

    public static SchoolinfoFragment newInstance() {
        SchoolinfoFragment fragment = new SchoolinfoFragment();
        return fragment;
    }

    public SchoolinfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schoolinfo, container, false);

        ImageView Logo = (ImageView) view.findViewById(R.id.logoimage);
        Picasso.with(getActivity().getApplicationContext()).load(R.mipmap.ic_launcher).into(Logo);

        ImageView Tree = (ImageView) view.findViewById(R.id.treeimage);
        Picasso.with(getActivity().getApplicationContext()).load(R.drawable.tree).into(Tree);

        ImageView Flower = (ImageView) view.findViewById(R.id.flowerimage);
        Picasso.with(getActivity().getApplicationContext()).load(R.drawable.flower).into(Flower);

        ImageView Animal = (ImageView) view.findViewById(R.id.animalimage);
        Picasso.with(getActivity().getApplicationContext()).load(R.drawable.animal).into(Animal);

        return view;
    }
}
