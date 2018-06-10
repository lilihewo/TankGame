package com.lili.tank;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import com.lili.helper.MyAnimation;

// 子弹类
public class Bullet extends CCSprite {
	
	// 屏幕宽高
	public CGSize winSize = CCDirector.sharedDirector().winSize();
	
	public int bulletSpeed = 3; // 速度
	public float dx; // X偏移量
	public float dy; // Y偏移量
	
	public float bulletWidth;
	public float bulletHeight;

	public Bullet(String filePath) {
		super(filePath);
	}

	
	// 子弹移动
	public void move() {
		bulletWidth = this.getBoundingBox().size.width;
		bulletHeight = this.getBoundingBox().size.height;
		this.schedule("bulletMove", 0.003f);
	}
	
	// 子弹移动的定时器
	public void bulletMove(float f) {
		double rotation = this.getRotation();
		double radians = Math.toRadians(rotation); // 得到角度
		double sin = Math.sin(radians); // 计算正弦值
		double cos = Math.cos(radians); // 计算余弦值
		// 计算偏移量
		dx = (float) (bulletSpeed * cos);
		dy = (float) (bulletSpeed * sin);

		CGPoint point = this.getPosition();
		// 如果没有碰到屏幕边缘
		if (isNoTouch(point)) {
			// 可以移动
			this.setPosition(point.x - dx, point.y + dy);
			
			// 得到移动后的点
			CGPoint movePoint = this.getPosition();
			// 如果移动后的点碰到屏幕边缘
			if (!isNoTouch(movePoint)) {
				noShowBullet(); // 不再显示子弹
			} else if (isTouchOther()) { // 如果子弹的矩形碰到其他的矩形
				noShowBullet(); // 不再显示子弹
			}
			
		}
	}
	
	// 不再显示子弹
	private void noShowBullet() {
		// 停止定时器
		this.unschedule("bulletMove");
		// 播放音乐
//		SoundEngine.sharedEngine().playSound(CCDirector.theApp, R.raw.attack, false);
		
		CCCallFuncN callFuncN = CCCallFuncN.action(this, "destroy");
		CCAnimate animate = (CCAnimate) MyAnimation.animate("bullet/bullet%02d.png", 3, false);
		CCSequence sequence = CCSequence.actions(animate, callFuncN);
		this.runAction(sequence);
	}
	
	// 子弹销毁
	public void destroy(Object sender) {
		CCSprite sprite = (CCSprite) sender;
		// 子弹销毁
		sprite.removeSelf();
	}
	
	public boolean isNoTouch(CGPoint point) {
		// 如果坦克没有超出屏幕范围
		if (point.x-bulletWidth/2 >= 0 && point.x+bulletWidth/2 <= winSize.width &&
				point.y-bulletHeight/2 >= 0 && point.y+bulletHeight/2 <= winSize.height ) {
			return true;
		}
		
		return false;
	}
	
	// 判断子弹的矩形是否碰到其他的矩形
	public boolean isTouchOther() {
		

		return false;
	}

}








