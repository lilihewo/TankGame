package com.lili.helper;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

/**
 * �˵�������
 * @author shiyang
 *
 */
public class MenuHelper {
	
	/**
	 * �õ��˵���ť����
	 * @param normal �˵���ť������ʾʱ��ͼƬ·��
	 * @param selected �˵���ťѡ��ʱ��ͼƬ·��
	 * @param point �˵���ť��λ��
	 * @param selector �˵���ť�Ļص�����
	 *	@return �˵�
	 */
	public CCMenu getMenu(String normal, String selected, CGPoint point, String selector, CCLayer layer) {
		// �����˵�����
		CCMenu menu = CCMenu.menu();
		menu.setAnchorPoint(0, 0); // ��Ҫ

		// ������ʾ�ľ���
		CCSprite normalSprite = CCSprite.sprite(normal);
		// ѡ��ʱ�ľ���
		CCSprite selectedSprite = CCSprite.sprite(selected);
		selectedSprite.setScale(1.2f);

		// ����һ�����ķŴ��Ч��
		float x = (normalSprite.getBoundingBox().size.width - selectedSprite.getBoundingBox().size.width)/2;
		float y = (normalSprite.getBoundingBox().size.height - selectedSprite.getBoundingBox().size.height)/2;
		selectedSprite.setPosition(x, y);

		CCMenuItemSprite menuItemSprite = CCMenuItemSprite.item(normalSprite,
				selectedSprite, layer, selector);
		menu.addChild(menuItemSprite);

		// ���ò˵���λ��
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
