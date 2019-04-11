package me.wangcl.codegen.generator;

import me.wangcl.codegen.util.Table;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangcl
 */
public class ControllerVelocityGenerator implements VelocityGenerator {
	private static Logger logger = LoggerFactory.getLogger(ControllerVelocityGenerator.class);

	@Override
	public void generate(VelocityContext context) throws Exception {
		logger.info("generate() starts...");

		String template = "templates/ControllerTemplate.vm";

		String table = ((Table) context.get("table")).getCapitalName();
		String path = (String) context.get("javaPath");

		boolean pathSwitch = "true".equals(context.get("pathSwitch").toString()) ? true : false;
		if (pathSwitch) {
			path += context.get("pkgController") + "/";
		}

		VelocityGeneratorUtils.generate(context, template, path + table + "Controller.java");

		logger.info("generate() ends successfully.");
	}

}
