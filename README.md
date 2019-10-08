# keimons-platform
## 系统底层架构

&emsp;&emsp;一个模块分为8部分，分别是：Manager、Service、Event、Processor、PlayerData、GameData、CronJob、Logger，将这8个模块接入到系统中即可完成模块安装。

&emsp;&emsp;模块标识注解：@AModule。AModale是一个包注解，位于package-info.java中，标注一个包为一个模块，自动扫描该包下的所有归属于一个模块的子系统。按照模块指定的加载顺序进行加载。

## 模块接入

### Manager

Manager负责管理静态数据，新建类实现IManager接口，并标注@AManager注解，即可完成Manager接入。
其中，AManager中的Priority为启动优先级，系统会按照模块的启动顺序进行启动。

### Service逻辑业务接入

Service负责业务逻辑相关处理，新建类实现IService接口，并标注@AService注解，即可完成Service接入。
其中，AService中的Priority为启动优先级，系统会按照模块的启动顺序进行启动。

### Event事件系统接入

Event事件系统负责处理玩家抛出的事件，目前，异步系统是完全异步的，并且事件处理是依附于IService存在的。Event的接入方法：新建类实现IService接口和IEventHandler接口，重写注册事件和事件处理方法，即可完成Event系统接入。

### Processor消息处理接入

新建类继承BaseProcessor类，标注@AProcessor接口，即可完成Processor接入。

### PlayerData数据接入

新建类继承BasePlayerData类，重写getModuleName()方法，即可完成PlayerData接入。

### GameData数据接入

新建类继承BaseGameData类，重写getModuleName()方法，即可完成GameData接入。

### 定时任务接入

新建类继承BaseJob类，即可完成CronJob接入。

### Logger日志系统接入

初始化时指定日志名称即可接入。
