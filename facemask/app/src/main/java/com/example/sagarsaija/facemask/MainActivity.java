package com.example.sagarsaija.facemask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;



public class MainActivity extends Activity {
    private static final int CAMERA_PREVIEW = 0;
    private static final int CAMERA_CAPTURE = 1;
    private static final int CAMERA_MASK = 2;
    private static final int MAX_FACES = 2;
    private static final String TAG = "camera251";


    private cameraSurfaceView cameraSurface;
    private stillImage cameraStill;
    private FrameLayout cameraFrame;
    private Button cameraButton;
    private Bitmap image = null;
    private int mode = CAMERA_PREVIEW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraSurface.stopCamera();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupCamera() {

        cameraSurface = new cameraSurfaceView(getApplicationContext());
        cameraStill = new stillImage(getApplicationContext());

        cameraStill.setBackgroundColor(Color.GRAY);
        cameraStill.setScaleType(ImageView.ScaleType.FIT_XY);

        cameraFrame = (FrameLayout) findViewById(R.id.cameraDisplay);
        cameraButton = (Button) findViewById(R.id.cameraButton);

        cameraFrame.addView(cameraSurface);
        cameraFrame.addView(cameraStill);

        cameraFrame.bringChildToFront(cameraSurface);
    }


    public void cameraButtonOnClick(View v) {

        switch (mode) {
            case CAMERA_PREVIEW:
                takePicture();
                cameraButton.setText(getString(R.string.add_mask));
                mode = CAMERA_CAPTURE;
                break;
            case CAMERA_CAPTURE:
                addMask();
                cameraButton.setText(getString(R.string.show_preview));
                mode = CAMERA_MASK;
                break;
            case CAMERA_MASK:
                resetCamera();
                cameraButton.setText(getString(R.string.take_picture));
                mode = CAMERA_PREVIEW;
                break;
            default:
                break;
        }
    }

    private void takePicture() {

        cameraSurface.capture(jpegHandler);
    }

    public Camera.PictureCallback jpegHandler = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap snapShot  = BitmapFactory.decodeByteArray(data, 0, data.length);

            //rotate image to compensate for the weird hardware
            snapShot = rotateImage(snapShot);

            image = snapShot.copy(Bitmap.Config.RGB_565, false);

            cameraStill.setImageBitmap(image);
            cameraStill.invalidate();
            cameraFrame.bringChildToFront(cameraStill);
        }
    };

    private Bitmap rotateImage(Bitmap image) {

        Matrix matrix = new Matrix();
        matrix.postTranslate(0f - image.getWidth()/2, 0f - image.getHeight()/2);
        matrix.postRotate(new Float(cameraSurface.getRotate()));
        matrix.postTranslate(image.getWidth()/2, image.getHeight()/2);

        return Bitmap.createBitmap(image,0, 0, image.getWidth(), image.getHeight(), matrix, false );
    }

    private void addMask() {

        FaceDetector detector;
        FaceDetector.Face [] faces = new FaceDetector.Face[MAX_FACES];
        int [] fx = null;
        int [] fy = null;

        int count = 0;

        detector = new FaceDetector(image.getWidth(), image.getHeight(), MAX_FACES);
        count = detector.findFaces(image, faces);

        if (count > 0) {
            cameraStill.setFaces(faces, count, image.getWidth(), image.getHeight());
            cameraStill.invalidate();
        }
        else {
            cameraStill.noFaces();
            cameraStill.invalidate();
        }
    }

    private void resetCamera() {

        cameraFrame.bringChildToFront(cameraSurface);
        cameraStill.reset();
        cameraSurface.startPreview();
    }



}
