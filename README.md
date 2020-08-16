# simple-batch
简单批处理模块
它能做什么？  
1、较好的描述一个批处理任务  
2、帮你管理任务结果的记录，在代码里方便存储任何时候的计算结果  
3、支持任务断点重启，当然这需要业务代码支持任务断点重做，框架只能提供上下文  
4、支持分布式部署（未来）  
5、任务查询管理dashboard（未来）  
## 环境准备
### 1、spring 
   向spring容器中注入 IBatchStore 的实现  默认实现了 基于map内存的存储 MemoryStore，（数据库 Redis等其它方式的存储待实现）
   向spring容器中注入 IBatchManager的实现，默认实现：DefaultBatchManage，manager需要注入store
### 2、普通Java环境
   直接new 依赖注入即可
## 使用步骤
 1）定义参数模型，参考测试用例
```
site.ymkj.batch.core.test.TestArgs
```
 2）定义processor 确定批处理阶段 参考测试用例
 ```
 site.ymkj.batch.core.test.TestProcessor
 ```
 3）提交任务
 参考测试用例
 ```
 site.ymkj.batch.core.test.BatchTest
 ```
