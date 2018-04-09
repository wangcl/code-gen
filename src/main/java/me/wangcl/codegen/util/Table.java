package me.wangcl.codegen.util;

/**
 * @author wangcl
 */
public class Table {
	// table name, eg. sysUser
	private String name;

	// first letter upper case, eg. SysUser
	private String capitalName;

	// database table name, eg. SYS_USER
	private String metaTableName;

	// comment for table
	private String metaComment;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCapitalName() {
		return capitalName;
	}

	public void setCapitalName(String capitalName) {
		this.capitalName = capitalName;
	}

	public String getMetaTableName() {
		return metaTableName;
	}

	public void setMetaTableName(String metaTableName) {
		this.metaTableName = metaTableName;
	}

	public String getMetaComment() {
		return metaComment;
	}

	public void setMetaComment(String metaComment) {
		this.metaComment = metaComment;
	}
	
}
