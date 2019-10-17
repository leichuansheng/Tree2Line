# Tree2Line
将一个json树转换成行记录

代码入口：com.lcs.solution.service.tree2line.Tree2LineTest

设计思路如下：
1、根据json树的深度，定义取值步骤；
2、每一个步骤都将上一个步骤的值合并至本步骤；
3、所有的步骤的取值完毕后，取最后一个步骤的取值。
