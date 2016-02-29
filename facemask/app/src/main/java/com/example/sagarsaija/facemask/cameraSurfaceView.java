package com.example.sagarsaija.facemask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by sagarsaija
 */
public class cameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    private static final int LANDSCAPE_ROTATE = 0;
    private static final int PORTRAIT_ROTATE = 90;

    private SurfaceHolder holder;
    private Camera camera = null;
    private int rotate = 0;

    public cameraSurfaceView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        camera = Camera.open();

        try {
            camera.setPreviewDisplay(holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        camera.stopPreview();

        Camera.Parameters parameters = camera.getParameters();

        Camera.Size previewSize = parameters.getSupportedPreviewSizes().get(0);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        if (width > height) {
            camera.setDisplayOrientation(LANDSCAPE_ROTATE);
            rotate = LANDSCAPE_ROTATE;
        }
        else {
            camera.setDisplayOrientation(PORTRAIT_ROTATE);
            rotate = PORTRAIT_ROTATE;
        }

        camera.setParameters(parameters);
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        stopCamera();
    }

    public void stopCamera() {

        if (camera != null){
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public void capture(Camera.PictureCallback jpegHandler) {
        camera.takePicture(null, null, jpegHandler);
    }

    public void startPreview() {
        camera.startPreview();
    }

    public int getRotate() {
        return rotate;
    }
}
