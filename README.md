## 功能点
1. 句子、知识点、标签上传、分页查询、删除
2. 知识点pair生成
3. 知识点pair打标签
4. 展示知识体系图

## 注意
1. 启动项目
    + cd KnowledgeBase
    + mvn install
    + mvn spring-boot:run
2. 从maven repository上下载ik-analyzer失败。
    + 新建一个目录，执行git clone git@github.com:wks/ik-analyzer.git
    + 然后执行cd ik-analyzer/，执行mvn install -Dmaven.test.skip=true
