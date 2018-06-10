package com.lili.tankgame;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

import com.lili.layer.FightLayer;

public class GameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 // 1.创建CCGLSurfaceView对象
        CCGLSurfaceView view = new CCGLSurfaceView(this);
        // 2.设置Activity显示的View
        this.setContentView(view);
        
        // 获取导演对象
        CCDirector director = CCDirector.sharedDirector();
        // 开始绘制线程
        director.attachInView(view);
		director.setDeviceOrientation(CCDirector.kCCDeviceOrientationPortrait);//设置横屏
		director.setScreenSize(480, 320);// 用于屏幕适配, 会基于不同大小的屏幕等比例缩放,
		// 设置游戏的刷新率 每秒60帧
        director.setAnimationInterval(1.0 / 60.0);
        // 显示当前的刷新率
//        director.setDisplayFPS(true);
        
        //创建一个场景对象
        CCScene scene = CCScene.node();

        FightLayer fightLayer = new FightLayer();
        scene.addChild(fightLayer);

        //导演运行场景
        director.runWithScene(scene); 
	}
	
	/**
	 * 暂停
	 */
	@Override
	protected void onPause() 
	{
		super.onPause();

		CCDirector.sharedDirector().onPause(); //游戏暂停
		// 	SoundEngine.sharedEngine().pauseSound(); //暂停音乐
	}

	/**
	 * 继续
	 */
	@Override
	protected void onResume() 
	{
		super.onResume();

		CCDirector.sharedDirector().onResume(); //游戏继续
		// 	SoundEngine.sharedEngine().resumeSound(); //音乐继续
	}

	/**
	 * 结束
	 */
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();

		CCDirector.sharedDirector().end(); //游戏结束
		// 	SoundEngine.sharedEngine().realesAllSounds(); // 音乐结束
	}

	
	// 用户按下返回键
		@Override
	   	public boolean onKeyDown(int keyCode, KeyEvent event)
	   	{
	   		if (keyCode == KeyEvent.KEYCODE_BACK )
	   		{
	   			AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
	   			builder.setIcon(R.drawable.icon);
	   			builder.setTitle("退出本游戏？");
	   			builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
	   			{
	   				@Override
	   				public void onClick(DialogInterface dialog, int which)
	   				{
	   					// 停止音乐
	   					SoundEngine.sharedEngine().realesAllSounds();
	   					// 销毁GameActivity
	   					GameActivity.this.finish();
	   				}
	   			});


	   			builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
	   			{
	   				@Override
	   				public void onClick(DialogInterface dialog, int which)
	   				{

	   				}
	   			});


	   			//    显示出该对话框
	   			builder.show();
	   		}

	   		return false;
	   	}
 
}
















