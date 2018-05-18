package me.wangcl.codegen;

import jodd.util.StringUtil;
import me.wangcl.codegen.generator.VelocityGenerator;
import me.wangcl.codegen.util.Column;
import me.wangcl.codegen.util.Converter;
import me.wangcl.codegen.util.PathUtils;
import me.wangcl.codegen.util.Table;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 代码生成器。
 * <p>
 * 基于数据库单表，生成Java代码。
 * 在<code>config.properties</code>中配置数据库和代码结构相关信息，在<code>spring-beans.xml</code>中配置具体的代码生成器列表。
 * </p>
 *
 * @author wangcl
 */
public final class CodeGenerator {
	private static Logger logger = LoggerFactory.getLogger(CodeGenerator.class);

	private VelocityContext context;
	private DataSource dataSource;

	private String author;
	private String outputPath;

	private String dialect;
	private String pkGen;
	private String schemaName;
	private String tableName;
	private String tablePrefix;

	private String rootPackage;
	private String pkgPojo;
	private String pkgDao;
	private String pkgService;
	private String pkgServiceImpl;
	private String pkgController;

	private Table table; // 数据库表对象
	private List<Column> columns; // 数据库表包含的字段对象列表
	private Column pk; // 主键对象（目前仅支持单主键）

	private List<VelocityGenerator> generators = new ArrayList<>();
	private Converter<String, String> db2JavaNameConverter;
	private Converter<Column, String> db2JavaTypeConverter;
	private Converter<String, String> java2MyBatisTypeAliasConverter;
	private Converter<String, String> metaType2MyBatisJdbcTypeConverter;

