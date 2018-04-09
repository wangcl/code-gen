package me.wangcl.codegen.util;

/**
 * 数据库表字段信息对象。
 *
 * @author wangcl
 */
public class Column {
	// java field name, eg. userId
	private String name;

	// first letter in upper case, eg. UserId
	private String capitalName;

	// java type, eg. Long
	private String type;

	// eg. _int, long
	private String myBatisTypeAlias;

	// eg. NUMERIC, VARCHAR
	private String myBatisJdbcType;

	private boolean pk = false;

	private boolean nullable = true;

	// database column name, eg. USER_ID
	private String metaColumnName;

	// jdbc class name, eg. java.math.BigDecimal
	private String metaClassName;

	// jdbc type name, eg. NUMBER
	private String metaTypeName;

	// eg. NUMBER(12,0) -> 12
	private int metaPrecision;

	// eg. NUMBER(12,0) -> 0
	private int metaScale;

	// the sql "as" clause, or column name if no "as"
	private String metaLabel;

	// comment for column
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMyBatisTypeAlias() {
		return myBatisTypeAlias;
	}

	public void setMyBatisTypeAlias(String myBatisTypeAlias) {
		this.myBatisTypeAlias = myBatisTypeAlias;
	}

	public String getMyBatisJdbcType() {
		return myBatisJdbcType;
	}

	public void setMyBatisJdbcType(String myBatisJdbcType) {
		this.myBatisJdbcType = myBatisJdbcType;
	}

	public boolean isPk() {
		return pk;
	}

	public void setPk(boolean pk) {
		this.pk = pk;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public String getMetaColumnName() {
		return metaColumnName;
	}

	public void setMetaColumnName(String metaColumnName) {
		this.metaColumnName = metaColumnName;
	}

	public String getMetaClassName() {
		return metaClassName;
	}

	public void setMetaClassName(String metaClassName) {
		this.metaClassName = metaClassName;
	}

	public String getMetaTypeName() {
		return metaTypeName;
	}

	public void setMetaTypeName(String metaTypeName) {
		this.metaTypeName = metaTypeName;
	}

	public int getMetaPrecision() {
		return metaPrecision;
	}

	public void setMetaPrecision(int metaPrecision) {
		this.metaPrecision = metaPrecision;
	}

	public int getMetaScale() {
		return metaScale;
	}

	public void setMetaScale(int metaScale) {
		this.metaScale = metaScale;
	}

	public String getMetaLabel() {
		return metaLabel;
	}

	public void setMetaLabel(String metaLabel) {
		this.metaLabel = metaLabel;
	}

	public String getMetaComment() {
		return metaComment;
	}

	public void setMetaComment(String metaComment) {
		this.metaComment = metaComment;
	}
	
}
