package may.dan.darkroom;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gallerypick.R;

import may.dan.darkroom.helper.BitmapHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static final String KEY = "key";
    private static final int GALLERY_CODE = 1;
    Button button, buttoned;
    ImageView image;
    SeekBar sb;
    TextView tv;
    private static int seekBarValue;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color=\"black\">"+ getString(R.string.app_name)+"</font>"));
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#FF0000")));
        button = findViewById(R.id.buttonone);
        buttoned =findViewById(R.id.buttontwo);
        sb= findViewById(R.id.seekBar);
        tv= findViewById(R.id.editText2);
        tv.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    sb.setProgress((Integer.parseInt(String.valueOf(s))));
                    EditText et= findViewById(R.id.editText2);
                    final int position = et.length();
                    final Editable event = et.getText();
                    Selection.setSelection(event,position);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                tv.setText(progress+ "");
                seekBarValue=progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHECKGALLERYPERMISSION();
                /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_CODE);*/
            }
        });
        buttoned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BitmapHelper.getInstance().getBitmap()==null){
                    Toast.makeText(MainActivity.this, "Select Image First", Toast.LENGTH_SHORT).show();
                }
                else if(seekBarValue==0){
                    Toast.makeText(MainActivity.this, "Exposure Can't be 0", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Intent intent= new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra("SeekerValues", seekBarValue);
                    startActivity(intent);

                }
            }
        });
    }
    private void CHECKGALLERYPERMISSION()
    {
        int MY_READ_PERMISSION_REQUEST_CODE =1 ;
        int PICK_IMAGE_REQUEST = 2;
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_CODE);
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_READ_PERMISSION_REQUEST_CODE);
                        CHECKGALLERYPERMISSION();

            }
            else
            {
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {
            image = findViewById(R.id.myimage);
            try {
                InputStream inputstream = getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
                Bitmap bitmap = BitmapFactory.decodeStream(inputstream);

                image.setImageBitmap(bitmap);

                Bitmap newPhoto=invert(bitmap);

                BitmapHelper.getInstance().setBitmap(newPhoto);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public Bitmap invert(Bitmap original) {
        final int RGB_MASK = 0x00FFFFFF;

        // Create mutable Bitmap to invert, argument true makes it mutable
        Bitmap inversion = original.copy(Bitmap.Config.ARGB_8888, true);

        // Get info about Bitmap
        int width = inversion.getWidth();
        int height = inversion.getHeight();
        int pixels = width * height;

        // Get original pixels
        int[] pixel = new int[pixels];
        inversion.getPixels(pixel, 0, width, 0, 0, width, height);

        // Modify pixels
        for (int i = 0; i < pixels; i++)
            pixel[i] ^= RGB_MASK;
        inversion.setPixels(pixel, 0, width, 0, 0, width, height);

        // Return inverted Bitmap
        return inversion;
    }


}

