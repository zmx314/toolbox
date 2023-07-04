package com.zxm.toolbox.pojo.gt;

/**
 * 团体票报表-列属性
 * <p>
 * index 列号，从0开始
 * <p>
 * name 报表表头的列名
 * <p>
 * resource 报表中该列数据的来源
 * <p>
 * returnType 返回值类型
 * <p>
 * paxType 旅客类型
 * <p>
 * ticketDetailAttr 票型
 * <p>
 * needSum 报表最后一行统计行中该项是否需要求和
 * <p>
 * style 报表中数据格式
 * <p>
 * columnSize 列宽
 * 
 * @author 张学敏
 *
 */
public class ColumnProperties {
	private int index;
	private String name;
	private String resource;
	private String method;
	private String returnType;
	private String paxType;
	private String ticketDetailAttr;
	private boolean needSum;
	private String style;
	private int columnSize;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getPaxType() {
		return paxType;
	}

	public void setPaxType(String paxType) {
		this.paxType = paxType;
	}

	public String getTicketDetailAttr() {
		return ticketDetailAttr;
	}

	public void setTicketDetailAttr(String ticketDetailAttr) {
		this.ticketDetailAttr = ticketDetailAttr;
	}

	public boolean getNeedSum() {
		return needSum;
	}

	public void setNeedSum(boolean needSum) {
		this.needSum = needSum;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + columnSize;
		result = prime * result + index;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (needSum ? 1231 : 1237);
		result = prime * result + ((paxType == null) ? 0 : paxType.hashCode());
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
		result = prime * result + ((style == null) ? 0 : style.hashCode());
		result = prime * result + ((ticketDetailAttr == null) ? 0 : ticketDetailAttr.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnProperties other = (ColumnProperties) obj;
		if (columnSize != other.columnSize)
			return false;
		if (index != other.index)
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (needSum != other.needSum)
			return false;
		if (paxType == null) {
			if (other.paxType != null)
				return false;
		} else if (!paxType.equals(other.paxType))
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
			return false;
		if (style == null) {
			if (other.style != null)
				return false;
		} else if (!style.equals(other.style))
			return false;
		if (ticketDetailAttr == null) {
			return other.ticketDetailAttr == null;
		} else return ticketDetailAttr.equals(other.ticketDetailAttr);
	}

	@Override
	public String toString() {
		return "ColumnProperties [index=" + index + ", name=" + name + ", resource=" + resource + ", method=" + method
				+ ", returnType=" + returnType + ", paxType=" + paxType + ", ticketDetailAttr=" + ticketDetailAttr
				+ ", needSum=" + needSum + ", style=" + style + ", columnSize=" + columnSize + "]";
	}

}
