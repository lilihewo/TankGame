package com.lili.tank;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import com.lili.helper.MyAnimation;

// �ӵ���
public class Bullet extends CCSprite {
	
	// ��Ļ���
	public CGSize winSize = CCDirector.sharedDirector().winSize();
	
	public int bulletSpeed = 3; // �ٶ�
	public float dx; // Xƫ����
	public float dy; // Yƫ����
	
	public float bulletWidth;
	public float bulletHeight;

	public Bullet(String filePath) {
		super(filePath);
	}

	
	// �ӵ��ƶ�
	public void move() {
		bulletWidth = this.getBoundingBox().size.width;
		bulletHeight = this.getBoundingBox().size.height;
		this.schedule("bulletMove", 0.003f);
	}
	
	// �ӵ��ƶ��Ķ�ʱ��
	public void bulletMove(float f) {
		double rotation = this.getRotation();
		double radians = Math.toRadians(rotation); // �õ��Ƕ�
		double sin = Math.sin(radians); // ��������ֵ
		double cos = Math.cos(radians); // ��������ֵ
		// ����ƫ����
		dx = (float) (bulletSpeed * cos);
		dy = (float) (bulletSpeed * sin);

		CGPoint point = this.getPosition();
		// ���û��������Ļ��Ե
		if (isNoTouch(point)) {
			// �����ƶ�
			this.setPosition(point.x - dx, point.y + dy);
			
			// �õ��ƶ���ĵ�
			CGPoint movePoint = this.getPosition();
			// ����ƶ���ĵ�������Ļ��Ե
			if (!isNoTouch(movePoint)) {
				noShowBullet(); // ������ʾ�ӵ�
			} else if (isTouchOther()) { // ����ӵ��ľ������������ľ���
				noShowBullet(); // ������ʾ�ӵ�
			}
			
		}
	}
	
	// ������ʾ�ӵ�
	private void noShowBullet() {
		// ֹͣ��ʱ��
		this.unschedule("bulletMove");
		// ��������
//		SoundEngine.sharedEngine().playSound(CCDirector.theApp, R.raw.attack, false);
		
		CCCallFuncN callFuncN = CCCallFuncN.action(this, "destroy");
		CCAnimate animate = (CCAnimate) MyAnimation.animate("bullet/bullet%02d.png", 3, false);
		CCSequence sequence = CCSequence.actions(animate, callFuncN);
		this.runAction(sequence);
	}
	
	// �ӵ�����
	public void destroy(Object sender) {
		CCSprite sprite = (CCSprite) sender;
		// �ӵ�����
		sprite.removeSelf();
	}
	
	public boolean isNoTouch(CGPoint point) {
		// ���̹��û�г�����Ļ��Χ
		if (point.x-bulletWidth/2 >= 0 && point.x+bulletWidth/2 <= winSize.width &&
				point.y-bulletHeight/2 >= 0 && point.y+bulletHeight/2 <= winSize.height ) {
			return true;
		}
		
		return false;
	}
	
	// �ж��ӵ��ľ����Ƿ����������ľ���
	public boolean isTouchOther() {
		

		return false;
	}

}








