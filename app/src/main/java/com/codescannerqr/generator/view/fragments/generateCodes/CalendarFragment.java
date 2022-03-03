package com.codescannerqr.generator.view.fragments.generateCodes;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.codescannerqr.generator.R;
import com.codescannerqr.generator.databinding.FragmentCalendarBinding;
import com.codescannerqr.generator.view.activity.GenerateCodeActivity;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;
    Calendar calendar = Calendar.getInstance();

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((GenerateCodeActivity) requireActivity()).setTextViewToolbar(getString(R.string.calendar));
        ((GenerateCodeActivity) requireActivity()).setIconToolbar(R.drawable.ic_gen_calendar);

        setClickView();

        getTimeZone();
        return root;
    }

    private void setClickView() {
        binding.editTextContentCalendar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                binding.textViewCountSymbolsCalendar.setText(
                        binding.editTextContentCalendar.getText().length() + "/300");

            }
        });

        binding.buttonCreateCalendar.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (!binding.editTextTitleCalendar.getText().toString().equals("") &&
                    !binding.editTextaddressCalendar.getText().toString().equals("") &&
                    !binding.editTextContentCalendar.getText().toString().equals("") &&
                    !binding.autotextViewTimeZoneCalendar.getText().toString().equals("") &&
                    !binding.textViewStartTime.getText().toString().equals("") &&
                    !binding.textViewStartDate.getText().toString().equals("") &&
                    !binding.textViewEndDate.getText().toString().equals("")) {
                bundle.putString("arg", createDataInBundle());
                bundle.putString("argTitle", getString(R.string.calendar));
                bundle.putInt("argIcon", R.drawable.ic_gen_calendar);

                ((GenerateCodeActivity) requireActivity()).navControllerGenerate.
                        navigate(R.id.action_calendarFragment_to_generatedFragment, bundle);
            } else {
                Toast.makeText(getActivity(), getString(R.string.field_empty), Toast.LENGTH_LONG).show();
            }
        });

        binding.cardViewCalendarStart.setOnClickListener(v -> getDateTime(R.id.cardViewCalendarStart));
        binding.cardViewCalendarEnd.setOnClickListener(v -> getDateTime(R.id.cardViewCalendarEnd));

    }

    private void getTimeZone() {
        String[] tz = TimeZone.getAvailableIDs();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.select_dialog_singlechoice,
                tz
        );
        binding.autotextViewTimeZoneCalendar.setThreshold(1);
        binding.autotextViewTimeZoneCalendar.setAdapter(adapter);
    }

    private void getDateTime(int res) {
        final Calendar currentDate = Calendar.getInstance();
        calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") DatePickerDialog datePickerDialog =
                new DatePickerDialog(getContext(), (
                datePicker, year, monthOfYear, dayOfMonth) -> {

            calendar.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                if (res == R.id.cardViewCalendarStart) {
                    binding.textViewStartTime.setText(new SimpleDateFormat("hh:mm")
                            .format(calendar.getTime()));
                    binding.textViewStartDate.setText(new SimpleDateFormat("dd-MM-yyyy")
                            .format(calendar.getTime()));
                } else if (res == R.id.cardViewCalendarEnd) {
                    binding.textViewEndTime.setText(new SimpleDateFormat("hh:mm")
                            .format(calendar.getTime()));
                    binding.textViewEndDate.setText(new SimpleDateFormat("dd-MM-yyyy")
                            .format(calendar.getTime()));
                }
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();

        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        datePickerDialog.show();
    }

    private String createDataInBundle() {
        //CALENDAR
        return "BEGIN:VEVENT" + "\n" + "SUMMARY:" + binding.editTextTitleCalendar.getText().toString() +
                "\n" + "DTSTART:" + binding.textViewStartDate.getText().toString() +
                binding.textViewStartTime.getText().toString() +
                "\n" + "DTEND:" + binding.textViewEndDate.getText().toString() +
                binding.textViewEndTime.getText().toString() +
                "\n" + "LOCATION:" + binding.editTextaddressCalendar.getText().toString() +
                "\n" + "DESCRIPTION:" + binding.editTextContentCalendar.getText().toString() +
                "\n" + "END:VEVENT";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}