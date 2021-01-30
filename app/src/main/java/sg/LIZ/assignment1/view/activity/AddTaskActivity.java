/**
 * @Author Ang Yun Zane
 * @Author Lucas Tan
 * @Author Lim I Kin
 * class DIT/FT/2A/21
 */
package sg.LIZ.assignment1.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import sg.LIZ.assignment1.Key;
import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.model.net.ImageDownload;
import sg.LIZ.assignment1.model.utilityBean.TaskDb;
import sg.LIZ.assignment1.model.valueBean.Task;
import sg.LIZ.assignment1.model.LocationTracker;

import android.Manifest;
import android.annotation.SuppressLint;
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
    private static Bitmap bitmap;
    private TextWatcher textWatcher = null;
    private final TaskDb taskDb = new TaskDb(this);

    @SuppressLint({"SetTextI18n", "DefaultLocale", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException ignored) {
        }
        setContentView(R.layout.activity_add_task);
        Intent i = getIntent();
        selectedDay = i.getIntExtra(Key.KEY_DAY, -1);
        selectedMonth = i.getIntExtra(Key.KEY_MONTH, -1);
        selectedYear = i.getIntExtra(Key.KEY_YEAR, -1);
        timeSelect = findViewById(R.id.add_task_time);
        buttonStartTime = findViewById(R.id.add_start_time);
        buttonEndTimeBtn = findViewById(R.id.add_end_time);
        editTextTitleInput = findViewById(R.id.add_title);
        editTextDescriptionInput = findViewById(R.id.add_description);
        imageView = findViewById(R.id.add_task_img);
        editTextVenueInput = findViewById(R.id.add_value);
        buttonGPS = findViewById(R.id.gps_btn);
        GregorianCalendar currentDate = new GregorianCalendar();
        startHours = currentDate.get(Calendar.HOUR_OF_DAY);
        startMinutes = currentDate.get(Calendar.MINUTE);
        final String FORMAT = "%02d";
        /*output the start and end time */
        buttonStartTime.setText(new StringBuilder(8)
                .append(currentDate.get(Calendar.HOUR))
                .append(':').append(String.format(FORMAT, startMinutes))
                .append(' ')
                .append(currentDate.get(Calendar.AM_PM) == Calendar.PM ? new char[]{'P', 'M'} : new char[]{'A', 'M'}));
        currentDate.set(Calendar.HOUR_OF_DAY, currentDate.get(Calendar.HOUR_OF_DAY) + 1);
        endHours = currentDate.get(Calendar.HOUR_OF_DAY);
        endMinutes = currentDate.get(Calendar.MINUTE);
        buttonEndTimeBtn.setText(new StringBuilder(8)
                .append(currentDate.get(Calendar.HOUR))
                .append(':')
                .append(String.format(FORMAT, endMinutes))
                .append(' ')
                .append(currentDate.get(Calendar.AM_PM) == Calendar.PM ? new char[]{'P', 'M'} : new char[]{'A', 'M'}));
        //selectedDay + " " + getResources().getStringArray(R.array.month)[selectedMonth]
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        ((TextView) findViewById(R.id.task_date)).setText(new StringBuilder().append(selectedDay).append(' ').append(getResources().getStringArray(R.array.month)[selectedMonth]));
        buttonGPS.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gps == null) {
                    gps = new LocationTracker(this);
                }
                if (gps.canGetLocation()) {
                    new Thread(() -> {
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
                        gps.getLocation();
                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        try {
                            final List<Address> addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 4);
                            final int size = addresses.size();
                            runOnUiThread(() -> {
                                switch (size) {
                                    case 1:
                                        editTextVenueInput.setText(addresses.get(0).getAddressLine(0));
                                        break;
                                    case 0:
                                        editTextVenueInput.setText(new StringBuilder(Double.toString(gps.getLatitude())).append(',').append(gps.getLongitude()));
                                        break;
                                    default:
                                        final String[] addressLines = new String[size];
                                        for (int j = 0; j < size; ++j) {
                                            Address address = addresses.get(j);
                                            final int addressLineSize = address.getMaxAddressLineIndex();
                                            StringBuilder addressLine = new StringBuilder(address.getAddressLine(0));
                                            for (int k = 3; k < addressLineSize; ++k) {
                                                addressLine.append(',').append(address.getAddressLine(k));
                                            }
                                            addressLines[j] = addressLine.toString();
                                        }
                                        Log.i("addressLines", Arrays.toString(addressLines));
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
                                buttonGPS.setImageDrawable(getDrawable(R.drawable.ic_baseline_gps_fixed_24));
                                editTextVenueInput.addTextChangedListener(textWatcher);
                            });
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Log.e("Error", e.getMessage(), e);
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).start();

                } else {
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.location)
                            .setMessage(R.string.no_gps)
                            .setPositiveButton(R.string.enable, (dialog, which) -> {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, 2);
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .show();
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    buttonGPS.callOnClick();
                }
                break;
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImageFromLink();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.discard_task)
                .setMessage(R.string.discard_task2)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    bitmap = null;
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public void onBackPressed(View v) {
        onBackPressed();
    }

    public void onAllDayToggled(View v) {
        timeSelect.setVisibility((allDay = ((SwitchCompat) v).isChecked()) ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 0:
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    break;
                case 1:
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        Log.e("image error", e.getMessage(), e);
                    }
            }
        } else {
            if (requestCode == 2) {
                gps = new LocationTracker(this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bitmap = null;
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
        String title = editTextTitleInput.getText().toString().trim();
        String description = editTextDescriptionInput.getText().toString().trim();
        String venue = editTextVenueInput.getText().toString().trim();
        if (title.isEmpty() || description.isEmpty() || venue.isEmpty()) {
            Toast.makeText(this, R.string.no_input_error, Toast.LENGTH_LONG).show();
        } else {
            if (allDay) {
                startMinutes = -1;
                startHours = -1;
                endMinutes = -1;
                endHours = -1;
            }
            byte[] imageByte = null;
            if (bitmap != null) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.WEBP, 0, outputStream);
                imageByte = outputStream.toByteArray();
                bitmap = null;
            }
            Task mTask = new Task((byte) selectedDay, (byte) selectedMonth, selectedYear, (byte) startHours, (byte) startMinutes, (byte) endHours, (byte) endMinutes, allDay, title, description, venue, imageByte);
            taskDb.addTask(mTask);
            setResult(Activity.RESULT_OK);
            Toast.makeText(this, R.string.add_successfully, Toast.LENGTH_LONG).show();
            finish();


        }
    }

    public void onSetImageClick(View v) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.select_img_src)
                .setItems(R.array.img_src_opt, (dialog, which) -> {
                    Intent i = null;
                    switch (which) {
                        case 0:
                            i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(i, 0);
                            break;
                        case 1:
                            i = new Intent(Intent.ACTION_PICK);
                            i.setType("image/*");
                            startActivityForResult(i, 1);
                            break;
                        case 2:
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
                                    getImageFromLink();
                                } else {
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
        final EditText txtUrl = new EditText(this);
        txtUrl.setMaxLines(1);
        txtUrl.setInputType(InputType.TYPE_CLASS_TEXT);
        final Thread thread =new Thread(() -> {
            bitmap = ImageDownload.getImage(this, txtUrl.getText());
            if (bitmap != null) {
                runOnUiThread(() -> imageView.setImageBitmap(bitmap));

            }
        });
        txtUrl.setHint(R.string.url_hint);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_upload)
                .setTitle(R.string.enter_img_url)
                .setView(txtUrl)
                .setPositiveButton(R.string.Ok, (dialog2, whichButton) -> {
                    thread.start();
                }).setNegativeButton(R.string.cancel, null).show();
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