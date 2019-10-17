package com.lcs.solution.service.tree2line;

import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.DocumentContext;
import com.lcs.solution.service.tree2line.domain.StrategyParseJsonColumn;

/**
 * 标题:树转行服務接口 
 * 描述:树转行服務接口 
 * 项目名称: 解決方案池
 * 创建人: lcs
 * 创建时间: 2019年10月17日 下午4:46:56
 * 修改人:
 * 修改时间:
 * 修改备注:
 */
public interface Tree2LineService {

	/**
	 * @标题：获取最后一个步骤的序号
	 * @作者：lcs
	 * @param columns
	 * @return
	 * @创建时间 2019年10月17日下午4:45:49
	 * @修改人
	 * @修改时间
	 * @修改备注
	 */
	public int getLastStepNum(List<StrategyParseJsonColumn> columns);
	
	/**
	 * @标题：将树形结构数据转成行
	 * @作者：lcs
	 * @param jsonContext
	 * @param columns
	 * @param lastStepNum
	 * @return
	 * @创建时间 2019年10月17日下午4:49:02
	 * @修改人
	 * @修改时间
	 * @修改备注
	 */
	public Map<Integer, List<List<StrategyParseJsonColumn>>> convertTree2Rows(DocumentContext jsonContext, List<StrategyParseJsonColumn> columns, int lastStepNum);
}
