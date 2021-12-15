# nutshell

## 线程模型

### 多级线程模型

&emsp;&emsp;允许开发人员自定义多级线程，用于区分处理耗时差异较大的操作。例如：涉及IO等操作，或者有较强同步需要的操作。

### 可以指定线程

&emsp;&emsp;允许开发人员指定执行业务线程，用于规避一些不必要的锁。例如：可以将同一个公会的线程，指定到同一个线程来执行，公会内部业务无需加锁。同理，同一小队的业务也可以由同一个线程来操作。

### 单线程模型

&emsp;&emsp;允许开发人员自定义单线程，用于处理比较适合单线程的业务。例如：可以将申请公会，退出公会，一键申请等业务，交由一个单线程执行。

### 玩家自身同步

&emsp;&emsp;每一个Session中维护一个消息队列，当消息执行完之后，继续选择消息执行。同时，多级线程池共享玩家的私有消息队列。

## 玩家数据

&emsp;&emsp;将玩家数据交由系统底层进行统一管理。

### 单数据模块

&emsp;&emsp;允许开发人员自定义单个的数据模块。例如：成就系统，任务系统等。

### 多数据模块

&emsp;&emsp;允许开发人员自定义多个数据组成的模块。例如：装备、卡牌等。

### 半加载模式

&emsp;&emsp;允许数据的半加载。例如：仅加载玩家的家园、好友、Mini数据等。

### 只读加载模式

&emsp;&emsp;将玩家的数据按照压缩的形式，只读加载到内存中，当使用数据时，对数据进行解压，使用完后，将数据存入软引用，如果内存不足则直接释放数据。例如：临时加载玩家战斗数据等。