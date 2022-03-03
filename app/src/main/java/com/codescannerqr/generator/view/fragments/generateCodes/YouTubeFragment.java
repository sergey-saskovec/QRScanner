package com.codescannerqr.generator.view.fragments.generateCodes;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentYouTubeBinding;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

public class YouTubeFragment extends Fragment {

    private FragmentYouTubeBinding binding;
    private String typeURL = "";

        @Override
        public View onCreateView (@NotNull LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState){
            binding = FragmentYouTubeBinding.inflate(inflater, container, false);
            View root = binding.getRoot();
            ((GenerateCodeActivity)requireActivity()).setTextViewToolbar(getString(R.string.youtube));
            ((GenerateCodeActivity)requireActivity()).setIconToolbar(R.drawable.ic_gen_youtube);

            clickYouTubeURL();

            binding.textViewURLYouTube.setOnClickListener(v -> clickYouTubeURL());

            binding.textViewVideoIDYotube.setOnClickListener(v -> clickYouTubeVideoID());

            binding.textViewChannelIDYoutube.setOnClickListener(v -> clickYouTubeChannelID());

            binding.buttonCreateYoutube.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                if (!binding.editTextYouTube.getText().toString().equals("")){
                    bundle.putString("arg", createDataInBundle());
                    bundle.putString("argTitle", getString(R.string.youtube));
                    bundle.putInt("argIcon", R.drawable.ic_gen_youtube);

                    ((GenerateCodeActivity)requireActivity()).navControllerGenerate.
                            navigate(R.id.action_youTubeFragment_to_generatedFragment, bundle);
                }
                else{
                    Toast.makeText(getActivity(), getString(R.string.field_empty),Toast.LENGTH_LONG).show();
                }
            });

            return root;
        }

    private String createDataInBundle() {
        //YOUTUBE
        if (typeURL.equals("ChannelID")){
            return "https://www.youtube.com/channel/"+binding.editTextYouTube.getText().toString();
        }
        else if (typeURL.equals("VideoID")){
            return "https://www.youtube.com/watch?v="+binding.editTextYouTube.getText().toString();
        }
        else{
            return binding.editTextYouTube.getText().toString();
        }
    }

    private void clickYouTubeURL(){
        binding.textViewURLYouTube.setTextColor(requireActivity().getColor(R.color.white));
        binding.textViewURLYouTube.setBackgroundResource(R.drawable.shape_oval_long_text);
        binding.textViewVideoIDYotube.setTextColor(requireActivity().getColor(R.color.orange));
        binding.textViewVideoIDYotube.setBackgroundResource(R.drawable.shape_oval);
        binding.textViewChannelIDYoutube.setTextColor(requireActivity().getColor(R.color.orange));
        binding.textViewChannelIDYoutube.setBackgroundResource(R.drawable.shape_oval);
        binding.textInputEditTextYouTube.setHint(R.string.youtube_url);
        typeURL = "URL";
    }

    private void clickYouTubeVideoID(){
        binding.textViewVideoIDYotube.setTextColor(requireActivity().getColor(R.color.white));
        binding.textViewVideoIDYotube.setBackgroundResource(R.drawable.shape_oval_long_text);
        binding.textViewURLYouTube.setTextColor(requireActivity().getColor(R.color.orange));
        binding.textViewURLYouTube.setBackgroundResource(R.drawable.shape_oval);
        binding.textViewChannelIDYoutube.setTextColor(requireActivity().getColor(R.color.orange));
        binding.textViewChannelIDYoutube.setBackgroundResource(R.drawable.shape_oval);
        binding.textInputEditTextYouTube.setHint(R.string.youtube_video_id);
        typeURL = "VideoID";
    }

    private void clickYouTubeChannelID(){
        binding.textViewChannelIDYoutube.setTextColor(requireActivity().getColor(R.color.white));
        binding.textViewChannelIDYoutube.setBackgroundResource(R.drawable.shape_oval_long_text);
        binding.textViewURLYouTube.setTextColor(requireActivity().getColor(R.color.orange));
        binding.textViewURLYouTube.setBackgroundResource(R.drawable.shape_oval);
        binding.textViewVideoIDYotube.setTextColor(requireActivity().getColor(R.color.orange));
        binding.textViewVideoIDYotube.setBackgroundResource(R.drawable.shape_oval);
        binding.textInputEditTextYouTube.setHint(R.string.youtube_channel_id);
        typeURL = "ChannelID";
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    }