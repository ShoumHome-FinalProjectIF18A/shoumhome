package id.shoumhome.android.ui.beranda;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import id.shoumhome.android.R;
import id.shoumhome.android.activityHandler.ArtikelActivity;

public class BerandaFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_beranda, container, false);

        return root;
    }

}
