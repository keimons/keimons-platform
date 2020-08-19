# 游戏数据

我们设计了大量的空接口，用于定义一个数据它应该是什么样子。

按照数据类型，可以将数据划分为两类，玩家数据和公共数据（例如：公会数据等），
分别对应了系统中的`IPlayerData`和`ISystemData`两个接口。
每一类数据，又可以划分为唯一数据（例如：成就系统、任务系统、家园系统等）和重复数据（例如：卡牌，装备等），
分别对应了`IRepeatedData`和`ISingularData`两个接口。所以，共计设计4个数据模块：
玩家唯一数据，玩家重复数据，公共唯一数据，公共重复数据。

## 玩家数据

### 玩家唯一数据

![player-singular-data](@/design/player-singular-data.png)

### 玩家重复数据

![player-repeated-data](@/design/player-repeated-data.png)

## 公共数据

### 公共唯一数据

![system-singular-data](@/design/system-singular-data.png)

### 公共重复数据

![system-repeated-data](@/design/system-repeated-data.png)

# 游戏模块

游戏数据是存放于游戏模块中。对应以上四种数据，设计了4个存储模块。对于数据的管理，是依赖于模块的，
同时，模块也是数据存储的最小单元。允许每个模块自定义存储方式、位置。
