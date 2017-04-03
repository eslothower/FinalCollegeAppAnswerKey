package org.pltw.examples.collegeappanswerkey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by simmonsj05 on 1/15/17.
 */
public class ProfileFragment extends Fragment{

    public static final int REQUEST_DATE_OF_BIRTH = 0;

    private TextView mFirstNameTextView, mLastNameTextView;
    private EditText mFirstNameEditText, mLastNameEditText;
    private Button mSubmitButton, mBirthdayButton;
    private SimpleDateFormat formatter;
    private Profile mProfile;
    private final String TAG = "PROFILE_FRAGMENT";
    private ImageButton mSelfieButton;
    private ImageView mSelfieView;
    private File mSelfieFile;
    private final int REQUEST_SELFIE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        mProfile = new Profile();
        SharedPreferences sharedPreferences =
                getActivity().getPreferences(Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(ApplicantActivity.EMAIL_PREF, null);
        if (mProfile.getEmail() == null) {
            mProfile.setEmail(email);
        }
            String whereClause = "email = '" + email + "'";
            BackendlessDataQuery query = new BackendlessDataQuery();
            query.setWhereClause(whereClause);
            Backendless.Persistence.of(Profile.class).find(query, new
                    AsyncCallback<BackendlessCollection<Profile>>() {
                        @Override
                        public void handleResponse(BackendlessCollection<Profile> response) {
                            if (!response.getData().isEmpty()) {
                                mProfile = response.getData().get(0);
                                mFirstNameTextView.setText(response.getData().get(0).getFirstName());
                                mLastNameTextView.setText(response.getData().get(0).getLastName());
                                Date birthday = response.getData().get(0).getBirthday();
                                try {
                                    Date formattedBirthday = new SimpleDateFormat("yyyy-MM-dd").parse(birthday.toString());
                                    mBirthdayButton.setText(formattedBirthday.toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Log.i(TAG, "Got profile: " + response.getData().get(0).objectId);
                            }
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.e(TAG, "Failed to find profile: " + fault.getMessage());

                        }
                    });
        formatter = new SimpleDateFormat("MM/dd/yyyy");

        mFirstNameTextView = (TextView) rootView.findViewById(R.id.profile_first_name);
        mLastNameTextView = (TextView) rootView.findViewById(R.id.profile_last_name);
        mFirstNameEditText = (EditText) rootView.findViewById(R.id.profile_first_name_edit);
        mLastNameEditText = (EditText) rootView.findViewById(R.id.profile_last_name_edit);
        mBirthdayButton = (Button) rootView.findViewById(R.id.birthday_picker_button);
        mSubmitButton = (Button) rootView.findViewById(R.id.profile_submit_button);
        mSelfieButton = (ImageButton) rootView.findViewById(R.id.profile_camera);
        mSelfieView = (ImageView) rootView.findViewById(R.id.profile_pic);
        mSelfieFile = mProfile.getPhotoFile(getActivity());

        if (mProfile.getFirstName() != null) {
            mFirstNameTextView.setText(mProfile.getFirstName());
            mLastNameTextView.setText(mProfile.getLastName());
            mBirthdayButton.setText(formatter.format(mProfile.getBirthday()));
        }
        else{
            mFirstNameTextView.setText("Enter First Name:");
            mLastNameTextView.setText("Enter Last Name:");
        }

        mBirthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog;
                dialog = DatePickerFragment.newInstance(mProfile.getBirthday());
                dialog.setTargetFragment(ProfileFragment.this, REQUEST_DATE_OF_BIRTH);
                dialog.show(fm, "DialogDateOfBirth");
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFirstNameEditText.getText().length() > 0){
                    String firstName = mFirstNameEditText.getText().toString();
                    mProfile.setFirstName(firstName);
                    mFirstNameTextView.setText(firstName);
                    mFirstNameEditText.setText("");
                }

                if(mLastNameEditText.getText().length() > 0){
                    String lastName = mLastNameEditText.getText().toString();
                    mProfile.setLastName(lastName);
                    mLastNameTextView.setText(lastName);
                    mLastNameEditText.setText("");
                }
                if (mProfile.getFirstName() != null && mProfile.getLastName()!= null){
                    Backendless.Persistence.save(mProfile, new BackendlessCallback<Profile>() {
                        @Override
                        public void handleResponse(Profile response) {
                            Log.i("Backendless", "Saved profile to Backendless " + response.objectId);
                        }
                    });
                }
                
            }
        });

        final Intent captureSelfie = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakeSelfie = mSelfieFile != null &&
                captureSelfie.resolveActivity(getActivity().getPackageManager()) != null;
        mSelfieButton.setEnabled(canTakeSelfie);
        if (canTakeSelfie) {
            Uri uri = Uri.fromFile(mSelfieFile);
            captureSelfie.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mSelfieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureSelfie, REQUEST_SELFIE);
            }
        });

        updateSelfieView();
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    public void onPause(){
        super.onPause();
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE_OF_BIRTH){
            Date date = (Date)(data.getSerializableExtra(DatePickerFragment.EXTRA_DATE_OF_BIRTH));
            mProfile.setBirthday(date);
            mBirthdayButton.setText(formatter.format(mProfile.getBirthday()));
        }
        if(requestCode == REQUEST_SELFIE){
            updateSelfieView();
        }
    }

    public void updateSelfieView(){
        if (mSelfieFile!=null && mSelfieFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(mSelfieFile.getPath());
            mSelfieView.setImageBitmap(bitmap);
        }
    }
}
