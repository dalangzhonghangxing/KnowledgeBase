## 以实现功能点
1. 句子、知识点、标签上传、分页查询、删除
2. 知识点pair生成
3. 知识点pair打标签
4. 实现导出功能
5. 展示知识体系图
6. 将时间结果记录在文件中，文件名为实验的名称，由用户指定。实现保存与解析功能。

## 待实现功能点
1. 根据实验结果画出折线图
2. 实现特征工程
    + stanford parser，为了提高效率，为每一句句子生成好句法树，然后以json格式保存起来，从而可以重用。
    + w2v(默认模型，根据已有句子训练)
    + position embedding
    + POS
3. 实现java调用python接口，设计执行方式。
    + 将数据预处理的结果存到指定文件夹下。
    + python程序接收路径为参数，然后执行。
    + python执行的中间结果也通过文件的方式与java通信，要设计统一的输出格式。
4. 确定需要画出的图的类型，从而确定在模型训练过程中需要记录的数据，以及解析的方式。
5. 为每对关系对生成句子，放到指定文件中

## 注意
1. 启动项目
    + cd KnowledgeBase
    + mvn install
    + mvn spring-boot:run
2. 从maven repository上下载ik-analyzer失败。
    + 新建一个目录，执行git clone git@github.com:wks/ik-analyzer.git
    + 然后执行cd ik-analyzer/，执行mvn install -Dmaven.test.skip=true
