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
		String file = context.get("testJavaPath") + context.get("pkgDao").toString() + "/" + table + "MapperTest.java";
		VelocityGeneratorUtils.generate(context, template, file);

		logger.info("generate() ends successfully.");
	}

}
