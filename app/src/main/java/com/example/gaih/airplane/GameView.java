package com.example.gaih.airplane;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaih on 2016/7/31.
 */

public class GameView extends SurfaceView
        implements SurfaceHolder.Callback, Runnable, View.OnTouchListener {

    private Bitmap my;
    private Bitmap baozha;
    private Bitmap bg;
    private Bitmap diren;
    private Bitmap zidan;
    private Bitmap hero1;
    private Bitmap hero2;



    private Bitmap erjihuancun;

    private WindowManager windowManager;
    private int display_w;
    private int display_h;

    private List<iGameImage> gameImages = new ArrayList();


    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        this.setOnTouchListener(this);
    }

    private void init() {
        hero1 = BitmapFactory.decodeResource(getResources(), R.drawable.hero1);
        hero2 = BitmapFactory.decodeResource(getResources(), R.drawable.hero2);

        my = BitmapFactory.decodeResource(getResources(), R.drawable.my);
        baozha = BitmapFactory.decodeResource(getResources(), R.drawable.baozha);
        zidan = BitmapFactory.decodeResource(getResources(), R.drawable.zidan);
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        diren = BitmapFactory.decodeResource(getResources(), R.drawable.diren);

        erjihuancun = Bitmap.createBitmap(display_w, display_h, Bitmap.Config.ARGB_8888);
        gameImages.add(new BeijingImage(bg));
        //gameImages.add(new Plane(my));
        //gameImages.add(new Plane(hero1));
        gameImages.add(new Plane(hero2));
    }

    private Plane selectPlane;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (iGameImage game : gameImages) {
                if (game instanceof Plane) {
                    Plane plane = (Plane) game;
                    if (plane.getX() < event.getX() &&
                            plane.getY() < event.getY() &&
                            plane.getX() + plane.getWidth() > event.getX() &&
                            plane.getY() + plane.getHeight() > event.getY()) {
                        selectPlane = plane;
                    } else {
                        selectPlane = null;
                    }

                    break;
                }
            }
        }else if (event.getAction() == MotionEvent.ACTION_MOVE){

            if (selectPlane !=null){
                selectPlane.setX((int)event.getX()-selectPlane.getWidth()/2);
                selectPlane.setY((int)event.getY()-selectPlane.getHeight()/2);
            }
        }else if (event.getAction() == MotionEvent.ACTION_UP){
            selectPlane = null;
        }
        return true;
    }




    private class BeijingImage implements iGameImage {

        private Bitmap bg;
        private Bitmap newBitmap = null;
        private int height = 0;

        private BeijingImage(Bitmap bg) {
            this.bg = bg;
            newBitmap = Bitmap.createBitmap(display_w, display_h, Bitmap.Config.ARGB_8888);


        }


        @Override
        public Bitmap getBitmap() {
            Canvas canvas = new Canvas(newBitmap);
            Paint paint = new Paint();
            canvas.drawBitmap(bg,
                    new Rect(0, 0, bg.getWidth(), bg.getHeight()),
                    new Rect(0, height, display_w, display_h + height), paint);

            canvas.drawBitmap(bg,
                    new Rect(0, 0, bg.getWidth(), bg.getHeight()),
                    new Rect(0, -display_h + height, display_w, height), paint);
            height++;
            if (height == display_h) {
                height = 0;
            }
            return newBitmap;
        }

        @Override
        public int getX() {
            return 0;
        }

        @Override
        public int getY() {
            return 0;
        }
    }

    private class Plane implements iGameImage {

        private Bitmap my;
        private int x;
        private int y;
        private int width;
        private int height;

        private List<Bitmap> bitmaps = new ArrayList<Bitmap>();


        private Plane(Bitmap my) {
            this.my = my;

//            bitmaps.add(Bitmap.createBitmap(my, 0, 0, my.getWidth() / 4, my.getHeight()));
//            bitmaps.add(Bitmap.createBitmap(my, (my.getWidth() / 4) * 1, 0, my.getWidth() / 4, my.getHeight()));
//            bitmaps.add(Bitmap.createBitmap(my, (my.getWidth() / 4) * 2, 0, my.getWidth() / 4, my.getHeight()));
//            bitmaps.add(Bitmap.createBitmap(my, (my.getWidth() / 4) * 3, 0, my.getWidth() / 4, my.getHeight()));

            width = my.getWidth();
            height = my.getHeight();
            x = (display_w - my.getWidth()) / 2;
            y = display_h - my.getHeight() - 30;
        }

        private int index = 0;
        int num = 0;

        @Override
        public Bitmap getBitmap() {
            bitmaps.add(hero1);
            bitmaps.add(hero2);
            Bitmap bitmap = bitmaps.get(index);

            if (num == 10) {
                index++;
                Log.d("aaaaa",""+bitmaps.size());
                if (index == bitmaps.size()) {
                    index = 0;
                }
                num = 0;
            }
            num++;
            return bitmap;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public boolean select(int x, int y) {

            return false;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    private boolean state = false;
    private SurfaceHolder holder;

    @Override
    public void run() {
        Paint p1 = new Paint();
        try {
            while (state) {

                Canvas newCanvas = new Canvas(erjihuancun);
                for (iGameImage image : gameImages) {
                    newCanvas.drawBitmap(image.getBitmap(), image.getX(), image.getY(), p1);
                }
                Canvas canvas = holder.lockCanvas();
                canvas.drawBitmap(erjihuancun, 0, 0, p1);
                holder.unlockCanvasAndPost(canvas);
                Thread.sleep(0);
            }

        } catch (Exception e) {

        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        display_w = width;
        display_h = height;
        init();

        this.holder = holder;
        state = true;
        new Thread(this).start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        state = false;

    }

}
