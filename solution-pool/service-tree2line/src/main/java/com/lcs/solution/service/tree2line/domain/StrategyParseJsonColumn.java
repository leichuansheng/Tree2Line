package com.lcs.solution.service.tree2line.domain;

/**
 * 标题: JSON解析策略之字段信息实体类
 * 描述: JSON解析策略之字段信息实体类
 * 项目名称: 大数据项目
 * 创建人: lcs
 * 创建时间: 2019年10月17日 下午3:31:48
 * 修改人:
 * 修改时间:
 * 修改备注:
 */
public class StrategyParseJsonColumn implements Cloneable {
	private String targetColumn; // 目标字段
	private String fetchPath; // 取值路径，须符合JsonPath表达式规范
	private int executeStep; // 执行步骤
	private int sortNum; // 序号，从1开始
	
    private String value; // 字段值
	public String getTargetColumn() {
		return targetColumn;
	}
	public void setTargetColumn(String targetColumn) {
		this.targetColumn = targetColumn;
	}
	public String getFetchPath() {
		return fetchPath;
	}
	public void setFetchPath(String fetchPath) {
		this.fetchPath = fetchPath;
	}
	public int getExecuteStep() {
		return executeStep;
	}
	public void setExecuteStep(int executeStep) {
		this.executeStep = executeStep;
	}
	public int getSortNum() {
		return sortNum;
	}
	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
