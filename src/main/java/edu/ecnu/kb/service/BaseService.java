package edu.ecnu.kb.service;

import edu.ecnu.kb.service.upload.RowProcessor;
import edu.ecnu.kb.service.upload.UploadProcessor;
import edu.ecnu.kb.service.util.FileUtil;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用于抽象所有Service公共的方法，或者继承某个公共的工具类或实现某些公共的接口
 */
public class BaseService {

    protected final Sort SORT_ID_DESC = new Sort(Sort.Direction.DESC, "id");

    protected final Sort SORT_ID_ASC = new Sort(Sort.Direction.ASC, "id");

    /**
     * 解析上传文件并保存，返回保存成功的数量
     *
     * @param file         待解析文件
     * @param rowProcessor 对每一行的解析器
     * @param tag          进度条的key
     * @param target       目标位置
     * @param repository
     * @return
     */
    @Transactional
    public Map<String, Object> uploadProcess(InputStream file, RowProcessor rowProcessor, String tag, List target
            , JpaRepository repository) {
        UploadProcessor.process(file, rowProcessor, tag, target);
        int size = 0;
        for (Object o : target) {
            try {
                size++;
                repository.save(o);
            } catch (Exception e) {
                // 如果出现异常，则应该是重复。
                size--;
            }
        }
        Map<String, Object> res = new HashMap<>();
        res.put("size", size);
        return res;
    }

    /**
     * 批量保存，能够为了避免事物过大导致update或insert失败
     *
     * @param target     待保存列表
     * @param repository
     */
    public void batchSave(List target, JpaRepository repository) {
        int begin = 0;
        int end = begin + 200;
        while (end <= target.size()) {
            batchSave(target, begin, end, repository);
            begin = end;
            end = begin + 200;
        }
        // 将剩下不足一个batch的数据进行保存
        batchSave(target, begin, target.size(), repository);
    }

    /**
     * 批量保存，能够为了避免事物过大导致update或insert失败
     *
     * @param target     待保存列表
     * @param begin      开始位置
     * @param end        结束位置
     * @param repository
     */
    @Transactional
    public void batchSave(List target, int begin, int end, JpaRepository repository) {
        if (target.size() > 0)
            repository.saveAll(target.subList(begin, end));
    }

    /**
     * 分页获取对象,按照id逆序排序
     */
    public Page getByPage(Integer page, Integer size, JpaRepository repository) {
        return repository.findAll(PageRequest.of(page - 1, size, SORT_ID_DESC));
    }

    /**
     * 分页获取对象,按照id逆序排序。自定义排序方式
     */
    public Page getByPage(Integer page, Integer size, JpaRepository repository, Sort sort) {
        return repository.findAll(PageRequest.of(page - 1, size, sort));
    }

    /**
     * 将newValueMap中的值，赋给target。
     * <p>
     * 只会覆盖key与target中的属性值一样才生效，否则自动跳过。
     *
     * @param clazz       target的类型
     * @param target      新对象
     * @param newValueMap 存放新值得map
     */
    public void setNewValue(Class clazz, Object target, Map<String, Object> newValueMap) {

        // 将clazz中所有属性的名称放入一个map中，其中id不允许修改。
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : FieldUtils.getAllFields(clazz)) {
            fieldMap.put(field.getName(), field);
        }
        fieldMap.remove("id");

        // 遍历newValueMap，如果key存在fieldMap中，则将改值覆盖掉target中的值
        // 通过调用set方法来覆盖值，避免Spring data jpa使用懒加载缓存元数据
        try {

            for (String key : newValueMap.keySet()) {
                if (fieldMap.containsKey(key)) {
                    Field field = fieldMap.get(key);
                    clazz.getMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1)
                            , field.getType())
                            .invoke(target, newValueMap.get(key));
                }
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据导出成以#分割的csv格式
     *
     * @param columns 表头,即数据的key
     * @param data    数据
     * @return
     */
    public byte[] export(String[] columns, List<Map<String, Object>> data) {
        String title = "";
        for (String column : columns)
            title += column + "#";
        title = title.substring(0, title.length() - 1) + "\n";

        StringBuffer content = new StringBuffer();
        for (Map<String, Object> obj : data) {
            for (String column : columns) {
                content.append(obj.get(column)).append("#");
            }
            content.setLength(content.length() - 1);
            content.append("\n");
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer out = new BufferedWriter(new OutputStreamWriter(outputStream));


        try {
            out.write(title + content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return outputStream.toByteArray();
    }

    /**
     * 将两个关系图合并
     */
    public Map<String, Object> mergeGraphes(Map<String, Object> graph1, Map<String, Object> graph2) {
        Set<Map<String, Object>> nodes1 = (Set<Map<String, Object>>) graph1.get("nodes");
        Set<Map<String, Object>> nodes2 = (Set<Map<String, Object>>) graph2.get("nodes");
        List<Map<String, Object>> edges1 = (List<Map<String, Object>>) graph1.get("edges");
        List<Map<String, Object>> edges2 = (List<Map<String, Object>>) graph2.get("edges");
        
        nodes1.addAll(nodes2);
        edges1.addAll(edges2);
        return graph1;
    }
}
