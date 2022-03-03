package com.codescannerqr.generator.view.fragments.generateCodes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentSpotifiBinding;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

public class SpotifyFragment extends Fragment {

    private FragmentSpotifiBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSpotifiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity)requireActivity()).setTextViewToolbar(getString(R.string.spotify));
        ((GenerateCodeActivity)requireActivity()).setIconToolbar(R.drawable.ic_gen_spotify);

        binding.buttonCreateSpotify.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (
                    !binding.editTextArtistSpotify.getText().toString().equals("") &&
                    !binding.editTextSongSpotify.getText().toString().equals("")){
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.spotify));
                bundle.putInt("argIcon", R.drawable.ic_gen_spotify);

                ((GenerateCodeActivity)requireActivity()).navControllerGenerate.
                        navigate(R.id.action_spotifyFragment_to_generatedFragment, bundle);
            }
            else{
                Toast.makeText(requireActivity(), getString(R.string.field_empty),Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private String createDataInBundle() {
        //SPOTIFY
        return "spotify:search:"+
                binding.editTextArtistSpotify.getText().toString()+","+
                binding.editTextSongSpotify.getText().toString();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}