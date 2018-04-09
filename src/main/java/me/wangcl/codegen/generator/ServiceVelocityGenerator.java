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
		String table = ((Table) context.get("table")).getCapitalName();
		String serviceFile = "" + context.get("javaPath") + context.get("pkgService") + "/" + table + "Service.java";
		String serviceImplFile = context.get("javaPath") + context.get("pkgServiceImpl").toString().replace(".",
				"/") + "/" + table + "ServiceImpl.java";
		VelocityGeneratorUtils.generate(context, serviceTemplate, serviceFile);
		VelocityGeneratorUtils.generate(context, serviceImplTemplate, serviceImplFile);

		logger.info("generate() ends successfully.");
	}

}
