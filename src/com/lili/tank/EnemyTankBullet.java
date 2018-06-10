package com.lili.tank;

import org.cocos2d.types.CGRect;

import com.lili.layer.FightLayer;

// ����̹�˵��ӵ�
public class EnemyTankBullet extends Bullet {

	private FightLayer layer;
	public EnemyTankBullet(String filePath, FightLayer layer) {
		super(filePath);
		this.layer = layer;
	}

	@Override
	public boolean isTouchOther() {
		
		// �ҵ�̹��
		MyTank myTank = layer.getMyTank();
		// ����ҵ�̹�˲�Ϊ��
		if (myTank != null) {
			// ���Ѫ����Ϊ0
			if (myTank.getHP() > 0) {
				// �ж��Ƿ�����ҵ�̹��
				if (CGRect.containsPoint(myTank.getBoundingBox(), this.getPosition())) {
					System.out.println("�������ҵ�̹��");
					myTank.setHP(0); // Ѫ��Ϊ0
					return true;
				}
			}
			// �����ͼ�����Ƴ���
			if (myTank.isRemove) {
				layer.setMyTank(null); // �����ҵ�̹��Ϊ��
			}
		}
		
		return false;
	}
}
