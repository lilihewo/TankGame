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

// 敌人坦克
public class EnemyTank extends CCSprite {

	private FightLayer layer;
	public boolean isRemove = false; // 标识敌人坦克是否从图层中移除
	
	private int HP = 100; // 血量

	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		HP = hP;
	}
	
	// 屏幕宽高
	private CGSize winSize = CCDirector.sharedDirector().winSize();
	private int enemyTankSpeed = 2; // 速度
	private float dx; // X偏移量
	private float dy; // Y偏移量
	
	private float enemyTankWidth;
	private float enemyTankHeight;

	public EnemyTank(String filePath, FightLayer layer) {
		super(filePath);
		
		this.layer = layer;
		// 启动发射子弹的定时器
		this.schedule("toFire", 2.3f);
		// 启动改变角度的定时器
		this.schedule("changeRotation", 1.7f);
		// 启动移动的定时器
		this.schedule("enemyTankMove", 0.003f);
		// 判断敌人坦克是否要销毁的定时器
		this.schedule("destroy", 0.1f);
	}
	
	// 改变角度的定时器
	public void changeRotation(float f) {
		// 如果还有血量，继续转动
		int rotation = (int) (Math.random()*10 * 36);
		int one = (int) (Math.random()*10);
		int two = (int) (Math.random()*10);
		// 增加一个50%的概率
		if (one >= two) {
			this.setRotation(rotation);
		}
	}
	
	// 发射子弹的定时器
	public void toFire(float f) {
		fire(); // 发射子弹
	}
	
	// 发射子弹的函数
	public void fire() {
		// 创建子弹
//		Bullet bullet = new Bullet("bullet.png");
		EnemyTankBullet bullet = new EnemyTankBullet("bullet.png", layer);
		bullet.setPosition(this.getPosition()); // 设置子弹的位置为坦克的位置
		bullet.setRotation(this.getRotation()); // 设置子弹的角度为坦克的角度
		bullet.setScale(0.5f);
		bullet.runAction(CCCallFunc.action(bullet, "move"));
		layer.addChild(bullet); // 将子弹添加到显示坦克的图层
		
		// 播放发射的音乐
		layer.engine.playSound(CCDirector.theApp, R.raw.shoot, false);
	}
	
	// 敌人坦克移动的定时器
	public void enemyTankMove(float f) {
		move();
	}
	
	// 移动
	private void move() {
		enemyTankWidth = this.getBoundingBox().size.width;
		enemyTankHeight = this.getBoundingBox().size.height;
		double rotation = this.getRotation();
		double radians = Math.toRadians(rotation); // 得到角度
		double sin = Math.sin(radians); // 计算正弦值
		double cos = Math.cos(radians); // 计算余弦值
		// 计算偏移量
		dx = (float) (enemyTankSpeed * cos);
		dy = (float) (enemyTankSpeed * sin);

		CGPoint point = this.getPosition();
		// 如果没有碰到屏幕边缘
		if (isNoTouch(point)) {
			// 可以移动
			this.setPosition(point.x - dx, point.y + dy);
			// 得到移动后的点
			CGPoint movePoint = this.getPosition();
			// 如果移动后的点碰到屏幕边缘
			if (!isNoTouch(movePoint)) {
				// 恢复到原来的点
				this.setPosition(point);
				// 反向
				float currentRotation = this.getRotation();
				if (currentRotation > 0 && currentRotation < 180) {
					this.setRotation(currentRotation+180);
				} else {
					this.setRotation(currentRotation-180);
				}
			}
		}
	}
	
	// 决定是否销毁敌人坦克的定时器
	public void destroy(float f) {
		// 如果血量<=0
		if (this.getHP() <= 0) {
			// 停止所有定时器
			this.unscheduleAllSelectors();
			// 播放声音
			layer.engine.playSound(CCDirector.theApp, R.raw.explosion3, false);
			// 动画
			CCAnimate animate = (CCAnimate) MyAnimation.animate("bmob/bz%02d.png", 3, false);
			CCSequence sequence = CCSequence.actions(animate, 
					CCCallFunc.action(this, "removeMe"));
			this.runAction(sequence);
		}
	}
	
	// 移除自己
	public void removeMe() {
		this.removeSelf(); // 移除自己
		
		// 显示玩家赢了的界面
		CCScene scene = CCScene.node();
		scene.addChild(new LuckyLayer("You win!"));
		CCFadeTransition transition = CCFadeTransition.transition(1, scene);
		CCDirector.sharedDirector().replaceScene(transition);
		
		isRemove = true;
	}
	
	
	public boolean isNoTouch(CGPoint point) {
		// 如果坦克没有超出屏幕范围
		if (point.x-enemyTankWidth/2 >= 0 && point.x+enemyTankWidth/2 <= winSize.width &&
				point.y-enemyTankHeight/2 >= 0 && point.y+enemyTankHeight/2 <= winSize.height ) {
			return true;
		}
		
		return false;
	}

}














