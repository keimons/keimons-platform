# 游戏数据

我们设计了大量的空接口，用于定义一个数据它应该是什么样子。

按照数据类型，可以将数据划分为两类，玩家数据和公共数据（例如：公会数据等），
分别对应了系统中的`IPlayerData`和`ISystemData`两个接口。
每一类数据，又可以划分为唯一数据（例如：成就系统、任务系统、家园系统等）和重复数据（例如：卡牌，装备等），
分别对应了`IRepeatedData`和`ISingularData`两个接口。所以，共计设计4个数据模块：
玩家唯一数据，玩家重复数据，公共唯一数据，公共重复数据。

我们尝试将数据存入一个`Map`的映射中，`key`是模块的名称，`value`是模块的数据，这样更有利于控制前期流失的低等级玩家大小。
例如：某一个功能65级解锁，那么在65级以前，实际上玩家是不需要初始化这个功能相关的数据的，我们可以通过这种方法，
来避免提前创建出来这个对象，造成内存以及存储空间的浪费。

## 玩家数据

注解`@APlayerData`表明最是玩家的数据，`moduleName`是用来存储这个模块的名字。玩家数据的两个主要接口`IRepeatedPlayerData`和`ISingularPlayerData`，
所有的玩家数据都应该是实现自这两个接口的。

### 玩家唯一数据

![player-singular-data](@/design/player-singular-data.png)

数据示例：

```
package com.keimons.platform;

import com.alibaba.fastjson.JSONObject;
import com.keimons.platform.keimons.DefaultPlayer;
import com.keimons.platform.module.ISingularPlayerData;
import com.keimons.platform.player.APlayerData;
import com.keimons.platform.player.IPlayer;

import java.util.HashSet;
import java.util.Set;

/**
 * 任务系统
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
@APlayerData(moduleName = "task")
public class Task implements ISingularPlayerData {

	/**
	 * 已经完成的任务
	 */
	private Set<String> finishTasks = new HashSet<>();

	@Override
	public <T extends IPlayer<?>> void init(T player) {
		DefaultPlayer dp = (DefaultPlayer) player;
		System.out.println("玩家ID：" + dp.getIdentifier());
	}

	public Set<String> getFinishTasks() {
		return finishTasks;
	}

	public void setFinishTasks(Set<String> finishTasks) {
		this.finishTasks = finishTasks;
	}

	@Override
	public String toString() {
		return "任务模块，已完成任务：" + JSONObject.toJSONString(finishTasks);
	}
}
```

### 玩家重复数据

![player-repeated-data](@/design/player-repeated-data.png)

数据示例：

```
package com.keimons.platform;

import com.keimons.platform.module.IRepeatedPlayerData;
import com.keimons.platform.player.APlayerData;

/**
 * 测试装备
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
@APlayerData(moduleName = "equip")
public class Equip implements IRepeatedPlayerData<Integer> {

	/**
	 * 装备唯一ID
	 */
	private int dataId;

	/**
	 * 装备表ID
	 */
	private String itemId;

	/**
	 * 装备等级
	 */
	private int level;

	/**
	 * 装备星级
	 */
	private int star;

	@Override
	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(int dataId) {
		this.dataId = dataId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	@Override
	public String toString() {
		return "装备ID：" + dataId +
				"，物品ID：" + itemId +
				"，等级：" + level +
				"，星级：" + star
				;
	}
}
```

## 公共数据

注解`@ASystemData`表明最是公共的数据。公共数据的两个主要接口`IRepeatedSystemData`和`ISingularSystemData`，
所有的公共数据都应该是实现自这两个接口的。

### 公共唯一数据

![system-singular-data](@/design/system-singular-data.png)

### 公共重复数据

![system-repeated-data](@/design/system-repeated-data.png)

# 游戏模块

顶层接口`IModule`定义了这是一个`数据容器`，
将`数据容器`分为：存放单数数据的`ISingularModule`和存放复数数据的`IRepeatedModule`。
玩家数据和公共数据也是存放于`数据容器`中的，对于数据的管理，也是依赖于`数据容器`的，
而`数据容器`也是数据存储的最小单元。我们允许为每个`数据容器`编写不同的存储方式、位置。

![module](@/design/module.png)