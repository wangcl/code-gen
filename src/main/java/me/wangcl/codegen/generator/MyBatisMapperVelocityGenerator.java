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
		String javaPath = (String) context.get("javaPath");
		String xmlPath = (String) context.get("resourcesPath");

		boolean pathSwitch = "true".equals(context.get("pathSwitch").toString()) ? true : false;
		if (pathSwitch) {
			javaPath += context.get("pkgDao") + "/";
			xmlPath += context.get("pkgDao") + "/";
		}

		VelocityGeneratorUtils.generate(context, javaTemplate, javaPath + table + "Mapper.java");
		VelocityGeneratorUtils.generate(context, xmlTemplate, xmlPath + table + "Mapper.xml");

		logger.info("generate() ends successfully.");
	}

}
