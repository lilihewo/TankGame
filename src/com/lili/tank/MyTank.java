package com.lili.tank;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import com.lili.helper.MyAnimation;
import com.lili.layer.FightLayer;
import com.lili.layer.LuckyLayer;
import com.lili.tankgame.R;

// �ҵ�̹����
public class MyTank extends CCSprite {

	private CGSize winSize = CCDirector.sharedDirector().winSize(); // ��Ļ���
	private float tankWidth; // ̹�˵Ŀ��
	private float tankHeight; // ̹�˵ĸ߶�
	private int speed = 2; // �ٶ�
	private float dx; // Xƫ����
	private float dy; // Yƫ����
	
	public boolean isRemove = false; // ��ʶ̹���Ƿ��ͼ���Ƴ�
	
	// ̹����ʾ��ͼ��
	private FightLayer layer;
	
	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		HP = hP;
	}

	private int HP = 100; // Ѫ��
	
	public MyTank(String filePath, FightLayer layer) {
		super(filePath);
		
		this.layer = layer;
		this.schedule("tankMove", 0.01f); // �����ҵ�̹���ƶ���ʱ��
	}
	
	// �Ƴ��Լ�
	public void removeMe() {
		this.removeSelf(); // �Ƴ��Լ�
		
		// ��ʾ������˽���
		// ---ת��������
		// �ҿ��ǵ�����ͬʱ�����е���������ǻ�û��������
		CCScene scene = CCScene.node();
		scene.addChild(new LuckyLayer("You lose!"));
		CCFadeTransition transition = CCFadeTransition.transition(1, scene);
		CCDirector.sharedDirector().replaceScene(transition);
		
		isRemove = true;
	}

	// ̹���ƶ��Ķ�ʱ��
	public void tankMove(float f) {
		// ���û��Ѫ����
		if (this.getHP() <= 0) {
			// ֹͣ��ʱ��
			this.unschedule("tankMove");
			System.out.println("ֹͣtankMove��ʱ��");
			
			// ��������
			layer.engine.playSound(CCDirector.theApp, R.raw.explosion3, false);
			// ���Ŷ���
			CCAnimate animate = (CCAnimate) MyAnimation.animate("bmob/bz%02d.png", 3, false);
			CCSequence sequence = CCSequence.actions(animate, 
					CCCallFunc.action(this, "removeMe"));
			this.runAction(sequence);
		} else { // �������Ѫ��
			// ���Ҫ�ƶ�
			if (layer.isMove) {
				tankWidth = this.getBoundingBox().size.width; // �ҵ�̹�˵Ŀ��
				tankHeight = this.getBoundingBox().size.height; // �ҵ�̹�˵ĸ߶�
				
				// �õ��Ƕ�����������90��������90��
				double rotation = (int) this.getRotation();
				// �õ��Ƕ�
				double radians = Math.toRadians(rotation);
				// ��������ֵ
				double sin = Math.sin(radians);
				// ��������ֵ
				double cos = Math.cos(radians);
				// ����ƫ����
				dx = (float) (speed * cos);
				dy = (float) (speed * sin);
				
				// ̹��ԭ���ĵ�
				CGPoint point = this.getPosition();
				// ��Ҫ�ƶ��ĵ�
				CGPoint movePoint = ccp(point.x - dx, point.y + dy);
				
				if (isOnScreen(movePoint)) {
					this.setPosition(movePoint);
				}
			}

		}
	}
	
	// �ж��Ƿ�û��ײ����Ļ��Ե�������߼��ϵ����⣩
	public boolean isNoTouch(CGPoint point) {
		// ���̹��û�г�����Ļ��Χ
		if (point.x-tankWidth/2 >= 0 && point.x+tankWidth/2 <= winSize.width &&
				point.y-tankHeight/2 >= 0 && point.y+tankHeight/2 <= winSize.height ) {
			return true;
		}

		return false;
	}
	
	private boolean isOnScreen(CGPoint movePoint) {
		if (movePoint.x >= 0 && movePoint.x <= winSize.width &&
				movePoint.y >= 0 && movePoint.y <= winSize.height) {
			return true;
		}
		
		return false;
	}
	
	// �����ӵ��ĺ���
	public void fire() {
		// �����ӵ�
//		Bullet bullet = new Bullet("bullet.png");
		MyBullet bullet = new MyBullet("bullet.png",layer);
		bullet.setPosition(this.getPosition()); // �����ӵ���λ��Ϊ̹�˵�λ��
		bullet.setRotation(this.getRotation()); // �����ӵ��ĽǶ�Ϊ̹�˵ĽǶ�
		bullet.setScale(0.5f);
		bullet.runAction(CCCallFunc.action(bullet, "move"));
		layer.addChild(bullet); // ���ӵ���ӵ���ʾ̹�˵�ͼ��
		
		// ���ŷ��������
		layer.engine.playSound(CCDirector.theApp, R.raw.shoot, false);
	}
	
	
	
}


