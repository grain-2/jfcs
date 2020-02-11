package cn.edu.jfcs.sys;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class CacheImage {
	private final Map<String, Image> imageMap = new HashMap<String, Image>();

	private static CacheImage INSTANCE;

	private CacheImage() {
	}

	// 单例模式，获得CacheImage实例
	public static CacheImage getINSTANCE() {
		if (INSTANCE == null)
			INSTANCE = new CacheImage();
		return INSTANCE;
	}

	// 获得图像
	public Image getImage(String applicationID,String imageName) {
		if (imageName == null)
			return null;
		Image image = (Image) imageMap.get(imageName);
		if (image == null) {
			image =AbstractUIPlugin.imageDescriptorFromPlugin(
					applicationID,imageName).createImage();
			imageMap.put(imageName, image);
		}
		return image;
	}

	// 释放图像资源
	public void dispose() {
		Iterator iterator = imageMap.values().iterator();
		while (iterator.hasNext())
			((Image) iterator.next()).dispose();
		imageMap.clear();
	}
}