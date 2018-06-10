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
		
		 // 1.����CCGLSurfaceView����
        CCGLSurfaceView view = new CCGLSurfaceView(this);
        // 2.����Activity��ʾ��View
        this.setContentView(view);
        
        // ��ȡ���ݶ���
        CCDirector director = CCDirector.sharedDirector();
        // ��ʼ�����߳�
        director.attachInView(view);
		director.setDeviceOrientation(CCDirector.kCCDeviceOrientationPortrait);//���ú���
		director.setScreenSize(480, 320);// ������Ļ����, ����ڲ�ͬ��С����Ļ�ȱ�������,
		// ������Ϸ��ˢ���� ÿ��60֡
        director.setAnimationInterval(1.0 / 60.0);
        // ��ʾ��ǰ��ˢ����
//        director.setDisplayFPS(true);
        
        //����һ����������
        CCScene scene = CCScene.node();

        FightLayer fightLayer = new FightLayer();
        scene.addChild(fightLayer);

        //�������г���
        director.runWithScene(scene); 
	}
	
	/**
	 * ��ͣ
	 */
	@Override
	protected void onPause() 
	{
		super.onPause();

		CCDirector.sharedDirector().onPause(); //��Ϸ��ͣ
		// 	SoundEngine.sharedEngine().pauseSound(); //��ͣ����
	}

	/**
	 * ����
	 */
	@Override
	protected void onResume() 
	{
		super.onResume();

		CCDirector.sharedDirector().onResume(); //��Ϸ����
		// 	SoundEngine.sharedEngine().resumeSound(); //���ּ���
	}

	/**
	 * ����
	 */
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();

		CCDirector.sharedDirector().end(); //��Ϸ����
		// 	SoundEngine.sharedEngine().realesAllSounds(); // ���ֽ���
	}

	
	// �û����·��ؼ�
		@Override
	   	public boolean onKeyDown(int keyCode, KeyEvent event)
	   	{
	   		if (keyCode == KeyEvent.KEYCODE_BACK )
	   		{
	   			AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
	   			builder.setIcon(R.drawable.icon);
	   			builder.setTitle("�˳�����Ϸ��");
	   			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
	   			{
	   				@Override
	   				public void onClick(DialogInterface dialog, int which)
	   				{
	   					// ֹͣ����
	   					SoundEngine.sharedEngine().realesAllSounds();
	   					// ����GameActivity
	   					GameActivity.this.finish();
	   				}
	   			});


	   			builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
	   			{
	   				@Override
	   				public void onClick(DialogInterface dialog, int which)
	   				{

	   				}
	   			});


	   			//    ��ʾ���öԻ���
	   			builder.show();
	   		}

	   		return false;
	   	}
 
}
















