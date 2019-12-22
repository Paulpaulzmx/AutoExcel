package com.z5n.autoexcel.excel;

/**
 * @program: autoexcel
 * @ClassName: TemplateDataListener
 * @Description: TemplateDataListener
 * @Author: chen qi
 * @Date: 2019/12/22 16:59
 * @Version: 1.0
 **/

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
public class TemplateDataListener extends AnalysisEventListener<Map<Integer, String>> {
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();



    /**
     * 加上存储数据库
     */
    private void saveData() {
    }

    @Override
    public void invoke(Map<Integer, String> integerStringMap, AnalysisContext analysisContext) {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("读取到表头数据 : {}", JSON.toJSONString(headMap));
    }
}
