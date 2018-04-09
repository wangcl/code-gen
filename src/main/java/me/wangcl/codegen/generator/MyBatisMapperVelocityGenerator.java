package me.wangcl.codegen.generator;

import me.wangcl.codegen.util.Table;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangcl
 */
public class MyBatisMapperVelocityGenerator implements VelocityGenerator {
	private static Logger logger = LoggerFactory.getLogger(MyBatisMapperVelocityGenerator.class);

	@Override
	public void generate(VelocityContext context) throws Exception {
		logger.info("generate() starts...");

		String javaTemplate = "templates/MapperTemplate.vm";
		String xmlTemplate = "templates/MapperXmlTemplate.vm";
		String table = ((Table) context.get("table")).getCapitalName();
		String path = context.get("pkgDao").toString() + "/" + table + "Mapper";
		String javaFile = context.get("javaPath") + path + ".java";
		String xmlFile = context.get("resourcesPath") + path + ".xml";

		VelocityGeneratorUtils.generate(context, javaTemplate, javaFile);
		VelocityGeneratorUtils.generate(context, xmlTemplate, xmlFile);

		logger.info("generate() ends successfully.");
	}

}
