package com.example.menuscript;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
/**
 * This class controls the addition of a new category, unit, or location.
 * Users can input desired attributes (within restrictions) and click confirm to add the option.
 * Currently, adding of specific units or locations is not implemented.
 * editOption {@link EditText}
 *
 * @author Micheal Chow
 */

public class AddOptionFragment extends DialogFragment {
    private AddOptionFragment.OnFragmentInteractionListener listener;
    EditText editOption;

    public interface OnFragmentInteractionListener {
        void onAddOKPressed(String newOption);
    }

    public AddOptionFragment() {
    }

    @Override
    public void onAttach(Context context) {
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
        builder.setView(view)
                .setTitle("Add category")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialogInterface, i) -> {

                    String option = editOption.getText().toString();

                    listener.onAddOKPressed(option);
                });
        return builder.create();

    }

}
