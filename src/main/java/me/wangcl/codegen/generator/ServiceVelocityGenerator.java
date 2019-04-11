package me.wangcl.codegen.generator;

import me.wangcl.codegen.util.Table;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangcl
 */
public class ServiceVelocityGenerator implements VelocityGenerator {
	private static Logger logger = LoggerFactory.getLogger(ServiceVelocityGenerator.class);

	@Override
	public void generate(VelocityContext context) throws Exception {
		logger.info("generate() starts...");

		String serviceTemplate = "templates/ServiceTemplate.vm";
		String serviceImplTemplate = "templates/ServiceImplTemplate.vm";

		String servicePath = (String) context.get("javaPath");
		String implPath = (String) context.get("javaPath");
		String table = ((Table) context.get("table")).getCapitalName();

		boolean pathSwitch = "true".equals(context.get("pathSwitch").toString()) ? true : false;
		if (pathSwitch) {
			servicePath += context.get("pkgService") + "/";
			implPath += context.get("pkgServiceImpl").toString().replace(".", "/") + "/";
		}

		VelocityGeneratorUtils.generate(context, serviceTemplate, servicePath + table + "Service.java");
		VelocityGeneratorUtils.generate(context, serviceImplTemplate, implPath + table + "ServiceImpl.java");

		logger.info("generate() ends successfully.");
	}

}
