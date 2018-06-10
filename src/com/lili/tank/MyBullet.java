package com.lili.tank;

import org.cocos2d.types.CGRect;

import com.lili.layer.FightLayer;

// 我的坦克的子弹
public class MyBullet extends Bullet {

	private FightLayer layer;
	public MyBullet(String filePath, FightLayer layer) {
		super(filePath);
		this.layer = layer;
	}
	
	@Override
	public boolean isTouchOther() {
		EnemyTank enemyTank = layer.getEnemyTank();
		// 如果敌人的坦克不为空
		if (enemyTank != null) {
			// 如果血量不为0
			if (enemyTank.getHP() > 0) {
				// 判断是否击中敌人的坦克
				if (CGRect.containsPoint(enemyTank.getBoundingBox(), this.getPosition())) {
					System.out.println("击中敌人坦克");
					enemyTank.setHP(0); // 血量为0
					return true;
				}
			}
			// 如果从图层中移除了
			if (enemyTank.isRemove) {
				layer.setEnemyTank(null); // 设置敌人的坦克为空
			}
		}
		
		return false;
	}
	
	
}









