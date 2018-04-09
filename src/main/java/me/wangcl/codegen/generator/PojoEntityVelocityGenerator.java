package me.wangcl.codegen.generator;

import me.wangcl.codegen.util.Table;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangcl
 */
public class PojoEntityVelocityGenerator implements VelocityGenerator {
	private static Logger logger = LoggerFactory.getLogger(PojoEntityVelocityGenerator.class);

	@Override
	public void generate(VelocityContext context) throws Exception {
		logger.info("generate() starts...");

		String template = "templates/EntityTemplate.vm";
		String table = ((Table) context.get("table")).getCapitalName();
		String file = "" + context.get("javaPath") + context.get("pkgPojo") + "/" + table + ".java";
		VelocityGeneratorUtils.generate(context, template, file);

		logger.info("generate() ends successfully.");
	}

}
