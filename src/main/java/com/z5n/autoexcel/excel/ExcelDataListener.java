package com.z5n.autoexcel.excel;

/**
 * @program: autoexcel
 * @ClassName: ExcelDataListener
 * @Description: ExcelDataListener
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
public class ExcelDataListener extends AnalysisEventListener<Map<Integer, String>> {

    private final List<Map<Integer, String>> headList = new ArrayList<>();

    @Override
    public void invoke(Map<Integer, String> integerStringMap, AnalysisContext analysisContext) {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        headList.add(headMap);
        log.info("读取到表头数据 : {}", JSON.toJSONString(headMap));
    }

    public List<Map<Integer, String>> getHeadList() {
        return headList;
    }
}
