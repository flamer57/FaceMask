package com.example.sagarsaija.facemask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by sagarsaija
 */
public class stillImage extends ImageView {

    FaceDetector.Face [] faces = null;
    Boolean printNoFaceFoundMessage = false;
    int imageWidth;
    int imageHeight;

    public stillImage(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Paint textPaint = new Paint();
        textPaint.setTextSize(50);
        textPaint.setColor(Color.RED);

        if (faces != null) {

            PointF midpoint = new PointF();

            float scaleX = (float) canvas.getWidth() / (float) imageWidth;
            float scaleY = (float) canvas.getHeight() / (float) imageHeight;
            float transX = ((float) canvas.getWidth() - scaleX*imageWidth) / 2f;
            float transY = ((float) canvas.getHeight() - scaleY*imageHeight) / 2f;
            float X, Y, faceSize;

            for(int i = 0; i < faces.length; i++) {

                faces[i].getMidPoint(midpoint);

                X = scaleX * midpoint.x + transX;
                Y = scaleY * midpoint.y + transY;
                faceSize = scaleX * faces[i].eyesDistance();

                drawMask(X, Y, faceSize, canvas);
                canvas.drawText( "Confidence" + i + ": " + faces[i].confidence(), 10, 50*(float)(i+1), textPaint);
            }

        }
        else if (printNoFaceFoundMessage) {

            canvas.drawText( "No face detected, try again!", 10, 50, textPaint);
            printNoFaceFoundMessage = false;
        }
        else{
            Log.d("camera251", "no faces");
            canvas.drawColor(Color.TRANSPARENT);
        }
    }

    private void drawMask(float X, float Y, float faceSize, Canvas canvas) {

        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth( (int) (0.1f * faceSize));

        //draw whiskers
        float whiskerUp = 0.10f * faceSize;
        float whiskerStart = 0.40f * faceSize;
        float whiskerEnd = 0.5f * faceSize;
        canvas.drawLine(X + whiskerStart, Y + faceSize - whiskerUp, X + faceSize, Y + whiskerEnd, linePaint);
        canvas.drawLine(X - whiskerStart, Y + faceSize - whiskerUp, X - faceSize, Y + whiskerEnd, linePaint);
        canvas.drawLine(X + whiskerStart, Y + faceSize, X + 1.3f * faceSize, Y + 2f * whiskerEnd, linePaint);
        canvas.drawLine(X - whiskerStart, Y + faceSize, X - 1.3f * faceSize, Y + 2f * whiskerEnd, linePaint);
        canvas.drawLine(X + whiskerStart, Y + faceSize + whiskerUp, X + faceSize, Y + 3f * whiskerEnd, linePaint);
        canvas.drawLine(X - whiskerStart, Y + faceSize + whiskerUp, X - faceSize, Y + 3f * whiskerEnd, linePaint);

        //draw ears
        Bitmap ears = BitmapFactory.decodeResource(getResources(), R.drawable.ears);
        RectF rectF = new RectF();
        rectF.set(X - 1.25f * faceSize, Y- 2.5f * faceSize, X + 1.25f * faceSize, Y - 0.5f * faceSize);
        canvas.drawBitmap(ears, null, rectF, null);

    }

    public void setFaces(FaceDetector.Face[] faces, int count, int width, int height) {

        imageWidth = width;
        imageHeight = height;
        this.faces = new FaceDetector.Face[count];

        System.arraycopy(faces, 0, this.faces, 0, count);
        printNoFaceFoundMessage = false;
    }

    public void noFaces() {
        faces = null;
        printNoFaceFoundMessage = true;
    }

    public void reset() {
        faces = null;
        setImageBitmap(null);
        printNoFaceFoundMessage = false;
    }
}
