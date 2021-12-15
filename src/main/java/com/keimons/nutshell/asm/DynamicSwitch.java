package com.keimons.nutshell.asm;

import com.keimons.nutshell.unit.UnsafeUtil;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 动态switch功能
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class DynamicSwitch {

	public static final String DEFAULT_SUPER = "java/lang/Object";

	public static final String METHOD_NAME = "valueOf";

	public static final String DEFAULT_SIGN = null;

	public static final String[] DEFAULT_INTERFACES = null;

	public static final String METHOD_PARAMS = "(Ljava/lang/String;)";

	public static final String STRING_CLASS = "java/lang/String";

	/**
	 * public static final
	 */
	public static final int ACCESS_PSF = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL;

	/**
	 * 从枚举中查找指定的枚举值
	 *
	 * @param clazz 要查找的枚举
	 * @param name  枚举名称
	 * @param <T>   枚举类型
	 * @return 枚举值
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<?>> T valueOf(Class<T> clazz, String name) {
		// 生成二进制字节码
		try {
			MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(clazz, MethodHandles.lookup());
			MethodHandle getter = lookup.findStaticGetter(clazz, "$VALUES", Array.newInstance(clazz, 0).getClass());
			@SuppressWarnings("unchecked")
			T[] values = (T[]) getter.invoke();

			byte[] bytes = DynamicSwitch.createClass(clazz);
			// 使用自定义的ClassLoader
			MyClassLoader cl = new MyClassLoader();
			// 加载我们生成的 HelloWorld 类
			Class<?> newClass = cl.defineClass(clazz.getName() + "$DynamicSwitch$" + values.length, bytes);
			Unsafe unsafe = UnsafeUtil.getUnsafe();
			Field cache = Class.class.getDeclaredField("module");
			long address = unsafe.objectFieldOffset(cache);
			unsafe.getAndSetObject(newClass, address, clazz.getModule());

			MethodHandles.Lookup lookup1 = MethodHandles.lookup();
			MethodType methodType = MethodType.methodType(clazz, String.class);
			MethodHandle method = lookup1.findStatic(newClass, "valueOf", methodType);
			return (T) method.invoke(name);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T extends Enum<?>> byte[] createClass(Class<T> enumType) throws Throwable {
		Class<?> clazz = Array.newInstance(enumType, 0).getClass();
		MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(enumType, MethodHandles.lookup());
		MethodHandle getter = lookup.findStaticGetter(enumType, "$VALUES", clazz);
		@SuppressWarnings("unchecked")
		T[] values = (T[]) getter.invoke();
		String className = enumType.getName().replaceAll("\\.", "/");
		System.out.println("原class名：" + className);
		// 生成新类名、方法签名
		String dynamicClazzName = className + "$DynamicSwitch$" + values.length;
		System.out.println("新class名：" + dynamicClazzName);
		String methodDescriptor = METHOD_PARAMS + "L" + className + ";";
		System.out.println("新签名：" + methodDescriptor);
		ClassWriter cw = new ClassWriter(0);
		cw.visit(Opcodes.V11, Opcodes.ACC_PUBLIC, dynamicClazzName, DEFAULT_SIGN, DEFAULT_SUPER, DEFAULT_INTERFACES);
		MethodVisitor mv = cw.visitMethod(ACCESS_PSF, METHOD_NAME, methodDescriptor, null, null);

		// 计算分支数量，因为使用String作为switch的条件，所以实际上是调用字符串的hashCode作为分支。
		// 不同的字符串可能有相同的hashCode。如果hashCode相同，采用顺序查找的方案。
		// Multimap不适用
		HashMap<Integer, Node<T>> cases = new HashMap<>(values.length);
		for (int i = 0; i < values.length; i++) {
			T value = values[i];
			Node<T> node = cases.computeIfAbsent(value.name().hashCode(), v -> new Node<>(value.name().hashCode()));
			System.out.println("name: " + value.name() + ", hashcode: " + value.name().hashCode());
			node.items.add(value);
			node.indexes.add(i);
		}
		List<Node<T>> nodes = cases.values().stream()
				.sorted(Comparator.comparing(Node::getHashcode))
				.collect(Collectors.toList());
		int[] keys = nodes.stream().mapToInt(Node::getHashcode).toArray();
		// 初始化所有分支标签
		Label[] labels1 = new Label[keys.length];
		for (int i = 0; i < keys.length; i++) {
			labels1[i] = new Label();
		}
		// 初始化默认分支标签
		Label table = new Label();

		// 定义标签L0
		Label L0 = new Label();
		mv.visitLabel(L0);
		mv.visitLineNumber(14, L0);
		// 将局部变量表0位置的String压入操作数栈
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		// 将String存入局部变量表1的位置
		mv.visitVarInsn(Opcodes.ASTORE, 1);
		// 将-1压入操作数栈 先初始化一个-1，这个值不是随便初始化的，保证到时候能走到default
		mv.visitInsn(Opcodes.ICONST_M1);
		// 将栈顶的-1存入局部变量表2的位置
		mv.visitVarInsn(Opcodes.ISTORE, 2);
		// 将局部变量表1位置的String压入操作数栈
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		// 调用String.hashCode()方法
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, STRING_CLASS, "hashCode", "()I", false);
		// 调用LookupSwitch方法
		mv.visitLookupSwitchInsn(table, keys, labels1);

		// 初始化标签分支
		for (int i = 0; i < labels1.length; i++) {
			Node<T> node = nodes.get(i);
			Label next = null;
			for (int j = 0; j < node.items.size(); j++) {
				if (j == 0) {
					mv.visitLabel(labels1[i]);
				} else {
					mv.visitLabel(next);
				}
				if (i == 0 && j == 0) {
					mv.visitFrame(Opcodes.F_APPEND, 2, new Object[]{"java/lang/String", Opcodes.INTEGER}, 0, null);
				} else {
					mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
				}
				mv.visitVarInsn(Opcodes.ALOAD, 1);
				mv.visitLdcInsn(node.items.get(j).name());
				mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, STRING_CLASS, "equals", "(Ljava/lang/Object;)Z", false);
				if (j == node.items.size() - 1) {
					// 最后一次直接跳转到结束位置
					mv.visitJumpInsn(Opcodes.IFEQ, table);
				} else {
					// 跳转到下一次
					next = new Label();
					mv.visitJumpInsn(Opcodes.IFEQ, next);
				}
				int opcode = opcode(node.indexes.get(j));
				if (opcode == Opcodes.BIPUSH) {
					mv.visitIntInsn(Opcodes.BIPUSH, node.indexes.get(j));
				} else {
					mv.visitInsn(opcode);
				}
				mv.visitVarInsn(Opcodes.ISTORE, 2);
			}
			if (i < nodes.size() - 1) {
				// Jdk编译优化 最后一个标签，不需要goto，所以，我们也不goto了。
				mv.visitJumpInsn(Opcodes.GOTO, table);
			}
		}

		Label[] labels2 = new Label[values.length];
		for (int i = 0; i < values.length; i++) {
			labels2[i] = new Label();
		}
		// 初始化默认分支标签
		Label fail = new Label();

		mv.visitLabel(table);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitTableSwitchInsn(0, values.length - 1, fail, labels2);

		for (int i = 0; i < labels2.length; i++) {
			mv.visitLabel(labels2[i]);
			mv.visitLineNumber(16 + i * 2, labels2[i]);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			int opcode = opcode(i);
			if (opcode == Opcodes.BIPUSH) {
				mv.visitIntInsn(Opcodes.BIPUSH, i);
			} else {
				mv.visitInsn(opcode);
			}
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, className, "valueOf", "(I)L" + className + ";", false);
			mv.visitInsn(Opcodes.ARETURN);
		}

		mv.visitLabel(fail);
		mv.visitLineNumber(16 + labels2.length * 2, fail);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitInsn(Opcodes.ACONST_NULL);
		mv.visitInsn(Opcodes.ARETURN);

		Label end = new Label();
		mv.visitLabel(end);
		mv.visitLocalVariable("name", "Ljava/lang/String;", null, L0, end, 0);
		mv.visitMaxs(2, 3);

		mv.visitEnd();
		cw.visitEnd();
		return cw.toByteArray();
	}

	private static int opcode(int opcode) {
		switch (opcode) {
			case -1:
				return Opcodes.ICONST_M1;
			case 0:
				return Opcodes.ICONST_0;
			case 1:
				return Opcodes.ICONST_1;
			case 2:
				return Opcodes.ICONST_2;
			case 3:
				return Opcodes.ICONST_3;
			case 4:
				return Opcodes.ICONST_4;
			case 5:
				return Opcodes.ICONST_5;
			default:
				return Opcodes.BIPUSH;
		}
	}

	private static class Node<T> {

		/**
		 * 节点的hashcode值
		 */
		final int hashcode;

		/**
		 * 节点中的元素
		 */
		List<T> items = new ArrayList<>();

		/**
		 * 元素的位置
		 */
		List<Integer> indexes = new ArrayList<>();

		public Node(int hashcode) {
			this.hashcode = hashcode;
		}

		public int getHashcode() {
			return hashcode;
		}

		public List<T> getItems() {
			return items;
		}

		public void setItems(List<T> items) {
			this.items = items;
		}

		public List<Integer> getIndexes() {
			return indexes;
		}

		public void setIndexes(List<Integer> indexes) {
			this.indexes = indexes;
		}
	}

	/**
	 * 自定义ClassLoader以支持加载字节数组形式的字节码
	 *
	 * @author dadiyang
	 */
	public static class MyClassLoader extends ClassLoader {

		public Class<?> defineClass(String name, byte[] b) {
			// ClassLoader是个抽象类，而ClassLoader.defineClass 方法是protected的
			// 所以我们需要定义一个子类将这个方法暴露出来
			return super.defineClass(name, b, 0, b.length);
		}
	}
}