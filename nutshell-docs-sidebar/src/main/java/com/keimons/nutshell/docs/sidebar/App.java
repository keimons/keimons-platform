package com.keimons.nutshell.docs.sidebar;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成侧边栏工具
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class App {

	/**
	 * 存储所有的节点
	 */
	public static Node node = new Node("start", 0);

	public static void main(String[] args) throws IOException {
		// 加载目录文件
		File file = new File(App.class.getResource("/sidebar.md").getFile());
		BufferedReader br = new BufferedReader(new FileReader(file));
		String title;
		while ((title = br.readLine()) != null) {
			int startIndex = title.indexOf("(");
			int endIndex = title.indexOf(")");
			if (startIndex > 0 && endIndex > 0) {
				String link = title.substring(startIndex + 1, endIndex);
				String[] context = link.split("/");
				Node current = node;
				for (int i = 0; i < context.length; i++) {
					int level = i + 1;
					String parent = context[i];
					List<String> path = current.path;
					current = current.next.computeIfAbsent(parent, v -> new Node(path, parent, level));
					if (level == context.length) {
						current.context = title;
					}
				}
			}
		}
		build(node);
	}

	/**
	 * 构建目录
	 *
	 * @param current 当前节点
	 */
	public static void build(Node current) {
		for (Map.Entry<String, Node> entry : current.next.entrySet()) {
			if (entry.getValue().next.size() == 0) {
				continue;
			}
			List<String> result = new ArrayList<>();
			scanner(result, node, entry.getValue().path, entry.getValue().level + 1);
			if (result.size() > 0) {
				write(entry.getValue().path, result);
			}
			build(entry.getValue());
		}
	}

	/**
	 * 按照层级和路径构造侧边栏
	 *
	 * @param result 构造结果
	 * @param node   当前几点
	 * @param path   路径
	 * @param level  最深层级
	 */
	public static void scanner(List<String> result, Node node, List<String> path, int level) {
		if (node.level <= 2 || isSamePath(node.path, path)) {
			if (node.level > 0) {
				result.add(node.context);
				result.add(System.getProperty("line.separator"));
				result.add(System.getProperty("line.separator"));
			}
		}
		if (node.next.size() == 0 || node.level >= level) {
			return;
		}
		for (Map.Entry<String, Node> entry : node.next.entrySet()) {
			scanner(result, entry.getValue(), path, level);
		}
	}

	/**
	 * 采用森林的数据结构
	 */
	public static class Node {

		/**
		 * 达到当前节点的路径
		 */
		List<String> path = new ArrayList<>();

		/**
		 * 节点中存储的内容
		 */
		String context;

		/**
		 * 当前节点所位于的层级
		 */
		int level;

		/**
		 * 记录当前节点的子节点
		 */
		Map<String, Node> next = new LinkedHashMap<>();

		/**
		 * 构造顶级节点
		 *
		 * @param current 顶级节点
		 * @param level   等级层级
		 */
		public Node(String current, int level) {
			this.path.add(current);
			this.level = level;
		}

		/**
		 * 根据父节点构造节点
		 *
		 * @param parent  父节点路径
		 * @param current 当前节点
		 * @param level   当前节点层级
		 */
		public Node(List<String> parent, String current, int level) {
			this.path.addAll(parent);
			this.path.add(current);
			this.level = level;
		}
	}

	public static boolean isSamePath(List<String> list1, List<String> list2) {
		if (list1.size() > list2.size() + 1) {
			return false;
		}
		for (int i = 0; i < list1.size() && i < list2.size(); i++) {
			if (!list1.get(i).equals(list2.get(i))) {
				return false;
			}
		}
		return true;
	}

	public static void write(List<String> path, List<String> context) {
		try {
			String dir = System.getProperty("user.dir");
			String separator = System.getProperty("file.separator");
			StringBuilder sb = new StringBuilder();
			sb.append(dir);
			if (!dir.endsWith(separator)) {
				sb.append(separator);
			}
			sb.append("docs").append(separator);
			for (int i = 1; i < path.size(); i++) {
				sb.append(path.get(i));
				sb.append(separator);
			}
			sb.append("_sidebar.md");

			System.out.println(sb.toString());

			File file = new File(sb.toString());

			//if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			//true = append file
			FileWriter writer = new FileWriter(file);
			for (int i = 0; i < context.size() - 2; i++) {
				writer.write(context.get(i));
				System.out.println(context.get(i));
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}