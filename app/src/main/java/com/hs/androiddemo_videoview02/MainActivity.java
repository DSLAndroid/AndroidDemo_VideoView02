package com.hs.androiddemo_videoview02;
//
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
///**
// * 作者：单胜凌
// * 时间：2016.10.07日
// * 功能解释：
// * 表面视图SurfaceView的使用，播放视频，SurfaceView只是用来做展示的
// */
//public class MainActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{
//
//    private String TAG=MainActivity.class.getSimpleName();
//
//    //展示画面
//    private SurfaceView surfaceView;
//    //媒体播放，播放声音
//    private MediaPlayer player;
//    //播放进度的显示
//    private SeekBar seekBar;
//    //显示播放的时间
//    private TextView textView;
//    //暂停与开始播放的按钮
//    private ImageView imageView;
//    //控制循环的标志位
//    private boolean flag=true;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        //初始化
//        initView();
//        //找到表面视图
//        SurfaceHolder holder = surfaceView.getHolder();
//        holder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                Log.i(TAG,"主线程运行，创建表面视图");
//                //必须在surface创建后才能初始化MediaPlayer,否则不会显示图像
//                player=MediaPlayer.create(MainActivity.this,R.raw.hello);
//                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//                //设置视频显示在SurfaceView上
//                player.setDisplay(holder);
//                //player.setDataSource("路径或者网址");指定资源的第二种方式
//                //player.prepare();//这是同步的准备,如果是在创建媒体对象的时候就指定了播放源,就不用写这句了,否则异常
//                //player.prepareAsync();//这是异步的准备,因为是获取网络上的音频，如果用同步会出现页面卡顿的现象
//                //准备完毕的监听事件
//                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
//                        Log.i(TAG,"播放的准备工作完毕");
//                        seekBar.setMax(player.getDuration());//设置进度条的最大值为视频的总时间
//                        handler.sendEmptyMessageDelayed(1,1000);//发送消息，实现循环
//                    }
//                });
//                //音乐播放结束的监听
//                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        Log.i(TAG,"播放完毕");
//                        flag = false;//结束handler消息的发送，这里可以将player重制下reset
//                    }
//                });
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                Log.i(TAG, "主线程中运行的,改变了,第一次创建也会调用");
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                Log.i(TAG, "主线程中运行的,销毁了,activity从可见变为不可见就会调用");
//                if(player.isPlaying()){
//                    player.stop();
//                }
//                player.release();//释放资源
//                player = null;
//            }
//        });
//
//    }
//
//    protected void initView()
//    {
//        seekBar = (SeekBar)findViewById(R.id.seekBar);
//        textView= (TextView)findViewById(R.id.textView);
//        imageView=(ImageView)findViewById(R.id.imageView);
//        surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
//
//        imageView.setOnClickListener(this);
//        seekBar.setOnSeekBarChangeListener(this);
//    }
//
//    /**
//     * 播放按钮监听
//     * 进度条的监听
//     */
//    //--------------------播放键的监听---------------------
//    public void onClick(View v)//播放键的监听
//    {
//        if(player.isPlaying())//正在播放
//        {
//            player.pause();//暂停
//            imageView.setImageResource(android.R.drawable.ic_media_play);
//        }
//        else{
//            player.start();//开始或者继续播放
//            imageView.setImageResource(android.R.drawable.ic_media_pause);
//        }
//    }
//    //---------------------进度条监听------------------------
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//        player.pause();//开始拖动时，暂停播放
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//        player.seekTo(seekBar.getProgress());//播放到滑动停止滑动的位置
//        player.start();//停止拖动后，继续播放
//    }
//
//    private Handler handler = new Handler(){
//        public void HandleMessage(Message msg){
//            if(msg.what==1){
//                updataTime();//类似递归，调用方法向自己发送消息
//            }
//        }
//    };
//    /**
//     * 如果是开线程实现更新数据,在关闭的时候容易出现关不了的情况,前面有利用SufaceView实现gif图片的播放,摄像头画面预览就出现了这种情况
//     * 利用handler向自己发送消息,实现循环,这样能很好的避免线程关闭不了的情况了
//     */
//    private void updataTime(){
//        if(flag&&player != null)
//        {
//            int currentPosition = player.getCurrentPosition();//当前播放的毫秒数
//            seekBar.setProgress(currentPosition);
//
//            textView.setText(simpleTime(currentPosition)+"/"+simpleTime(seekBar.getMax()));
//            handler.sendEmptyMessageDelayed(1,1000);
//        }
//    }
//    /**
//     * 格式化时间,2d代表二个十进制数,02d代表当不足两位时前面补零
//     */
//    private String simpleTime(int time)
//    {
//        int hour = time / 1000 / 3600;
//        int minute = time / 1000 % 3600 /60;
//        int second = time / 1000 % 60;
//        return String.format("%02d:%02d:%02d",hour,minute,second);//00:00:00的样式
//    }
//    protected void onDestroy()
//    {
//        super.onDestroy();
//    }
//
//}

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * 表面视图SurfaceView的使用,播放视频,SurfaceView只是用来做展示的
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    /**
     * 展示画面
     */
    private SurfaceView surfaceView;

    /**
     * 媒体播放,播放声音
     */
    private MediaPlayer player;

    /**
     * 播放进度的显示
     */
    private SeekBar seekBar;

    /**
     * 显示播放的时间
     */
    private TextView textView;

    /**
     * 暂停与开始播放的按钮
     */
    private ImageView imageView;

    /**
     * 控制循环的标志位
     */
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        //找到表面视图
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i("tag", "主线程运行的,创建表面视图");
                //必须在surface创建后才能初始化MediaPlayer,否则不会显示图像
                player=MediaPlayer.create(MainActivity.this,R.raw.hello);
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                //设置视频显示在SurfaceView上
                player.setDisplay(holder);
                //player.setDataSource("路径或者网址");指定资源的第二种方式
                //player.prepare();//这是同步的准备,如果是在创建媒体对象的时候就指定了播放源,就不用写这句了,否则异常
                //player.prepareAsync();//这是异步的准备,因为是获取网络上的音频，如果用同步会出现页面卡顿的现象
                //准备完毕的监听事件
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Log.i("tag", "播放的准备工作完毕");
                        seekBar.setMax(player.getDuration());//设置滑动条的最大值为视频的总时间
                        handler.sendEmptyMessageDelayed(1,1000);//发送消息,实现循环
                    }
                });
                //音乐播放结束的监听
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Log.i("tag", "播放完毕");
                        imageView.setImageResource(android.R.drawable.ic_media_play);
                    }
                });
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if(player.isPlaying()){
                    player.stop();
                    imageView.setImageResource(android.R.drawable.ic_media_play);
                }
            }
        });
    }
    //------------------------播放按钮的监听--------------------------
    @Override
    public void onClick(View v) {
        if(player.isPlaying()){//正在播放
            player.pause();//暂停,   设置为暂停的图标
            imageView.setImageResource(android.R.drawable.ic_media_play);
        }else{
            player.start();//开始或者继续播放
            imageView.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    //------------------------seekBar的监听事件--------------------------
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        player.pause();//开始拖动时,暂停播放
        imageView.setImageResource(android.R.drawable.ic_media_play);//改变播放按钮的状态
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        player.seekTo(seekBar.getProgress());//播放到滑动条停止滑动的位置
        player.start();//停止拖动后,继续播放
        imageView.setImageResource(android.R.drawable.ic_media_pause);//改变播放按钮的状态
    }

    /**
     * 更新显示
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                updataTime();//类似于递归,调用方法向自己发送消息
            }
        }
    };

    /**
     * 如果是开线程实现更新数据,在关闭的时候容易出现关不了的情况,前面有利用SufaceView实现gif图片的播放,摄像头画面预览就出现了这种情况
     * 利用handler向自己发送消息,实现循环,这样能很好的避免线程关闭不了的情况了
     */
    private void updataTime(){
        if(player != null){
            int currentPosition = player.getCurrentPosition();//当前播放的毫秒数
            seekBar.setProgress(currentPosition);
            textView.setText(simpleTime(currentPosition)+"/"+simpleTime(seekBar.getMax()));//设置播放时间
            handler.sendEmptyMessageDelayed(1,1000);
        }
    }

    /**
     * 格式化时间,2d代表二个十进制数,02d代表当不足两位时前面补零
     */
    private String simpleTime(int time){
        int hour = time / 1000 / 3600;
        int minute = time / 1000 % 3600 /60;
        int second = time / 1000 % 60;
        return String.format("%02d:%02d:%02d",hour,minute,second);//00:00:00的样式
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();//释放资源
        player = null;
    }
}
