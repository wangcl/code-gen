package me.wangcl.codegen.util;

import jodd.util.CharUtil;

/**
 * 将Java类型转变成MyBatis定义的默认别名。
 *
 * @author wangcl
 */
public class Java2MyBatisTypeAliasConverter implements Converter<String, String> {
	@Override
	public String convert(String from) {
		Character first = from.charAt(0);
		if (CharUtil.isUppercaseAlpha(first)) {
			return from.toLowerCase();
		}
		return "_" + from;
	}
}
