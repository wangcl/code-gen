package me.wangcl.codegen.util;

import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Set;

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
		if (isDigital(metaType)) {
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
		} else if (isDatetime(metaType)) {
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

	/*
	 * 判断是否为数字型。
	 */
	private boolean isDigital(String type) {
		if (StringUtils.isEmpty(type)) {
			return false;
		}

		Set<String> types = new HashSet<>();
		types.add("NUMBER");
		types.add("INT");
		types.add("BIGINT");
		types.add("SMALLINT");
		types.add("TINYINT");
		types.add("DECIMAL");

		return types.contains(type);
	}

	/*
	 * 判断是否为日期型。
	 */
	private boolean isDatetime(String type) {
		if (StringUtils.isEmpty(type)) {
			return false;
		}

		Set<String> types = new HashSet<>();
		types.add("DATE");
		types.add("TIMESTAMP");
		types.add("DATETIME");

		return types.contains(type);
	}

}
