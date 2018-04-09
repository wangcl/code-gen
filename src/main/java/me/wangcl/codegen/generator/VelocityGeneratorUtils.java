package me.wangcl.codegen.generator;

import me.wangcl.codegen.util.PathUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Velocity生成器辅助类。
 *
 * @author wangcl
 */
public final class VelocityGeneratorUtils {
	private static Logger logger = LoggerFactory.getLogger(VelocityGeneratorUtils.class);

	/**
	 * 根据模板生成文件。
	 *
	 * @param context velocity上下文环境
	 * @param templateName 模板文件名
	 * @param fullFileName 生成文件的全路径名
	 * @throws Exception
	 */
	public static void generate(VelocityContext context, String templateName, String fullFileName) throws Exception {
		try {
			Template template = Velocity.getTemplate(templateName);
			BufferedWriter writer = new BufferedWriter(new FileWriter(PathUtils.makeFile(fullFileName)));
			if (template != null) {
				template.merge(context, writer);
			}
			writer.flush();
			writer.close();
		} catch (ResourceNotFoundException e) {
			logger.error("cannot find template: {}", templateName);
			throw e;
		} catch (ParseErrorException e) {
			logger.error("syntax error in template: {}", templateName);
			throw e;
		} catch (Exception e) {
			StringBuffer buffer = new StringBuffer("");
			for (StackTraceElement element : e.getStackTrace()) {
				buffer.append("\n\t" + element.getClassName() + "." + element.getMethodName() + "() on line: " +
						element.getLineNumber());
			}
			logger.error("exception occurs: {}", buffer.toString());
			throw e;
		}
	}

}
