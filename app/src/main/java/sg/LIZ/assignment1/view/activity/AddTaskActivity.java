/**
 * @Author Ang Yun Zane
 * @Author Lucas Tan
 * @Author Lim I Kin
 * class DIT/FT/2A/21
 */
package sg.LIZ.assignment1.view.activity;

import sg.LIZ.assignment1.Key;
import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.model.net.ImageDownload;
import sg.LIZ.assignment1.model.utilityBean.TaskDb;
import sg.LIZ.assignment1.model.valueBean.Task;
import sg.LIZ.assignment1.model.LocationTracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.GridLayout;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private GridLayout timeSelect;
    private TimePickerDialog startTimePicker = null;
    private TimePickerDialog endTimePicker = null;
    private Button buttonStartTime;
    private Button buttonEndTimeBtn;
    private ImageButton buttonGPS;
    private LocationTracker gps = null;
    private EditText editTextTitleInput;
    private EditText editTextDescriptionInput;
    private EditText editTextVenueInput;
    private ImageView imageView;
    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;
    private boolean allDay;
    private int startHours;
    private int startMinutes;
    private int endHours;
    private int endMinutes;
    private Bitmap bitmap;
    private TextWatcher textWatcher = null;
    private final TaskDb taskDb = new TaskDb(this);

    @SuppressLint({"SetTextI18n", "DefaultLocale", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Intent i = getIntent();
        /*get date from the previous page*/
        selectedDay = i.getIntExtra(Key.KEY_DAY, -1);
        selectedMonth = i.getIntExtra(Key.KEY_MONTH, -1);
        selectedYear = i.getIntExtra(Key.KEY_YEAR, -1);
        /*pre define view object */
        timeSelect = findViewById(R.id.add_task_time);
        buttonStartTime = findViewById(R.id.add_start_time);
        buttonEndTimeBtn = findViewById(R.id.add_end_time);
        editTextTitleInput = findViewById(R.id.add_title);
        editTextDescriptionInput = findViewById(R.id.add_description);
        imageView = findViewById(R.id.add_task_img);
        editTextVenueInput = findViewById(R.id.add_value);
        buttonGPS = findViewById(R.id.gps_btn);
        GregorianCalendar currentDate = new GregorianCalendar();
        /*get the current time*/
        startHours = currentDate.get(Calendar.HOUR_OF_DAY);
        startMinutes = currentDate.get(Calendar.MINUTE);
        final String FORMAT = "%02d";
        /*output the start and end time */
        buttonStartTime.setText(new StringBuilder(8)
                .append(currentDate.get(Calendar.HOUR))
                .append(':').append(String.format(FORMAT, startMinutes))
                .append(currentDate.get(Calendar.AM_PM) == Calendar.PM ? new char[]{' ','P', 'M'} : new char[]{' ','A', 'M'}));
        currentDate.set(Calendar.HOUR_OF_DAY, currentDate.get(Calendar.HOUR_OF_DAY) + 1);
        endHours = currentDate.get(Calendar.HOUR_OF_DAY);
        endMinutes = currentDate.get(Calendar.MINUTE);
        buttonEndTimeBtn.setText(new StringBuilder(8)
                .append(currentDate.get(Calendar.HOUR))
                .append(':')
                .append(String.format(FORMAT, endMinutes))
                .append(String.format(FORMAT, endMinutes))
                .append(currentDate.get(Calendar.AM_PM) == Calendar.PM ? new char[]{' ','P', 'M'} : new char[]{' ','A', 'M'}));
        /*check the bitmap is not null output the image*/
        if (savedInstanceState != null) {
            bitmap = savedInstanceState.getParcelable(Key.KEY_IMAGE);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
        /*output the month*/
        ((TextView) findViewById(R.id.task_date)).setText(new StringBuilder().append(selectedDay).append(' ').append(getResources().getStringArray(R.array.month)[selectedMonth]));
        /*run this when the user click to get location*/
        buttonGPS.setOnClickListener(v -> {
            /*check if the app is allow to get location*/
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                /*check if the gps object is already define*/
                if (gps == null) {
                    /*if not create the gps object*/
                    gps = new LocationTracker(this);
                }
                /*check if the location service is enabled*/
                if (gps.canGetLocation()) {
                    /*run the provess in new thread so as not to slow down the UI thread*/
                    new Thread(() -> {
                        /*check if the watch is enabled*/
                        if (textWatcher == null) {
                            textWatcher = new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                }

                                @SuppressLint("UseCompatLoadingForDrawables")
                                @Override
                                public void afterTextChanged(Editable s) {
                                    buttonGPS.setImageDrawable(getDrawable(R.drawable.ic_baseline_gps_not_fixed_24));
                                    editTextVenueInput.removeTextChangedListener(this);
                                }
                            };
                        }
                        /*run the to get ask the gps object to get location*/
                        gps.getLocation();
                        Locale mLocale;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            mLocale = getResources().getConfiguration().getLocales().get(0);
                        } else{
                            mLocale = getResources().getConfiguration().locale;
                        }
                        /*put the location to geocoder to get the address of the location*/
                        Geocoder geocoder = new Geocoder(this, mLocale);
                        try {
                            /*getting the list of address for the location*/
                            final List<Address> addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 4);
                            final int size = addresses.size();
                            /*you have run on UI thread in order to change the view*/
                            runOnUiThread(() -> {
                                /*check the among of address*/
                                switch (size) {
                                    case 1:
                                        /*if there is only one address found output that address*/
                                        Address address0 = addresses.get(0);
                                        final int addressLineSize = address0.getMaxAddressLineIndex();
                                        StringBuilder  addressLines1 = new StringBuilder(address0.getAddressLine(0));
                                        for(int j =1;j<addressLineSize;++j){
                                                addressLines1.append(',').append(address0.getAddressLine(j));
                                        }
                                        editTextVenueInput.setText(addressLines1);
                                        break;
                                    case 0:
                                        /*if there is not address found just output the lat and long*/
                                        editTextVenueInput.setText(new StringBuilder().append(gps.getLatitude()).append(',').append(gps.getLongitude()));
                                        break;
                                    default:
                                        /*if there is more then one ask the user to select the address they want*/
                                        final String[] addressLines = new String[size];
                                        /*loop to get the list of address to string array*/
                                        for (int j = 0; j < size; ++j) {
                                            Address address = addresses.get(j);
                                            final int addressLineSize2 = address.getMaxAddressLineIndex();
                                            StringBuilder addressLine = new StringBuilder(address.getAddressLine(0));
                                            for (int k = 1; k < addressLineSize2; ++k) {
                                                addressLine.append(',').append(address.getAddressLine(k));
                                            }
                                            addressLines[j] = addressLine.toString();
                                        }
                                        new AlertDialog.Builder(this)
                                                .setIcon(android.R.drawable.ic_menu_mylocation)
                                                .setTitle(R.string.select_address)
                                                .setItems(addressLines, (dialog, which) -> {
                                                    editTextVenueInput.setText(addressLines[which]);
                                                    buttonGPS.setImageDrawable(getDrawable(R.drawable.ic_baseline_gps_fixed_24));
                                                    editTextVenueInput.addTextChangedListener(textWatcher);
                                                }).show();
                                        return;
                                }
                                /*change the button icon to indicate that the venue coming the location service*/
                                buttonGPS.setImageDrawable(getDrawable(R.drawable.ic_baseline_gps_fixed_24));
                                editTextVenueInput.addTextChangedListener(textWatcher);
                            });
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage(), e);
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).start();

                } else {
                    /*if location service disabled ask the user to enable it */
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.location)
                            .setMessage(R.string.no_gps)
                            .setPositiveButton(R.string.enable, (dialog, which) -> {
                                /*navigate to the location setting page of the device to enable it*/
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, 2);
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .show();
                }
            } else {
                /*if the app not allow to get location ask the user to get permissions*/
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                /*run this after the user allow the app to use the location service*/
                /*check if the the user had allow the use if location*/
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    /*call the method again to get the location*/
                    buttonGPS.callOnClick();
                }
                break;
            case 1:
                /*run the after the user have allow internet*/
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImageFromLink();
                }
                break;
        }
    }

    /*run this when the user press on the back button on he/she device*/
    @Override
    public void onBackPressed() {
        /*ask the user if they reilly want to discard the task*/
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.discard_task)
                .setMessage(R.string.discard_task2)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public void onBackPressed(View v) {
        onBackPressed();
    }

    /*run this when the user click on the all day*/
    public void onAllDayToggled(View v) {
        /*check if the user on or off the toggle and set the visibility of the start and end time accordingly*/
        timeSelect.setVisibility((allDay = ((SwitchCompat) v).isChecked()) ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 0:
                    /*run this after the user have already taken the photo from the device camera */
                    bitmap = (Bitmap) data.getExtras().get("data");
                    /*output the image*/
                    imageView.setImageBitmap(bitmap);
                    break;
                case 1:
                    try {
                        /*run this after the user have choose the image from the device gallery
                        get the image from the device file system and convent to bitmap*/
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                        /*output the image*/
                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        Log.e("image error", e.getMessage(), e);
                    }
            }
        } else {
            if (requestCode == 2) {
                /*run this after the user have enabled the location service*/
                gps = new LocationTracker(this);
            }
        }
    }

    @SuppressLint("DefaultLocale")
    public void onSetStartTime(View v) {
        /*only create the TimePickerDialog when needed*/
        if (startTimePicker == null) {
            startTimePicker = new TimePickerDialog(this, (v2, hours, minute) -> {
                if (hours < endHours || minute < endMinutes) {
                    startHours = hours;
                    startMinutes = minute;
                    buttonStartTime.setText(new StringBuilder(8)
                            .append(hours > 12 ? startHours - 12 : startHours)
                            .append(':').append(String.format("%02d", startMinutes))
                            .append(' ')
                            .append(hours > 11 ? new char[]{'P', 'M'} : new char[]{'A', 'M'}));
                } else {
                    Toast.makeText(this, R.string.set_start_time_err, Toast.LENGTH_LONG).show();
                }
            }, startHours, startMinutes, false);
        }
        startTimePicker.show();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Key.KEY_IMAGE, bitmap);
    }

    @SuppressLint("DefaultLocale")
    public void onSetEndTime(View v) {
        /*only create the TimePickerDialog when needed*/
        if (endTimePicker == null) {
            endTimePicker = new TimePickerDialog(this, (v2, hours, minute) -> {
                if (hours > startHours || minute > startMinutes) {
                    endHours = hours;
                    endMinutes = minute;
                    buttonEndTimeBtn.setText(new StringBuilder(8)
                            .append(hours > 12 ? endHours - 12 : endHours)
                            .append(':').append(String.format("%02d", endMinutes))
                            .append(' ')
                            .append(hours > 11 ? new char[]{'P', 'M'} : new char[]{'A', 'M'}));
                } else {
                    Toast.makeText(this, R.string.set_end_time_err, Toast.LENGTH_LONG).show();
                }
            }, endHours, endMinutes, false);
        }
        endTimePicker.show();
    }

    public void onSubmit(View v) {
        /*get the title, description and venue from edit text*/
        String title = editTextTitleInput.getText().toString().trim();
        String description = editTextDescriptionInput.getText().toString().trim();
        String venue = editTextVenueInput.getText().toString().trim();
        /*check if the user have left out any fill*/
        if (title.isEmpty() || description.isEmpty() || venue.isEmpty()) {
            Toast.makeText(this, R.string.no_input_error, Toast.LENGTH_LONG).show();
        } else {
            /*check if is all day*/
            if (allDay) {
                startMinutes = -1;
                startHours = -1;
                endMinutes = -1;
                endHours = -1;
            }
            byte[] imageByte = null;
            /*check if the user have upload an image*/
            if (bitmap != null) {
                /*convent the bitmap to webp image format as byte array*/
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.WEBP, 0, outputStream);
                imageByte = outputStream.toByteArray();
                bitmap = null;
            }
            /*create the task object*/
            Task mTask = new Task((byte) selectedDay, (byte) selectedMonth, selectedYear, (byte) startHours, (byte) startMinutes, (byte) endHours, (byte) endMinutes, allDay, title, description, venue, imageByte);
            /*add the task to SQLite database */
            taskDb.addTask(mTask);
            /*set the result ok so as tell the view by month that an task have been aded*/
            setResult(Activity.RESULT_OK);
            /*inform the user the the task have successfully added*/
            Toast.makeText(this, R.string.add_successfully, Toast.LENGTH_LONG).show();
            finish();


        }
    }

    /*run this when the user want to upload an image to the task*/
    public void onSetImageClick(View v) {
        /*ask the user where he/she want to get the image from*/
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_gallery)
                .setTitle(R.string.select_img_src)
                .setItems(R.array.img_src_opt, (dialog, which) -> {
                    Intent i;
                    switch (which) {
                        case 0:
                            /*run this if the user want take a photo*/
                            i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(i, 0);
                            break;
                        case 1:
                            /*run this if the user want to get it from the device gallery*/
                            i = new Intent(Intent.ACTION_PICK);
                            i.setType("image/*");
                            startActivityForResult(i, 1);
                            break;
                        case 2:
                            /*run this if the user want to download the image from the internet
                            check if the app is allow to use the internet*/
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                                /*check if the internet is on*/
                                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
                                    /*if is on tell the method to ask the user for the link*/
                                    getImageFromLink();
                                } else {
                                    /*else inform the user that he/she device is not connected to the internet*/
                                    Toast.makeText(this, R.string.conn_off, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
                            }
                            break;
                    }
                }).show();
    }

    private void getImageFromLink() {
        /*ask the user for the image download link*/
        final EditText txtUrl = new EditText(this);
        txtUrl.setMaxLines(1);
        txtUrl.setInputType(InputType.TYPE_CLASS_TEXT);
        /*create a new a new thread to download the image so as not to slow down the UI thread */
        final Thread thread = new Thread(() -> {
            bitmap = ImageDownload.getImage(this, txtUrl.getText());
            /*check if they manage to download the image*/
            if (bitmap != null) {
                /*run on UI thread as only this thread can change the view*/
                runOnUiThread(() -> imageView.setImageBitmap(bitmap));
            }
        });
        txtUrl.setHint(R.string.url_hint);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_upload)
                .setTitle(R.string.enter_img_url)
                .setView(txtUrl)
                .setPositiveButton(R.string.Ok, (dialog2, whichButton) -> thread.start())
                .setNegativeButton(R.string.cancel, null).show();
        txtUrl.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                alertDialog.dismiss();
                thread.start();
                return true;
            }
            return false;
        });
    }


}