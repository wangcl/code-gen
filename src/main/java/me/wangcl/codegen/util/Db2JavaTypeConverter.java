package me.wangcl.codegen.util;

/**
 * 数据库字段类型向Java类型的转换器。
 *
 * @author wangcl
 */
public class Db2JavaTypeConverter implements Converter<Column, String> {
	@Override
	public String convert(Column column) {
		String type;
		String metaType = column.getMetaTypeName();
		// TODO: more meta types support
		if ("NUMBER".equals(metaType) || "INT".equals(metaType) || "BIGINT".equals(metaType) || "TINYINT".equals(metaType)) {
			int precision = column.getMetaPrecision();
			int scale = column.getMetaScale();
			if (scale == 0) {
				if (precision > 9) {
					type = "Long";
				} else {
					type = "Integer";
				}
			} else {
				type = "Double";
			}
		} else if ("DATE".equals(metaType) || "TIMESTAMP".equals(metaType) || "DATETIME".equals(metaType)) {
			type = "Date";
		} else {
			switch (metaType) {
				case "oracle.sql.BLOB":
				case "oracle.sql.CLOB":
					type = "byte[]";
					break;
				default:
					type = "String";
			}
		}
		return type;
	}
}
