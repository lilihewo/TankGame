package com.lili.helper;

import java.util.ArrayList;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * ����������
 * @author shiyang
 *
 */
public class MyAnimation {
	/**
	 * ����������
	 * 
	 * @param format
	 *            ·����format
	 * @param num
	 *            ֡��
	 * @param repeat
	 *            �����Ƿ�ѭ��
	 * @return
	 */
	public static CCAction animate(String format, int num, boolean repeat) {
		 return animate(format,num,repeat,0.2f);
	}
	
	
	public static CCAction animate(String format, int num, boolean repeat, float time) {
		ArrayList<CCSpriteFrame> frames = new ArrayList<CCSpriteFrame>();
		for (int i = 1; i <= num; i++) {
			frames.add(CCSprite.sprite(String.format(format, i))
					.displayedFrame());
		}

		CCAnimation anim = CCAnimation.animation("loading", time, frames);// ��2��ʾÿһ֡��ʾʱ��

		if (!repeat) {
			CCAnimate animate = CCAnimate.action(anim, false);// ��2��false��ʾִֻ��һ��,Ĭ����true
			return animate;
		} else {
			CCAnimate animate = CCAnimate.action(anim);
			CCRepeatForever r = CCRepeatForever.action(animate);
			return r;
		}
	}
	
	public static CCAction animateWithFile(String format, int num, boolean repeat) {
		return animateWithFile(format,num,repeat,0.2f);
	}
	
	
	public static CCAction animateWithFile(String format, int num, boolean repeat, float time) {
		ArrayList<CCSpriteFrame> frames = new ArrayList<CCSpriteFrame>();
		for (int i = 1; i <= num; i++) {
			Bitmap myBitmap = BitmapFactory.decodeFile(String.format(format, i));
			frames.add(CCSprite.sprite(myBitmap).displayedFrame());
		}

		CCAnimation anim = CCAnimation.animation("loading", time, frames);// ��2��ʾÿһ֡��ʾʱ��

		if (!repeat) {
			CCAnimate animate = CCAnimate.action(anim, false);// ��2��false��ʾִֻ��һ��,Ĭ����true
			return animate;
		} else {
			CCAnimate animate = CCAnimate.action(anim);
			CCRepeatForever r = CCRepeatForever.action(animate);
			return r;
		}
	}
	
	
}














