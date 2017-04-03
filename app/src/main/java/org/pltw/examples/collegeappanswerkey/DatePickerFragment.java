package org.pltw.examples.collegeappanswerkey;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by simmonsj05 on 1/15/17.
 */
public class DatePickerFragment extends DialogFragment {

    public static final String DATE_ARGUMENT = "date of birth";
    public static final String EXTRA_DATE_OF_BIRTH = "org.pltw.examples.collegeappanswerkey.dateofbirth";
    private DatePicker mDatePicker;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                                .inflate(R.layout.dialog_date_of_birth, null);

        Date date = (Date)(getArguments().getSerializable(DATE_ARGUMENT));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_of_birth);
        mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Date of Birth")
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date date = new GregorianCalendar(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth())
                                .getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }

    public static DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(DATE_ARGUMENT, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int resultCode, Date date){
        if (getTargetFragment() != null){
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DATE_OF_BIRTH, date);
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        }
    }
}
