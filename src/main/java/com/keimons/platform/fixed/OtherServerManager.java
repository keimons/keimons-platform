package com.keimons.platform.fixed;

import com.keimons.platform.log.ILogger;
import com.keimons.platform.log.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class OtherServerManager {

	private static final ILogger logger = LoggerFactory.getLogger(OtherServerManager.class);

	static {
		System.out.println(OtherServerManager.class.getName());
	}

	private Map<String, ServerClassLoader> pluginMap = new HashMap<>();
	private static String PackageName = "com.keimons.platform";

	public OtherServerManager() {

	}

	public void doSome(String pluginName) {
		try {
			Class<?> forName = Class.forName(PackageName, true, getLoader(pluginName));
			Class<?>[] interfaces = forName.getInterfaces();//获得Dog所实现的所有接口
			for (Class<?> clazz : interfaces) {//打印
				System.out.println("实现接口：" + clazz);
			}
			System.out.println(OtherServer.class.equals(interfaces[0]));
			OtherServer ins = (OtherServer) forName.getDeclaredConstructor().newInstance();
			ins.start();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void addLoader(String pluginName, ServerClassLoader loader) {
		this.pluginMap.put(pluginName, loader);
	}

	private ServerClassLoader getLoader(String pluginName) {
		return this.pluginMap.get(pluginName);
	}

	public void loadPlugin(String pluginName) {
		this.pluginMap.remove(pluginName);
		ServerClassLoader loader = new ServerClassLoader();
		String pluginurl = "jar:file:/D:/ServerPacket/" + pluginName + ".jar!/"; // CenterServer_jar
		URL url = null;
		try {
			url = new URL(pluginurl);
		} catch (MalformedURLException e) {
			logger.error(e);
		}
		loader.addURLFile(url);
		addLoader(pluginName, loader);
		System.out.println("load " + pluginName + "  success");
	}

	public void unloadPlugin(String pluginName) {
		this.pluginMap.get(pluginName).unloadJarFiles();
		this.pluginMap.remove(pluginName);
	}

	public static void main(String[] arg0) {
		OtherServerManager server = new OtherServerManager();
		System.out.println(OtherServer.class.toString());
		server.loadPlugin("CenterServer");
		server.doSome("CenterServer");
	}
}