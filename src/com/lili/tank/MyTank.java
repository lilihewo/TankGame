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

// 我的坦克类
public class MyTank extends CCSprite {

	private CGSize winSize = CCDirector.sharedDirector().winSize(); // 屏幕宽高
	private float tankWidth; // 坦克的宽度
	private float tankHeight; // 坦克的高度
	private int speed = 2; // 速度
	private float dx; // X偏移量
	private float dy; // Y偏移量
	
	public boolean isRemove = false; // 标识坦克是否从图层移除
	
	// 坦克显示的图层
	private FightLayer layer;
	
	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		HP = hP;
	}

	private int HP = 100; // 血量
	
	public MyTank(String filePath, FightLayer layer) {
		super(filePath);
		
		this.layer = layer;
		this.schedule("tankMove", 0.01f); // 启动我的坦克移动定时器
	}
	
	// 移除自己
	public void removeMe() {
		this.removeSelf(); // 移除自己
		
		// 显示玩家输了界面
		// ---转换到场景
		// 我考虑到可能同时被击中的情况，但是还没有做出来
		CCScene scene = CCScene.node();
		scene.addChild(new LuckyLayer("You lose!"));
		CCFadeTransition transition = CCFadeTransition.transition(1, scene);
		CCDirector.sharedDirector().replaceScene(transition);
		
		isRemove = true;
	}

	// 坦克移动的定时器
	public void tankMove(float f) {
		// 如果没有血量了
		if (this.getHP() <= 0) {
			// 停止定时器
			this.unschedule("tankMove");
			System.out.println("停止tankMove定时器");
			
			// 播放声音
			layer.engine.playSound(CCDirector.theApp, R.raw.explosion3, false);
			// 播放动画
			CCAnimate animate = (CCAnimate) MyAnimation.animate("bmob/bz%02d.png", 3, false);
			CCSequence sequence = CCSequence.actions(animate, 
					CCCallFunc.action(this, "removeMe"));
			this.runAction(sequence);
		} else { // 如果还有血量
			// 如果要移动
			if (layer.isMove) {
				tankWidth = this.getBoundingBox().size.width; // 我的坦克的宽度
				tankHeight = this.getBoundingBox().size.height; // 我的坦克的高度
				
				// 得到角度数，例如是90，而不是90°
				double rotation = (int) this.getRotation();
				// 得到角度
				double radians = Math.toRadians(rotation);
				// 计算正弦值
				double sin = Math.sin(radians);
				// 计算余弦值
				double cos = Math.cos(radians);
				// 计算偏移量
				dx = (float) (speed * cos);
				dy = (float) (speed * sin);
				
				// 坦克原来的点
				CGPoint point = this.getPosition();
				// 将要移动的点
				CGPoint movePoint = ccp(point.x - dx, point.y + dy);
				
				if (isOnScreen(movePoint)) {
					this.setPosition(movePoint);
				}
			}

		}
	}
	
	// 判断是否没有撞到屏幕边缘（存在逻辑上的问题）
	public boolean isNoTouch(CGPoint point) {
		// 如果坦克没有超出屏幕范围
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
	
	// 发射子弹的函数
	public void fire() {
		// 创建子弹
//		Bullet bullet = new Bullet("bullet.png");
		MyBullet bullet = new MyBullet("bullet.png",layer);
		bullet.setPosition(this.getPosition()); // 设置子弹的位置为坦克的位置
		bullet.setRotation(this.getRotation()); // 设置子弹的角度为坦克的角度
		bullet.setScale(0.5f);
		bullet.runAction(CCCallFunc.action(bullet, "move"));
		layer.addChild(bullet); // 将子弹添加到显示坦克的图层
		
		// 播放发射的音乐
		layer.engine.playSound(CCDirector.theApp, R.raw.shoot, false);
	}
	
	
	
}


