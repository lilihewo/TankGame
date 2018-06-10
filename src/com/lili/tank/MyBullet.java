package com.lili.tank;

import org.cocos2d.types.CGRect;

import com.lili.layer.FightLayer;

// �ҵ�̹�˵��ӵ�
public class MyBullet extends Bullet {

	private FightLayer layer;
	public MyBullet(String filePath, FightLayer layer) {
		super(filePath);
		this.layer = layer;
	}
	
	@Override
	public boolean isTouchOther() {
		EnemyTank enemyTank = layer.getEnemyTank();
		// ������˵�̹�˲�Ϊ��
		if (enemyTank != null) {
			// ���Ѫ����Ϊ0
			if (enemyTank.getHP() > 0) {
				// �ж��Ƿ���е��˵�̹��
				if (CGRect.containsPoint(enemyTank.getBoundingBox(), this.getPosition())) {
					System.out.println("���е���̹��");
					enemyTank.setHP(0); // Ѫ��Ϊ0
					return true;
				}
			}
			// �����ͼ�����Ƴ���
			if (enemyTank.isRemove) {
				layer.setEnemyTank(null); // ���õ��˵�̹��Ϊ��
			}
		}
		
		return false;
	}
	
	
}









