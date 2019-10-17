package com.lcs.solution.service.tree2line;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.lcs.solution.service.tree2line.domain.StrategyParseJsonColumn;
import com.lcs.solution.service.tree2line.impl.Tree2LineServiceImpl;

/**
 * 标题: 树转行测试类 
 * 描述: 树转行测试类
 * 项目名称: 解決方案池
 * 创建人: lcs
 * 创建时间: 2019年10月17日 下午3:49:29
 * 修改人:
 * 修改时间:
 * 修改备注:
 */
public class Tree2LineTest {

	/**
	 * @标题：创建字段示例
	 * @作者：雷传盛
	 * @return
	 * @创建时间 2019年10月8日上午9:14:37
	 * @修改人
	 * @修改时间
	 * @修改备注
	 */
	public static List<StrategyParseJsonColumn> createColumns() {
		List<StrategyParseJsonColumn> columns = new ArrayList<StrategyParseJsonColumn>();
		
		StrategyParseJsonColumn column1 = new StrategyParseJsonColumn();
		column1.setSortNum(1);
		column1.setTargetColumn("store_name");
		column1.setFetchPath("$..store_name");
		column1.setExecuteStep(1);
		columns.add(column1);
		
		StrategyParseJsonColumn column2 = new StrategyParseJsonColumn();
		column2.setSortNum(2);
		column2.setTargetColumn("business_scope");
		column2.setFetchPath("$..business_scope");
		column2.setExecuteStep(1);
		columns.add(column2);
		
		StrategyParseJsonColumn column3 = new StrategyParseJsonColumn();
		column3.setSortNum(3);
		column3.setTargetColumn("category_name");
		column3.setFetchPath("$..categories[*].category_name");
		column3.setExecuteStep(2);
		columns.add(column3);
		
		StrategyParseJsonColumn column4 = new StrategyParseJsonColumn();
		column4.setSortNum(4);
		column4.setTargetColumn("book_name");
		column4.setFetchPath("$..categories["+Tree2LineServiceImpl.NUMBER_PLACEHOLDER+"].books[*].book_name");
		column4.setExecuteStep(3);
		columns.add(column4);
		
		StrategyParseJsonColumn column5 = new StrategyParseJsonColumn();
		column5.setSortNum(5);
		column5.setTargetColumn("author");
		column5.setFetchPath("$..categories["+Tree2LineServiceImpl.NUMBER_PLACEHOLDER+"].books[*].author");
		column5.setExecuteStep(3);
		columns.add(column5);
		
		StrategyParseJsonColumn column6 = new StrategyParseJsonColumn();
		column6.setSortNum(6);
		column6.setTargetColumn("tag");
		column6.setFetchPath("$..categories["+Tree2LineServiceImpl.NUMBER_PLACEHOLDER+"].books[*].tag");
		column6.setExecuteStep(3);
		columns.add(column6);
		
		return columns;
	}
	
	public static void main(String[] args) {
		String jsonStr = "{\"store_name\": \"我的杂书店\",\"business_scope\": \"图书、杂志\",\"categories\": [{\"category_name\": \"图书\",\"books\": [{\"book_name\": \"红楼梦\",\"author\": \"曹雪芹\",\"tag\": \"古典文学、四大名著、章回体小说\"},{\"book_name\": \"平凡的世界\",\"author\": \"路遥\",\"tag\": \"当代文学、矛盾文学奖\"},{\"book_name\": \"源氏物语\",\"author\": \"紫式部\",\"tag\": \"日本名著、古典文学\"},{\"book_name\": \"霍乱时期的爱情\",\"author\": \"马尔克斯\",\"tag\": \"南美文学、诺贝尔文学奖\"}]},{\"category_name\": \"杂志\",\"books\": [{\"book_name\": \"时代周刊\",\"author\": \"时代周刊出版社\",\"tag\": \"全球权威刊物\"},{\"book_name\": \"知音\",\"author\": \"知音出版社\",\"tag\": \"知音\"},{\"book_name\": \"读者\",\"author\": \"读者出版社\",\"tag\": \"读者\"},{\"book_name\": \"计算机爱好者\",\"author\": \"清华大学机械出版社\",\"tag\": \"计算机\"}]}]}";
		List<StrategyParseJsonColumn> columns = createColumns();
		
		Tree2LineService tree2LineService = new Tree2LineServiceImpl();
		int lastStepNum = tree2LineService.getLastStepNum(columns);
		DocumentContext jsonContext = JsonPath.parse(jsonStr);
		Map<Integer, List<List<StrategyParseJsonColumn>>> allStepRows = tree2LineService.convertTree2Rows(jsonContext, columns, lastStepNum);
		
		// 取最后一个步骤的数据行
		List<List<StrategyParseJsonColumn>> rowList = allStepRows.get(new Integer(lastStepNum));
		for(List<StrategyParseJsonColumn> row : rowList) {
			row.sort(new Comparator<StrategyParseJsonColumn>() {

				@Override
				public int compare(StrategyParseJsonColumn o1, StrategyParseJsonColumn o2) {
					return o1.getSortNum()-o2.getSortNum();
				}
			});
			
			StringBuilder rowStr = new StringBuilder();
			for(StrategyParseJsonColumn column : row) {
				rowStr.append(column.getTargetColumn() +"==>" + column.getValue() + "\t");
			}
			System.out.println(rowStr.toString());
		}
		
		
	}
}
