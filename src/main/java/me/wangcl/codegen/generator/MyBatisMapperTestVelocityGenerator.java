package me.wangcl.codegen.generator;

import me.wangcl.codegen.util.Table;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangcl
 */
public class MyBatisMapperTestVelocityGenerator implements VelocityGenerator {
	private static Logger logger = LoggerFactory.getLogger(MyBatisMapperTestVelocityGenerator.class);

	@Override
	public void generate(VelocityContext context) throws Exception {
		logger.info("generate() starts...");

		String template = "templates/MapperTestTemplate.vm";

		String table = ((Table) context.get("table")).getCapitalName();
		String path = (String) context.get("testJavaPath");

		boolean pathSwitch = "true".equals(context.get("pathSwitch").toString()) ? true : false;
		if (pathSwitch) {
			path += context.get("pkgDao").toString() + "/";
		}

		VelocityGeneratorUtils.generate(context, template, path + table + "MapperTest.java");

		logger.info("generate() ends successfully.");
	}

}
