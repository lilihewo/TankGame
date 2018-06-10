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
 * ս��ͼ��
 * @author shiyang
 *
 */
/**
 * �й���ѧ����Ĵ���ο���Himi��Դ����
 */
public class FightLayer extends CCLayer {
	// ��Ļ���
	private CGSize winSize = CCDirector.sharedDirector().winSize();
	public SoundEngine engine;
	private final static int SHOW = 5;
	
	private float rockerX = 70;
	private float rockerY = 70;
	private float smallCenterX = rockerX, smallCenterY = rockerY;
	private float BigCenterX = rockerX, BigCenterY = rockerY;
	private float BigCenterR = 50;
	
	private CCSprite bigRound; // ��Բ
	private CCSprite smallRound; // СԲ
	private CCSprite fireButton;
	private MyTank myTank = null; // �ҵ�̹��
	public void setMyTank(MyTank myTank) {
		this.myTank = myTank;
	}

	public MyTank getMyTank() {
		return myTank;
	}
	
	
	private EnemyTank enemyTank = null; // ���˵�̹��
	public void setEnemyTank(EnemyTank enemyTank) {
		this.enemyTank = enemyTank;
	}

	public EnemyTank getEnemyTank() {
		return enemyTank;
	}

	public FightLayer() {
		initFightLayer(); // ��ʼ��ͼ��
		
		this.setIsTouchEnabled(true); // ͼ����Խ��մ����¼�
	}


	// ��ʼ��ͼ��
	private void initFightLayer() {
		// ����
		CCSprite bg = CCSprite.sprite("blue.jpg");
		bg.setAnchorPoint(0, 0);
		bg.setScale(1.2f);
		this.addChild(bg);
		
		// --> ҡ��
		// ��Բ
		bigRound = CCSprite.sprite("big.png");
		bigRound.setPosition(BigCenterX, BigCenterY);
		this.addChild(bigRound);
		// СԲ
		smallRound = CCSprite.sprite("small.png");
		smallRound.setPosition(smallCenterX, smallCenterY);
		smallRound.setOpacity(100);
		this.addChild(smallRound);
		// ���䰴ť
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
	
	// ���ؾ���
	private void loadSprite() {
		// �ҵ�̹��
		myTank = new MyTank("tk49.png", this);
		myTank.setScale(0.3f);
		myTank.setPosition(200, 150);
		this.addChild(myTank, SHOW);
		
		// ���˵�̹��
		enemyTank = new EnemyTank("tank/tk003.png", this);
		enemyTank.setScale(0.3f);
		enemyTank.setPosition(300, 230);
		this.addChild(enemyTank, SHOW);
		
//		for (int i = 0; i < 3; i++) {
//			// ���˵�̹��
//			enemyTank = new EnemyTank("tank/tk003.png", this);
//			enemyTank.setScale(0.3f);
//			enemyTank.setPosition(200+i*30, 230);
//			this.addChild(enemyTank, SHOW);
//		}
	}

	
	// ��ʼʱ�Ƿ��ڴ�Բ��
	private boolean isBeginOnBig = false;
	// �Ƿ�Ҫ�ƶ�
	public boolean isMove = false;

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		CGPoint beganPoint = convertTouchToNodeSpace(event);
		// �����ָ��λ���ڴ�Բ��
		if (CGRect.containsPoint(bigRound.getBoundingBox(), beganPoint)) {
			isBeginOnBig = true;
			isMove = true;
		}
		if (CGRect.containsPoint(fireButton.getBoundingBox(), beganPoint)) {
			fireButton.setScale(0.8f);
			// �ҵ�̹�˷����ӵ�
			if (myTank != null) {
				myTank.fire();
			}
		}
		return super.ccTouchesBegan(event);
	}
	
	// ��ָ�ƶ�ʱ
	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
	
		moveSmall(convertTouchToNodeSpace(event));
		return super.ccTouchesMoved(event);
	}
	
	
	// �ƶ�СԲ
	private void moveSmall(CGPoint movePoint) {
		// �����ָ��λ���ڴ�Բ��
		if (CGRect.containsPoint(bigRound.getBoundingBox(), movePoint)) {
			isMove = true;
			
			// ��ָ��λ�þ���СԲ��λ��
			smallRound.setPosition(movePoint.x, movePoint.y);
			double rad = getRad(BigCenterX, BigCenterY, movePoint.x, movePoint.y);
			int rotation = (int) (rad*180/Math.PI);
			if (myTank != null) {
				myTank.setRotation(360-(rotation + 180));
			}
		} else { // �����ָ��λ�ò��ڴ�Բ��
			if (isBeginOnBig) {
				double rad = getRad(BigCenterX, BigCenterY, movePoint.x, movePoint.y);
				int rotation = (int) (rad*180/Math.PI);
				if (myTank != null) {
					myTank.setRotation(360-(rotation + 180));
				}
				// СԲ����ָ��Բ���˶�
				setSmallCircleXY(BigCenterX, BigCenterY, BigCenterR, rad);
				
			}
		}
	}
	
	// ��ָ̧��ʱ
	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		CGPoint endPoint = convertTouchToNodeSpace(event);
		// ���̧�����ָ�ǵ���˷��䰴ť��
		if (CGRect.containsPoint(fireButton.getBoundingBox(), endPoint)) {
			fireButton.setScale(0.7f);
		} else { // ���̧�����ָ���ǵ���ŷ��䰴ť��
			isBeginOnBig = false;
			isMove = false;
			// СԲ�ָ���ԭ����λ��
			smallRound.setPosition(BigCenterX, BigCenterY);
		}

		return super.ccTouchesEnded(event);
	}
	
	public void setSmallCircleXY(float centerX, float centerY, float R, double rad) {
		//��ȡԲ���˶���X����   
		smallCenterX = (float) (R * Math.cos(rad)) + centerX;
		//��ȡԲ���˶���Y����  
		smallCenterY = (float) (R * Math.sin(rad)) + centerY;
		smallRound.setPosition(smallCenterX, smallCenterY);
	}
	
	public double getRad(float px1, float py1, float px2, float py2) {
		//�õ�����X�ľ���  
		float x = px2 - px1;
		//�õ�����Y�ľ���  
		float y = py1 - py2;
		//���б�߳�  
		float Hypotenuse = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		//�õ�����Ƕȵ�����ֵ��ͨ�����Ǻ����еĶ��� ���ڱ�/б��=�Ƕ�����ֵ��  
		float cosAngle = x / Hypotenuse;
		//ͨ�������Ҷ����ȡ����ǶȵĻ���  
		float rad = (float) Math.acos(cosAngle);
		//��������λ��Y����<ҡ�˵�Y��������Ҫȡ��ֵ-0~-180  
		if (py2 < py1) {
			rad = -rad;
		}
		return rad;
	}
	
}









