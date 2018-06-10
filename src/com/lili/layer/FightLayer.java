package com.lili.layer;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;

import android.view.MotionEvent;

import com.lili.tank.EnemyTank;
import com.lili.tank.MyTank;
import com.lili.tankgame.R;

/**
 * 战斗图层
 * @author shiyang
 *
 */
/**
 * 有关数学计算的代码参考了Himi的源代码
 */
public class FightLayer extends CCLayer {
	// 屏幕宽高
	private CGSize winSize = CCDirector.sharedDirector().winSize();
	public SoundEngine engine;
	private final static int SHOW = 5;
	
	private float rockerX = 70;
	private float rockerY = 70;
	private float smallCenterX = rockerX, smallCenterY = rockerY;
	private float BigCenterX = rockerX, BigCenterY = rockerY;
	private float BigCenterR = 50;
	
	private CCSprite bigRound; // 大圆
	private CCSprite smallRound; // 小圆
	private CCSprite fireButton;
	private MyTank myTank = null; // 我的坦克
	public void setMyTank(MyTank myTank) {
		this.myTank = myTank;
	}

	public MyTank getMyTank() {
		return myTank;
	}
	
	
	private EnemyTank enemyTank = null; // 敌人的坦克
	public void setEnemyTank(EnemyTank enemyTank) {
		this.enemyTank = enemyTank;
	}

	public EnemyTank getEnemyTank() {
		return enemyTank;
	}

	public FightLayer() {
		initFightLayer(); // 初始化图层
		
		this.setIsTouchEnabled(true); // 图层可以接收触摸事件
	}


	// 初始化图层
	private void initFightLayer() {
		// 背景
		CCSprite bg = CCSprite.sprite("blue.jpg");
		bg.setAnchorPoint(0, 0);
		bg.setScale(1.2f);
		this.addChild(bg);
		
		// --> 摇杆
		// 大圆
		bigRound = CCSprite.sprite("big.png");
		bigRound.setPosition(BigCenterX, BigCenterY);
		this.addChild(bigRound);
		// 小圆
		smallRound = CCSprite.sprite("small.png");
		smallRound.setPosition(smallCenterX, smallCenterY);
		smallRound.setOpacity(100);
		this.addChild(smallRound);
		// 发射按钮
		fireButton = CCSprite.sprite("fire.png");
		fireButton.setPosition(winSize.width-(fireButton.getBoundingBox().size.width/2+10), 
				fireButton.getBoundingBox().size.height/2 + 10);
		fireButton.setScale(0.7f);
		this.addChild(fireButton);
		
		engine = SoundEngine.sharedEngine();
		engine.preloadSound(CCDirector.theApp, R.raw.shoot);
		engine.preloadSound(CCDirector.theApp, R.raw.explosion3);
		
		loadSprite();
	}
	
	// 加载精灵
	private void loadSprite() {
		// 我的坦克
		myTank = new MyTank("tk49.png", this);
		myTank.setScale(0.3f);
		myTank.setPosition(200, 150);
		this.addChild(myTank, SHOW);
		
		// 敌人的坦克
		enemyTank = new EnemyTank("tank/tk003.png", this);
		enemyTank.setScale(0.3f);
		enemyTank.setPosition(300, 230);
		this.addChild(enemyTank, SHOW);
		
//		for (int i = 0; i < 3; i++) {
//			// 敌人的坦克
//			enemyTank = new EnemyTank("tank/tk003.png", this);
//			enemyTank.setScale(0.3f);
//			enemyTank.setPosition(200+i*30, 230);
//			this.addChild(enemyTank, SHOW);
//		}
	}

	
	// 开始时是否在大圆内
	private boolean isBeginOnBig = false;
	// 是否要移动
	public boolean isMove = false;

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		CGPoint beganPoint = convertTouchToNodeSpace(event);
		// 如果手指的位置在大圆内
		if (CGRect.containsPoint(bigRound.getBoundingBox(), beganPoint)) {
			isBeginOnBig = true;
			isMove = true;
		}
		if (CGRect.containsPoint(fireButton.getBoundingBox(), beganPoint)) {
			fireButton.setScale(0.8f);
			// 我的坦克发射子弹
			if (myTank != null) {
				myTank.fire();
			}
		}
		return super.ccTouchesBegan(event);
	}
	
	// 手指移动时
	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
	
		moveSmall(convertTouchToNodeSpace(event));
		return super.ccTouchesMoved(event);
	}
	
	
	// 移动小圆
	private void moveSmall(CGPoint movePoint) {
		// 如果手指的位置在大圆内
		if (CGRect.containsPoint(bigRound.getBoundingBox(), movePoint)) {
			isMove = true;
			
			// 手指的位置就是小圆的位置
			smallRound.setPosition(movePoint.x, movePoint.y);
			double rad = getRad(BigCenterX, BigCenterY, movePoint.x, movePoint.y);
			int rotation = (int) (rad*180/Math.PI);
			if (myTank != null) {
				myTank.setRotation(360-(rotation + 180));
			}
		} else { // 如果手指的位置不在大圆内
			if (isBeginOnBig) {
				double rad = getRad(BigCenterX, BigCenterY, movePoint.x, movePoint.y);
				int rotation = (int) (rad*180/Math.PI);
				if (myTank != null) {
					myTank.setRotation(360-(rotation + 180));
				}
				// 小圆随手指做圆周运动
				setSmallCircleXY(BigCenterX, BigCenterY, BigCenterR, rad);
				
			}
		}
	}
	
	// 手指抬起时
	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		CGPoint endPoint = convertTouchToNodeSpace(event);
		// 如果抬起的手指是点击了发射按钮的
		if (CGRect.containsPoint(fireButton.getBoundingBox(), endPoint)) {
			fireButton.setScale(0.7f);
		} else { // 如果抬起的手指不是点击着发射按钮的
			isBeginOnBig = false;
			isMove = false;
			// 小圆恢复到原来的位置
			smallRound.setPosition(BigCenterX, BigCenterY);
		}

		return super.ccTouchesEnded(event);
	}
	
	public void setSmallCircleXY(float centerX, float centerY, float R, double rad) {
		//获取圆周运动的X坐标   
		smallCenterX = (float) (R * Math.cos(rad)) + centerX;
		//获取圆周运动的Y坐标  
		smallCenterY = (float) (R * Math.sin(rad)) + centerY;
		smallRound.setPosition(smallCenterX, smallCenterY);
	}
	
	public double getRad(float px1, float py1, float px2, float py2) {
		//得到两点X的距离  
		float x = px2 - px1;
		//得到两点Y的距离  
		float y = py1 - py2;
		//算出斜边长  
		float Hypotenuse = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		//得到这个角度的余弦值（通过三角函数中的定理 ：邻边/斜边=角度余弦值）  
		float cosAngle = x / Hypotenuse;
		//通过反余弦定理获取到其角度的弧度  
		float rad = (float) Math.acos(cosAngle);
		//当触屏的位置Y坐标<摇杆的Y坐标我们要取反值-0~-180  
		if (py2 < py1) {
			rad = -rad;
		}
		return rad;
	}
	
}









