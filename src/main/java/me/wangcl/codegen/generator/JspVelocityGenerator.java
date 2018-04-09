package me.wangcl.codegen.generator;

import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangcl
 */
public class JspVelocityGenerator implements VelocityGenerator {
	private static Logger logger = LoggerFactory.getLogger(JspVelocityGenerator.class);

	@Override
	public void generate(VelocityContext context) throws Exception {
		logger.info("generate() starts...");

		String indexTemplate = "templates/JspIndexTemplate.vm";
		String indexFile = context.get("webJspPath") + "index.jsp";
		VelocityGeneratorUtils.generate(context, indexTemplate, indexFile);

		String viewTemplate = "templates/JspViewTemplate.vm";
		String viewFile = context.get("webJspPath") + "view.jsp";
		VelocityGeneratorUtils.generate(context, viewTemplate, viewFile);

		String inputTemplate = "templates/JspInputTemplate.vm";
		String inputFile = context.get("webJspPath") + "input.jsp";
		VelocityGeneratorUtils.generate(context, inputTemplate, inputFile);

		String editTemplate = "templates/JspEditTemplate.vm";
		String editFile = context.get("webJspPath") + "edit.jsp";
		VelocityGeneratorUtils.generate(context, editTemplate, editFile);

		logger.info("generate() ends successfully.");
	}

}
