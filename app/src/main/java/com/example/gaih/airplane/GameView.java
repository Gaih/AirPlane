package com.example.gaih.airplane;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
import java.util.Random;

/**
 * Created by gaih on 2016/7/31.
 */

public class GameView extends SurfaceView
        implements SurfaceHolder.Callback, Runnable, View.OnTouchListener {

    private Bitmap my;
    private Bitmap baozha;
    private Bitmap bg;
    private Bitmap army;
    private Bitmap zidan;
    private Bitmap hero1;
    private Bitmap hero2;



    private Bitmap erjihuancun;

    private WindowManager windowManager;
    private int display_w;
    private int display_h;

    private ArrayList<Zidan> zidans = new ArrayList<Zidan>();
    private ArrayList<iGameImage> gameImages = new ArrayList();


    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        this.setOnTouchListener(this);
    }
//子弹
    public class Zidan implements iGameImage{
        private Bitmap zidan;
        private Plane plane;
        private int x;
        private int y;


        public Zidan(Plane plane,Bitmap zidan){
            this.zidan = zidan;
            this.plane = plane;
            x=(plane.getX()+plane.getWidth()/2-8);
            y=(plane.getY()-zidan.getHeight());


        }

        @Override
        public Bitmap getBitmap() {
            y-=30;
            if (y<=-10){
                zidans.remove(this);
            }
            return zidan;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }
    }

    private void init() {
        hero1 = BitmapFactory.decodeResource(getResources(), R.drawable.hero1);
        hero2 = BitmapFactory.decodeResource(getResources(), R.drawable.hero2);

        my = BitmapFactory.decodeResource(getResources(), R.drawable.my);
        baozha = BitmapFactory.decodeResource(getResources(), R.drawable.baozha);
        zidan = BitmapFactory.decodeResource(getResources(), R.drawable.zidan);
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        army = BitmapFactory.decodeResource(getResources(), R.drawable.diren);

        erjihuancun = Bitmap.createBitmap(display_w, display_h, Bitmap.Config.ARGB_8888);
        gameImages.add(new BeijingImage(bg));
        gameImages.add(new Plane(my));
//        gameImages.add(new Plane(hero1));
//        gameImages.add(new Plane(hero2));
        gameImages.add(new DijiImage(army,baozha));
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



//背景
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

            bitmaps.add(Bitmap.createBitmap(my, 0, 0, my.getWidth() / 4, my.getHeight()));
            bitmaps.add(Bitmap.createBitmap(my, (my.getWidth() / 4) * 1, 0, my.getWidth() / 4, my.getHeight()));
            bitmaps.add(Bitmap.createBitmap(my, (my.getWidth() / 4) * 2, 0, my.getWidth() / 4, my.getHeight()));
            bitmaps.add(Bitmap.createBitmap(my, (my.getWidth() / 4) * 3, 0, my.getWidth() / 4, my.getHeight()));
//            bitmaps.add(hero1);
//            bitmaps.add(hero2);
            width = my.getWidth() / 4;
            height = my.getHeight();
            x = (display_w - my.getWidth() / 4) / 2;
            y = display_h - my.getHeight() - 30;
        }

        private int index = 0;
        int num = 0;

        @Override
        public Bitmap getBitmap() {

            Bitmap bitmap = bitmaps.get(index);

            if (num == 10) {
                index++;
                if (index == bitmaps.size()) {
                    Log.d("aaaa",""+bitmaps.size());
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
    //敌机
    private class DijiImage implements  iGameImage{

        private Bitmap diren = null;
        private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
        private List<Bitmap> baozhas = new ArrayList<Bitmap>();

        private int x;
        private int y;
        private int width;
        private int height;

        public DijiImage(Bitmap diren,Bitmap baozha){
            this.diren = diren;
            bitmaps.add(Bitmap.createBitmap(diren,0,0,diren.getWidth()/4,diren.getHeight()));
            bitmaps.add(Bitmap.createBitmap(diren,(diren.getWidth()/4)*1,0,diren.getWidth()/4,diren.getHeight()));
            bitmaps.add(Bitmap.createBitmap(diren,(diren.getWidth()/4)*2,0,diren.getWidth()/4,diren.getHeight()));
            bitmaps.add(Bitmap.createBitmap(diren,(diren.getWidth()/4)*3,0,diren.getWidth()/4,diren.getHeight()));

            baozhas.add(Bitmap.createBitmap(baozha,0,0,baozha.getWidth()/4,baozha.getHeight()/2));
            baozhas.add(Bitmap.createBitmap(baozha,(baozha.getWidth()/4)*1,0,baozha.getWidth()/4,baozha.getHeight()/2));
            baozhas.add(Bitmap.createBitmap(baozha,(baozha.getWidth()/4)*2,0,baozha.getWidth()/4,baozha.getHeight()/2));
            baozhas.add(Bitmap.createBitmap(baozha,(baozha.getWidth()/4)*3,0,baozha.getWidth()/4,baozha.getHeight()/2));

            baozhas.add(Bitmap.createBitmap(baozha,0,baozha.getHeight()/2,baozha.getWidth()/4,baozha.getHeight()/2));
            baozhas.add(Bitmap.createBitmap(baozha,(baozha.getWidth()/4)*1,baozha.getHeight()/2,baozha.getWidth()/4,baozha.getHeight()/2));
            baozhas.add(Bitmap.createBitmap(baozha,(baozha.getWidth()/4)*2,baozha.getHeight()/2,baozha.getWidth()/4,baozha.getHeight()/2));
            baozhas.add(Bitmap.createBitmap(baozha,(baozha.getWidth()/4)*3,baozha.getHeight()/2,baozha.getWidth()/4,baozha.getHeight()/2));




            width = diren.getWidth()/4;
            height = diren.getHeight();

            y= -diren.getHeight();
            Random random = new Random();
            x=random.nextInt(display_w - (diren.getWidth()/4));


        }

        private int index = 0;
        private int num = 0;
        @Override
        public Bitmap getBitmap() {
            Bitmap bitmap = bitmaps.get(index);
            if (num ==10){
                index++;
                if (index==8&&dead){
                    gameImages.remove(this);
                }
                if (index == bitmaps.size()){
                    index = 0;
                }
                num = 0;
            }
            y+=dijishu;
            num++;
            if (y>display_h){
                gameImages.remove(this);
            }
            return bitmap;
        }

        private boolean dead = false;

        public void attack(ArrayList<Zidan> zidans){
            if (!dead){
                for (iGameImage zidan : (List<iGameImage>)zidans.clone()){

                    if (zidan.getX()>x&&zidan.getY()>y&&
                            zidan.getX()<width+x&&zidan.getY()<y+height){
                        zidans.remove(zidan);
                        dead = true;
                        bitmaps = baozhas;
                        fenshu+=10;
                        break;
                    }
                }
            }

        }
        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
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
        thread = new Thread(this);
        thread.start();

    }
    Thread thread = null;

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        state = false;

    }


    private boolean state = false;
    private SurfaceHolder holder;
    private long fenshu = 0;
    private int guanka = 1;

    private int chudishu = 30;
    private int dijishu = 2;
    private int xiayiguan = 100;



    private int[][]sj ={{1,100,30,2},
            {2,200,30,3},
            {3,300,25,3},
            {4,400,20,4},
            {5,500,20,5},
            {6,600,15,5},
            {7,700,10,6},
            {8,800,10,7},
            {9,900,10,8},
            {10,1000,10,9},

    };

    private boolean stopState = false;
    public void stop(){
        stopState = true;
    }
    public void start(){
        stopState = false;
        thread.interrupt();
    }

    @Override
    public void run() {
        Paint p1 = new Paint();
        int army_num = 0;
        int zidan_num = 0;
        Paint p2 = new Paint();
        p2.setColor(Color.BLACK);
        p2.setTextSize(40);
        p2.setDither(true);
        p2.setAntiAlias(true);
        try {
            while (state) {
                while (stopState){
                    try {
                        thread.sleep(100000);
                    }catch (Exception e){

                    }
                }
                if (selectPlane !=null){
                    if (zidan_num ==5){
                        zidans.add(new Zidan(selectPlane,zidan));
                        zidan_num=0;
                    }
                    zidan_num++;
                }
                Canvas newCanvas = new Canvas(erjihuancun);
                for (iGameImage image : (List<iGameImage>)gameImages.clone()) {
                    if (image instanceof  DijiImage){
                        ((DijiImage)image).attack(zidans);
                    }
                    newCanvas.drawBitmap(image.getBitmap(), image.getX(), image.getY(), p1);
                }
                for (iGameImage image : (List<iGameImage>)zidans.clone()){
                    newCanvas.drawBitmap(image.getBitmap(), image.getX(), image.getY(), p1);
                }

                newCanvas.drawText("分数："+fenshu,0,50,p2);
                newCanvas.drawText("关卡："+guanka,0,100,p2);
                newCanvas.drawText("下一关："+xiayiguan,0,150,p2);

                if (sj[guanka-1][1]<=fenshu){
                    chudishu = sj[guanka][2];
                    dijishu = sj[guanka][3];
                    fenshu = sj[guanka-1][1]-fenshu;
                    xiayiguan = sj[guanka][1];
                    guanka = sj[guanka][0];
                }

                if (army_num ==chudishu){
                    army_num = 0;
                    gameImages.add(new DijiImage(army,baozha));
                }
                army_num++;
                Canvas canvas = holder.lockCanvas();
                canvas.drawBitmap(erjihuancun, 0, 0, p1);
                holder.unlockCanvasAndPost(canvas);
                Thread.sleep(0);
            }

        } catch (Exception e) {

        }

    }
}
