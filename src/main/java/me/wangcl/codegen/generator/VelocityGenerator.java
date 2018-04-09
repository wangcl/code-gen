package me.wangcl.codegen.generator;

import org.apache.velocity.VelocityContext;

/**
 * Vecocity代码生成器。
 *
 * @author wangcl
 */
public interface VelocityGenerator {
	/**
	 * 生成代码内容。
	 *
	 * @param context Velocity上下文。
	 */
	void generate(VelocityContext context) throws Exception;
}
