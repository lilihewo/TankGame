package com.lili.helper;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

/**
 * 菜单助手类
 * @author shiyang
 *
 */
public class MenuHelper {
	
	/**
	 * 得到菜单按钮函数
	 * @param normal 菜单按钮正常显示时的图片路径
	 * @param selected 菜单按钮选中时的图片路径
	 * @param point 菜单按钮的位置
	 * @param selector 菜单按钮的回调函数
	 *	@return 菜单
	 */
	public CCMenu getMenu(String normal, String selected, CGPoint point, String selector, CCLayer layer) {
		// 创建菜单对象
		CCMenu menu = CCMenu.menu();
		menu.setAnchorPoint(0, 0); // 重要

		// 正常显示的精灵
		CCSprite normalSprite = CCSprite.sprite(normal);
		// 选中时的精灵
		CCSprite selectedSprite = CCSprite.sprite(selected);
		selectedSprite.setScale(1.2f);

		// 能有一个中心放大的效果
		float x = (normalSprite.getBoundingBox().size.width - selectedSprite.getBoundingBox().size.width)/2;
		float y = (normalSprite.getBoundingBox().size.height - selectedSprite.getBoundingBox().size.height)/2;
		selectedSprite.setPosition(x, y);

		CCMenuItemSprite menuItemSprite = CCMenuItemSprite.item(normalSprite,
				selectedSprite, layer, selector);
		menu.addChild(menuItemSprite);

		// 设置菜单的位置
		menu.setPosition(point);
		
		return menu;
	}
	
	public CCMenu getMenu(String normal, String selected, String selector, CCLayer layer) {
		CGPoint point = CGPoint.ccp(440, 30);
		
		return getMenu(normal, selected, point, selector, layer);
	}
	
	public CCMenu getMenu(String image, String selector, CCLayer layer) {
		
		return getMenu(image, image, selector, layer);
	}
	

}
