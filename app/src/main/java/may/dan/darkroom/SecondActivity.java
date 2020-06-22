package may.dan.darkroom;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.gallerypick.R;

import java.util.Objects;

import may.dan.darkroom.helper.BitmapHelper;

public class SecondActivity extends AppCompatActivity {
    public ImageView imageview;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Handler handler = new Handler();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);
        imageview = (ImageView) findViewById(R.id.printingActivity);
        imageview.setBackgroundColor(Color.BLACK);
        this.negatives(imageview);
        this.delayme(imageview);
        this.newmethod(imageview);

    }

    public void negatives(final ImageView view) {
        try {
            System.out.println("Yaha Aya kya???");
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 15s = 15000ms
                    view.setImageBitmap(BitmapHelper.getInstance().getBitmap());
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);
                    imageview.setColorFilter(new ColorMatrixColorFilter(matrix));
                }
            }, 15000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delayme(final View view) {
        final Handler handler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                long future = System.currentTimeMillis() + 15000;

                while (System.currentTimeMillis() < future) {
                    synchronized (this) {
                        try {
                            //wait(future - System.currentTimeMillis());
                            Thread.sleep(15000);
                            System.out.println("Time btaio???");
                            int intValue;

                            {
                                Intent mIntent = getIntent();
                                intValue = mIntent.getIntExtra("SeekerValues", 0);
                            }
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    System.out.println("Ab Yaha hu");
                                    Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                                    startActivity(intent);

                                }
                            }, intValue * 1000+10000);
                        } catch (Exception ignored) {
                        }
                    }
                }
                handler.sendEmptyMessage(0);
            }
        };
        Thread myThread = new Thread(r);
        myThread.start();
    }

    public void newmethod(ImageView views) {
        final Handler handler1 = new Handler();
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                int intValues;

                {
                    Intent mIntent = getIntent();
                    intValues = mIntent.getIntExtra("SeekerValues", 0);
                }
                int intValuess=intValues*1000;
                long future = System.currentTimeMillis() + 15000 + intValuess;
                long waiting= future-System.currentTimeMillis();


                while (System.currentTimeMillis() < future) {
                    synchronized (this) {
                        try {

                            Thread.sleep(waiting);
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI
                                    imageview.setImageDrawable(null);

                                }
                            });

                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
                handler1.sendEmptyMessage(0);
            }
        };
        Thread myThread = new Thread(r1);
        myThread.start();
    }
}