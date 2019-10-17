package com.lcs.solution.service.tree2line.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.jayway.jsonpath.DocumentContext;
import com.lcs.solution.service.tree2line.Tree2LineService;
import com.lcs.solution.service.tree2line.domain.StrategyParseJsonColumn;

/**
 * 标题: 树转行服務实现类
 * 描述: 树转行服務实现类
 * 项目名称: 解決方案池
 * 创建人: lcs
 * 创建时间: 2019年10月17日 下午3:37:57
 * 修改人:
 * 修改时间:
 * 修改备注:
 */
public class Tree2LineServiceImpl implements Tree2LineService {
	public static final String NUMBER_PLACEHOLDER = "${num}";
	
	/**
	 * @see com.lcs.solution.service.tree2line.Tree2LineService#getLastStepNum(List)
	 */
	@Override
	public int getLastStepNum(List<StrategyParseJsonColumn> columns) {
		int maxStep = 0;
		for(StrategyParseJsonColumn column : columns) {
			if(column.getExecuteStep() > maxStep) {
				maxStep = column.getExecuteStep();
			}
		}
		
		return maxStep;
	}
	
	/**
	 * @see com.lcs.solution.service.tree2line.Tree2LineService#convertTree2Rows(DocumentContext, List, int)
	 */
	@Override
	public Map<Integer, List<List<StrategyParseJsonColumn>>> convertTree2Rows(DocumentContext jsonContext, List<StrategyParseJsonColumn> columns, int lastStepNum) {
		Map<Integer, List<List<StrategyParseJsonColumn>>> allStepRows = new HashMap<Integer, List<List<StrategyParseJsonColumn>>>();
		
		for(int step=1;step<=lastStepNum;step++) {
			List<List<StrategyParseJsonColumn>> currentStepRows = new ArrayList<List<StrategyParseJsonColumn>>();
			List<List<StrategyParseJsonColumn>> lastStepRows = new ArrayList<List<StrategyParseJsonColumn>>();
			if(1 < step) {
				lastStepRows = allStepRows.get(step-1);
			}
			
			List<StrategyParseJsonColumn> currentColumns = getColumnsByStep(step, columns);
			if(CollectionUtils.isNotEmpty(lastStepRows)) {// 存在上一层的值
				for(int i=0;i<lastStepRows.size();i++) {
					List<StrategyParseJsonColumn> lastRow = lastStepRows.get(i);
					Map<StrategyParseJsonColumn, List<String>> values = new HashMap<StrategyParseJsonColumn, List<String>>();
					boolean hasChildren = false; // 上一行是否有子集
					for(StrategyParseJsonColumn column : currentColumns) {
						
						String fetchPath = column.getFetchPath();
						if(fetchPath.contains(NUMBER_PLACEHOLDER)) {
							fetchPath = fetchPath.replace(NUMBER_PLACEHOLDER, String.valueOf(i));
						}
						List<String> valueList = jsonContext.read(fetchPath, List.class);
						if(CollectionUtils.isEmpty(valueList)) {
							continue;
						}
						hasChildren = true;
						
						// 生成字段值列表
						values.put(column, valueList);
					}
					
					// ！！！重要：上一行无子集，则直接合并至当前行
					if(!hasChildren) {
						currentStepRows.add(lastRow);
					}
					
					// 行转列
					row2column(values, lastRow, currentStepRows);
				}
			} else {// 不存在上一层的值，即第一层
				Map<StrategyParseJsonColumn, List<String>> values = new HashMap<StrategyParseJsonColumn, List<String>>();
				for(StrategyParseJsonColumn column : currentColumns) {
					
					
					String fetchPath = column.getFetchPath();
					List<String> valueList = jsonContext.read(fetchPath, List.class);
					if(CollectionUtils.isEmpty(valueList)) {
						continue;
					}
					
					// 生成字段值列表
					values.put(column, valueList);
				}
				
				// 行转列
				row2column(values, null, currentStepRows);
			}
			
			allStepRows.put(new Integer(step), currentStepRows);
		}
		
		return allStepRows;
	}
	
	/**
	 * @标题：获取当前步骤对应的字段集
	 * @作者：lcs
	 * @param step
	 * @param columns
	 * @return
	 * @创建时间 2019年10月17日下午3:42:36
	 * @修改人
	 * @修改时间
	 * @修改备注
	 */
	private List<StrategyParseJsonColumn> getColumnsByStep(Integer step, List<StrategyParseJsonColumn> columns) {
		List<StrategyParseJsonColumn> currentColumns = new ArrayList<StrategyParseJsonColumn>();
		for(StrategyParseJsonColumn column : columns) {
			if(column.getExecuteStep() != step) {
				continue;
			}
			
			currentColumns.add(column);
		}
		
		return currentColumns;
	}
	
	/**
	 * @标题：行转列
	 * @作者：lcs
	 * @param values
	 * @param lastRow
	 * @param currentStepRows
	 * @创建时间 2019年10月17日下午3:45:17
	 * @修改人
	 * @修改时间
	 * @修改备注
	 */
	private void row2column(Map<StrategyParseJsonColumn, List<String>> values, List<StrategyParseJsonColumn> lastRow, List<List<StrategyParseJsonColumn>> currentStepRows) {
		int maxValueSize = getMaxRowNum(values);
		for(int i=0;i<maxValueSize;i++) {
			List<StrategyParseJsonColumn> currentRow = new ArrayList<StrategyParseJsonColumn>();
			for(StrategyParseJsonColumn column : values.keySet()) {
				List<String> valueList = values.get(column);
				
				// 克隆一个新列实例
				// (注意：千万不要直接使用“column.setValue(valueList.get(i));”，否则下一个循环时valueList会为空)
				StrategyParseJsonColumn columnCopy = (StrategyParseJsonColumn)column.clone();
				if(i <= (valueList.size()-1)) {
					String value = String.valueOf(valueList.get(i));
					columnCopy.setValue(value);
				}
				currentRow.add(columnCopy);
			}
			
			// 合并上一行
			if(CollectionUtils.isNotEmpty(lastRow)) {
				currentRow.addAll(lastRow);
			}
			
			currentStepRows.add(currentRow);
		}
	}
	
	/**
	 * @标题：获取最大size
	 * @作者：lcs
	 * @param values
	 * @return
	 * @创建时间 2019年10月17日下午3:45:34
	 * @修改人
	 * @修改时间
	 * @修改备注
	 */
	private int getMaxRowNum(Map<StrategyParseJsonColumn, List<String>> values) {
		Collection<List<String>> columnValues = values.values();
		int maxValueSize = 0;
		for(List<String> list : columnValues) {
			if(maxValueSize < list.size()) {
				maxValueSize = list.size();
			}
		}
		return maxValueSize;
	}
}
