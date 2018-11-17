package com.abreaking.addon.helloworld;

import com.abreaking.menu.MenuGroup;
import com.abreaking.menu.MenuItem;
import com.abreaking.menu.MenuManager;
import com.abreaking.message.Message;
import com.abreaking.message.MessageListener;
import com.abreaking.message.annotation.Listener;

@Listener(action = MenuManager.ACTION_INIT_MENU, async = false)
public class HelloMessage implements MessageListener {

	@Override
	public void onMessage(Message message) {

		MenuManager manager = message.getData();

		MenuGroup menuGroup = new MenuGroup("hello-test", null, "插件测试");

		MenuItem item = new MenuItem("test", "#", "插件测试");
		menuGroup.addMenuItem(item);

		manager.addMenuGroup(menuGroup);

	}

}
