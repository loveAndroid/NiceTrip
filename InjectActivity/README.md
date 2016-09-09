

Android Activity 插件动态加载


项目介绍  
  - library 主库，实现了 动态加载的功能
  - library_public 实现了一套用于 宿主和插件apk通信的公共接口
  - sample 实例工程
  - publicA 插件实例工程


特性 ：
        插件资源分离，插件apk可以安装调试发布
        
用法： 1. 宿主 要依赖 Library ，并继承 DynamicApplication 
       2. 插件activity基类要继承自 InjectBaseAct 并实现抽象方法，返回 apk的包名
