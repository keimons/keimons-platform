# 系统启动流程  <!-- {docsify-ignore} -->

## 配置文件加载  <!-- {docsify-ignore} -->

> 加载游戏配置文件。

## 系统模块安装  <!-- {docsify-ignore} -->

> 安装顺序：

1.控制台重定向

2.日志模块

3.调度模块

4.事件模块

5.消息模块

6.网络模块

## 持久化模块启动  <!-- {docsify-ignore} -->

> 数据库的初始化工作要在应用模块的启动之前。

## 应用模块安装  <!-- {docsify-ignore} -->

> 检查玩家所有模块，将所有模块安装到制定的位置。

查找所有的模块，按照启动顺序，逐个启动。 每个模块分为：Manager、Service&Event、Logger、Processor、Job、PlayerData、GameData八个部分。
将这七个部分安装在系统中，并对所有模块进行初始化。

## 网络模块启动  <!-- {docsify-ignore} -->

> 监听端口号，启动网络模块

## 系统启动完成  <!-- {docsify-ignore} -->

> 系统启动完成。