	public void init() {
		initParams();
		initDbInfo();
		initVelocity();
		initContext();
		initFilePath();
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setGenerators(List<VelocityGenerator> generators) {
		this.generators = generators;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public void setPkGen(String pkGen) {
		this.pkGen = pkGen;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}

	public void setRootPackage(String rootPackage) {
		this.rootPackage = rootPackage;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public void setPkgPojo(String pkgPojo) {
		this.pkgPojo = pkgPojo;
	}

	public void setPkgDao(String pkgDao) {
		this.pkgDao = pkgDao;
	}

	public void setPkgService(String pkgService) {
		this.pkgService = pkgService;
	}

	public void setPkgServiceImpl(String pkgServiceImpl) {
		this.pkgServiceImpl = pkgServiceImpl;
	}

	public void setPkgController(String pkgController) {
		this.pkgController = pkgController;
	}

	@Autowired
	@Qualifier("db2JavaNameConverter")
	public void setDb2JavaNameConverter(Converter<String, String> db2JavaNameConverter) {
		this.db2JavaNameConverter = db2JavaNameConverter;
	}

	@Autowired
	@Qualifier("db2JavaTypeConverter")
	public void setDb2JavaTypeConverter(Converter<Column, String> db2JavaTypeConverter) {
		this.db2JavaTypeConverter = db2JavaTypeConverter;
	}

	@Autowired
	@Qualifier("java2MyBatisTypeAliasConverter")
	public void setJava2MyBatisTypeAliasConverter(Converter<String, String> java2MyBatisTypeAliasConverter) {
		this.java2MyBatisTypeAliasConverter = java2MyBatisTypeAliasConverter;
	}

	@Autowired
	@Qualifier("metaType2MyBatisJdbcTypeConverter")
	public void setMetaType2MyBatisJdbcTypeConverter(Converter<String, String> metaType2MyBatisJdbcTypeConverter) {
		this.metaType2MyBatisJdbcTypeConverter = metaType2MyBatisJdbcTypeConverter;
	}

	public void generate() throws Exception {
		logger.info("start to generate code ... ...\n");
		for (VelocityGenerator generator : generators) {
			generator.generate(context);
		}
		logger.info("code generation finished successfully.");
	}

	private void initParams() {
		// 检验outputpath不为空
		if (StringUtil.isBlank(outputPath)) {
			throw new IllegalArgumentException("未指定输出路径参数：outputPath");
		} else if (!(StringUtil.endsWithIgnoreCase(outputPath, "/"))) { // 保证输出目录以"/"结尾
			outputPath += "/";
		}

		// 检验rootpackage不为空
		if (StringUtil.isBlank(rootPackage)) {
			throw new IllegalArgumentException("未指定包名参数：rootPackage");
		}

		// 检验dialect配置的有效性，默认为mysql
		if (StringUtil.isBlank(dialect)) {
			dialect = "mysql";
		} else {
			// dialect置为小写
			dialect = dialect.toLowerCase();

			if (!("oracle".equals(dialect) || "mysql".equals(dialect))) {
				throw new IllegalArgumentException("不支持的数据库类型：" + dialect);
			}
		}

		// 主键默认使用guid
		if (StringUtil.isBlank(pkGen)) {
			pkGen = "guid";
		}

		// 检验tableName不为空
		if (StringUtil.isBlank(tableName)) {
			throw new IllegalArgumentException("未指定表名参数：tableName");
		}

		// 设置包名的默认值
		if (StringUtil.isBlank(pkgPojo)) {
			pkgPojo = "entity";
		}
		if (StringUtil.isBlank(pkgDao)) {
			pkgDao = "dao";
		}
		if (StringUtil.isBlank(pkgService)) {
			pkgService = "service";
		}
		if (StringUtil.isBlank(pkgServiceImpl)) {
			pkgServiceImpl = "service";
		}
		if (StringUtil.isBlank(pkgController)) {
			pkgController = "controller";
		}
	}

	private void initDbInfo() {
		table = new Table();
		columns = new ArrayList<>();

		table.setMetaTableName(tableName);

		// 如果表有在代码中无意义的前缀，则将其删除。例如：表名有"T_"前缀，代码中不需要此前缀。
		if (StringUtil.isNotBlank(tablePrefix) && tableName.startsWith(tablePrefix)) {
			tableName = StringUtil.cutPrefix(tableName, tablePrefix);
		}

		table.setCapitalName(StringUtil.toCamelCase(tableName, true, '_'));
		table.setName(StringUtil.toCamelCase(tableName, false, '_'));

		jdbcInit();
	}

	private void jdbcInit() {
		try (
				Connection connection = dataSource.getConnection();
				Statement st = connection.createStatement()
		) {
			DatabaseMetaData dbmd = connection.getMetaData();

			ResultSet trs, crs, pkrs;

			// 获取表信息
			if ("oracle".equals(dialect)) {
				trs = st.executeQuery("SELECT COMMENTS FROM USER_TAB_COMMENTS WHERE TABLE_NAME='" + table
						.getMetaTableName() + "'");
				if (trs.next()) {
					String remarks = trs.getString("COMMENTS");
					if (StringUtil.isNotBlank(remarks)) {
						table.setMetaComment(remarks);
					}
				}
			} else if ("mysql".equals(dialect)) {
				trs = st.executeQuery("SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES" +
						" WHERE TABLE_SCHEMA='" + schemaName + "' AND TABLE_NAME='" + table.getMetaTableName() + "'");
				if (trs.next()) {
					String remarks = trs.getString("TABLE_COMMENT");
					if (StringUtil.isNotBlank(remarks)) {
						table.setMetaComment(remarks);
					}
				}
			} else {
				trs = dbmd.getTables(null, schemaName, table.getMetaTableName(), new String[]{"TABLE"});
				if (trs.next()) {
					String remarks = trs.getString("REMARKS"); // DOES NOT WORK for oracle and mysql
					if (StringUtil.isNotBlank(remarks)) {
						table.setMetaComment(remarks);
					}
				}
			}

			// 获取主键信息
			pkrs = dbmd.getPrimaryKeys(null, schemaName, table.getMetaTableName());
			List<String> pks = new ArrayList<>();
			while (pkrs.next()) {
				String pk = pkrs.getString("COLUMN_NAME");
				pks.add(pk);
			}

			// 获取字段注释信息
			Map<String, String> columnCommentMap = new HashMap<>();
			if ("oracle".equals(dialect)) {
				crs = st.executeQuery("SELECT COLUMN_NAME, COMMENTS FROM USER_COL_COMMENTS WHERE TABLE_NAME='" +
						table.getMetaTableName() + "'");
				while (crs.next()) {
					String remarks = crs.getString("COMMENTS");
					if (StringUtil.isNotBlank(remarks)) {
						columnCommentMap.put(crs.getString("COLUMN_NAME"), remarks);
					}
				}
			} else {
				crs = dbmd.getColumns(null, schemaName, table.getMetaTableName(), "%");
				while (crs.next()) {
					String remarks = crs.getString("REMARKS");
					if (StringUtil.isNotBlank(remarks)) {
						columnCommentMap.put(crs.getString("COLUMN_NAME"), remarks);
					}
				}
			}

			// 获取字段信息
			ResultSet rs = st.executeQuery("SELECT * FROM " + table.getMetaTableName() + " WHERE 1=0");
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				Column column = new Column();
				String columnName = rsmd.getColumnName(i);
				String metaType = rsmd.getColumnTypeName(i);

				column.setMetaColumnName(columnName);
				column.setMetaClassName(rsmd.getColumnClassName(i));
				column.setMetaTypeName(metaType);
				column.setMetaPrecision(rsmd.getPrecision(i));
				column.setMetaScale(rsmd.getScale(i));
				column.setMetaLabel(rsmd.getColumnLabel(i));

				String name = db2JavaNameConverter.convert(columnName.toLowerCase());
				String type = db2JavaTypeConverter.convert(column);

				column.setName(name);
				column.setCapitalName(StringUtil.capitalize(name));
				column.setType(type);
				column.setMyBatisTypeAlias(java2MyBatisTypeAliasConverter.convert(type));
				column.setMyBatisJdbcType(metaType2MyBatisJdbcTypeConverter.convert(metaType));

				if (ResultSetMetaData.columnNoNulls == rsmd.isNullable(i)) {
					column.setNullable(false);
				}
				if (pks.contains(columnName)) {
					column.setPk(true);
					pk = column;
				}

				if (columnCommentMap.size() > 0) {
					if (columnCommentMap.containsKey(columnName)) {
						column.setMetaComment(columnCommentMap.get(columnName));
					}
				}

				columns.add(column);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void initVelocity() {
		Properties config = new Properties();
		try {
			config.load(CodeGenerator.class.getClassLoader().getResourceAsStream("velocity.properties"));
			Velocity.init(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initContext() {
		context = new VelocityContext();

		// 通用信息
		context.put("author", author);

		// 数据库相关信息
		context.put("dialect", dialect);
		context.put("pkGen", pkGen);
		context.put("table", table);
		context.put("columns", columns);
		context.put("pk", pk);

		// 生成文件的路径
		context.put("rootPackage", rootPackage);
		String rootPackagePath = rootPackage.replace(".", "/") + "/";
		context.put("javaPath", outputPath + "src/main/java/" + rootPackagePath);
		context.put("resourcesPath", outputPath + "src/main/resources/" + rootPackagePath);
		context.put("testJavaPath", outputPath + "src/test/java/" + rootPackagePath);
		context.put("testJavaPath", outputPath + "src/test/java/" + rootPackagePath);
		context.put("webJspPath", outputPath + "src/main/webapp/WEB-INF/jsp/" + table.getName() + "/");

		// Java程序包
		context.put("pkgPojo", pkgPojo);
		context.put("pkgDao", pkgDao);
		context.put("pkgService", pkgService);
		context.put("pkgServiceImpl", pkgServiceImpl);
		context.put("pkgController", pkgController);
	}

	private void initFilePath() {
		PathUtils.makeDirectory(outputPath + "src/");
		PathUtils.makeDirectory(outputPath + "src/main/java/");
		PathUtils.makeDirectory(outputPath + "src/main/resources/");
		PathUtils.makeDirectory(outputPath + "src/test/java/");
		PathUtils.makeDirectory(outputPath + "src/main/webapp/WEB-INF/jsp/");
	}
}
