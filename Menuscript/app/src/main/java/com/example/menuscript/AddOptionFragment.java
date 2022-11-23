package com.example.menuscript;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

/**
 * This class controls the addition of a new category, unit, or location.
 * Users can input desired attributes (within restrictions) and click confirm to add the option.
 * Currently, adding of specific units or locations is not implemented.
 * editOption {@link EditText}
 *
 * @author Micheal
 */

public class AddOptionFragment extends DialogFragment {
    private AddOptionFragment.OnFragmentInteractionListener listener;
    EditText editOption;

    public interface OnFragmentInteractionListener {
        void onAddOKPressed(String newOption, int tag);
    }

    public AddOptionFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_option_fragment, null);
        editOption = view.findViewById(R.id.prompt_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (Objects.equals(this.getTag(), "ADD CATEGORY")) {
            builder.setView(view)
                    .setTitle("Add category")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", (dialogInterface, i) -> {

                        String option = editOption.getText().toString();

                        listener.onAddOKPressed(option, 1);
                    });
            return builder.create();
        } else if (Objects.equals(this.getTag(), "ADD LOCATION")) {
            editOption.setHint("Location");
            builder.setView(view)
                    .setTitle("Add location")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", (dialogInterface, i) -> {

                        String option = editOption.getText().toString();

                        listener.onAddOKPressed(option, 2);
                    });
            return builder.create();
        } else {
            editOption.setHint("Unit");
            builder.setView(view)
                    .setTitle("Add unit")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", (dialogInterface, i) -> {

                        String option = editOption.getText().toString();

                        listener.onAddOKPressed(option, 3);
                    });
            return builder.create();
        }
    }

}
