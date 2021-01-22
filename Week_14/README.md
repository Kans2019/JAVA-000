# 作业
2、去掉内存Queue，设计[自定义Queue](kmq-core/src/main/java/io/kimmking/kmq/core/Kmq.java)，实现消息确认和消费offset 
* 1）自定义内存Message数组模拟Queue。 
* 2）使用指针记录当前消息写入位置。 
* 3）对于每个命名消费者，用指针记录消费位置。