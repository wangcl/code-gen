package me.wangcl.codegen.util;

/**
 * 将JDBC的元类型转变成MyBatis的jdbcType。
 *
 * @author wangcl
 */
public class MetaType2MyBatisJdbcTypeConverter implements Converter<String, String> {
	@Override
	public String convert(String from) {
		String to;
		switch (from) {
			case "NUMBER":
				to = "NUMERIC";
				break;
			case "INT":
				to = "INTEGER";
				break;
			case "VARCHAR2":
				to = "VARCHAR";
				break;
			case "DATETIME":
				to = "DATE";
				break;
			default:
				to = from;
		}
		return to;
	}
}
