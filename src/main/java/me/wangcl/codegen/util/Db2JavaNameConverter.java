package me.wangcl.codegen.util;

import jodd.util.StringUtil;

/**
 * 数据库名称向Java名称的转换器。
 *
 * @author wangcl
 */
public class Db2JavaNameConverter implements Converter<String, String> {
	@Override
	public String convert(String dbname) {
		return StringUtil.toCamelCase(dbname, false, '_');
	}
}
