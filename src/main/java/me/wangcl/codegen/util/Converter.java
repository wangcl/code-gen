package me.wangcl.codegen.util;

/**
 * 转换器接口。
 *
 * @author wangcl
 */
public interface Converter<F, T> {
	T convert(F from);
}
