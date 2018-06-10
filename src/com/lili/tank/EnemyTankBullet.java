package com.lili.tank;

import org.cocos2d.types.CGRect;

import com.lili.layer.FightLayer;

// 敌人坦克的子弹
public class EnemyTankBullet extends Bullet {

	private FightLayer layer;
	public EnemyTankBullet(String filePath, FightLayer layer) {
		super(filePath);
		this.layer = layer;
	}

	@Override
	public boolean isTouchOther() {
		
		// 我的坦克
		MyTank myTank = layer.getMyTank();
		// 如果我的坦克不为空
		if (myTank != null) {
			// 如果血量不为0
			if (myTank.getHP() > 0) {
				// 判断是否击中我的坦克
				if (CGRect.containsPoint(myTank.getBoundingBox(), this.getPosition())) {
					System.out.println("击中了我的坦克");
					myTank.setHP(0); // 血量为0
					return true;
				}
			}
			// 如果从图层中移除了
			if (myTank.isRemove) {
				layer.setMyTank(null); // 设置我的坦克为空
			}
		}
		
		return false;
	}
}
