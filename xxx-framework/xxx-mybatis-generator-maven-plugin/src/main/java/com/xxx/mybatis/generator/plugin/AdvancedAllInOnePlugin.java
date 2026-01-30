package com.xxx.mybatis.generator.plugin;


import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 高级 MyBatis Generator 扩展插件
 *
 * 功能特性：
 * 1. 支持 implements 属性指定接口实现
 * 2. 支持 annotations 属性添加类级别注解
 * 3. 支持 lombok 集成
 * 4. 支持 JPA 注解
 * 5. 支持 Jackson 注解
 * 6. 支持 Swagger 注解
 * 7. 支持自定义字段验证注解
 * 8. 支持生成完整的业务层代码
 * 9. 支持代码模板自定义
 * 10. 支持多数据源配置
 */
public class AdvancedAllInOnePlugin extends PluginAdapter {

    // 全局配置
    private boolean addJacksonIgnoreProperties = true;
    private boolean addSerializable = true;

    // 插件配置参数
    private boolean generateService = true;
    private boolean generateController = true;
    private boolean enableLombok = false;
    private boolean enableJPA = false;
    private boolean enableSwagger = false;
    private boolean enableAuditing = false;



    // === 新增：代码生成目录 / 包配置 ===
    private String apiTargetProject;        // xxx-user-api/src/main/java
    private String serviceTargetProject;    // xxx-user-service/src/main/java
    private String mapperXmlTargetProject;  // xxx-user-service/src/main/resources

    private String providerPackage;         // com.xxx.user.provider
    private String voPackage;              // com.xxx.user.vo
    private String servicePackage;         // com.xxx.user.service
    private String controllerPackage;
    private String mapperPackage;          // com.xxx.user.mapper

    // mybatis-mapper/user 这一层目录
    private String mapperXmlSubDir;        // mybatis-mapper/user 或 mybatis-mapper/game

    // 表级别敏感字段配置 - key: tableName, value: 敏感字段列表
    private final Map<String, Set<String>> tableSensitiveFields = new HashMap<>();


    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);

        // 读取全局配置
        if (properties.containsKey("addJacksonIgnoreProperties")) {
            this.addJacksonIgnoreProperties = Boolean.parseBoolean(properties.getProperty("addJacksonIgnoreProperties"));
        }

        // 基本配置
        this.generateService = Boolean.parseBoolean(properties.getProperty("generateService", "true"));
        this.generateController = Boolean.parseBoolean(properties.getProperty("generateController", "true"));

        // 框架集成配置
        this.enableLombok = Boolean.parseBoolean(properties.getProperty("enableLombok", "false"));
        this.enableJPA = Boolean.parseBoolean(properties.getProperty("enableJPA", "false"));
        this.enableSwagger = Boolean.parseBoolean(properties.getProperty("enableSwagger", "false"));
        this.enableAuditing = Boolean.parseBoolean(properties.getProperty("enableAuditing", "false"));

        // 目标工程目录
        this.apiTargetProject = properties.getProperty("apiTargetProject", "");
        this.serviceTargetProject = properties.getProperty("serviceTargetProject", "");
        this.mapperXmlTargetProject = properties.getProperty("mapperXmlTargetProject", "");

        // 包名
        this.providerPackage = properties.getProperty("providerPackage", "");
        this.voPackage = properties.getProperty("voPackage", "");
        this.servicePackage = properties.getProperty("servicePackage", "");
        this.mapperPackage = properties.getProperty("mapperPackage", "");
        this.controllerPackage = properties.getProperty("controllerPackage", "");

        // xml 子目录（mybatis-mapper/user）
        this.mapperXmlSubDir = properties.getProperty("mapperXmlSubDir", "");

    }

    @Override
    public boolean validate(List<String> warnings) {
        boolean valid = true;

        if (generateService && StringUtility.stringHasValue(servicePackage)) {
            if (!isValidPackageName(servicePackage)) {
                warnings.add("Invalid service package name: " + servicePackage);
                valid = false;
            }
        }

        if (generateController && StringUtility.stringHasValue(controllerPackage)) {
            if (!isValidPackageName(controllerPackage)) {
                warnings.add("Invalid controller package name: " + controllerPackage);
                valid = false;
            }
        }

        return valid;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        super.initialized(introspectedTable);

        // 读取表级别的敏感字段配置
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();

        // 从 table 配置中读取敏感字段
        String sensitiveFields = introspectedTable.getTableConfiguration().getProperty("sensitiveFields");
        if (sensitiveFields != null && !sensitiveFields.trim().isEmpty()) {
            Set<String> fields;
            // 清理空格
            fields = new HashSet<>();
            for (String field : sensitiveFields.split(",")) {
                fields.add(field.trim());
            }
            tableSensitiveFields.put(tableName, fields);
        }

    }

    /**
     * 验证包名格式
     */
    private boolean isValidPackageName(String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches("^[a-z][a-z0-9_]*(\\.[a-z][a-z0-9_]*)*$", packageName);
    }

    /**
     * 获取字段注释
     */
    private String getFieldComment(IntrospectedColumn introspectedColumn) {
        String remarks = introspectedColumn.getRemarks();
        return StringUtility.stringHasValue(remarks) ? remarks : introspectedColumn.getJavaProperty();
    }

    /**
     * 获取表注释
     */
    private String getTableComment(IntrospectedTable introspectedTable) {
        String remarks = introspectedTable.getRemarks();
        return StringUtility.stringHasValue(remarks) ? remarks : introspectedTable.getTableConfiguration().getDomainObjectName();
    }

    /**
     * 添加表注释注解
     */
    private void addTableCommentAnnotation(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String tableComment = getTableComment(introspectedTable);
        if (StringUtility.stringHasValue(tableComment)) {
            // 添加 Swagger 注解
            if (enableSwagger) {
                topLevelClass.addImportedType("io.swagger.v3.oas.annotations.media.Schema");
                topLevelClass.addAnnotation("@Schema(description = \"" + tableComment + "\")");
            }
            
            // 添加 Javadoc 注释
            topLevelClass.addJavaDocLine("/**");
            topLevelClass.addJavaDocLine(" * " + tableComment);
            topLevelClass.addJavaDocLine(" * ");
            topLevelClass.addJavaDocLine(" * @author MyBatis Generator");
            //topLevelClass.addJavaDocLine(" * @date " + new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
            topLevelClass.addJavaDocLine(" */");
        }
    }

    /**
     * 添加字段注释注解
     */
    private void addFieldCommentAnnotation(Field field, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, TopLevelClass topLevelClass) {

        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        String fieldName = field.getName();
        String columnName = introspectedColumn.getActualColumnName();

        // === 检查是否为敏感字段 ===
        boolean isSensitiveField = false;
        if (tableSensitiveFields.containsKey(tableName)) {
            Set<String> sensitiveFields = tableSensitiveFields.get(tableName);
            // 支持字段名和列名两种匹配方式
            isSensitiveField = sensitiveFields.contains(fieldName) ||
                    sensitiveFields.contains(columnName);
        }

        String fieldComment = getFieldComment(introspectedColumn);
        if (StringUtility.stringHasValue(fieldComment)) {
            // 添加 Swagger 注解
            if (enableSwagger) {
                topLevelClass.addImportedType("io.swagger.v3.oas.annotations.media.Schema");
                StringBuilder schemaAnnotation = new StringBuilder("@Schema(description = \"");
                schemaAnnotation.append(fieldComment).append("\"");
                if (!introspectedColumn.isNullable() && !introspectedColumn.isAutoIncrement()) {
                    schemaAnnotation.append(", required = true");
                }
                if (introspectedColumn.isStringColumn() && introspectedColumn.getLength() > 0) {
                    schemaAnnotation.append(", maxLength = ").append(introspectedColumn.getLength());
                }
                schemaAnnotation.append(")");
                field.addAnnotation(schemaAnnotation.toString());
            }
            
            // 添加 Javadoc 注释
            field.addJavaDocLine("/**");
            field.addJavaDocLine(" * " + fieldComment);
            
            // 添加字段类型和约束信息
            if (introspectedColumn.isStringColumn() && introspectedColumn.getLength() > 0) {
                field.addJavaDocLine(" * 最大长度: " + introspectedColumn.getLength());
            }
            if (!introspectedColumn.isNullable() && !introspectedColumn.isAutoIncrement()) {
                field.addJavaDocLine(" * 非空字段");
            }
            if (introspectedColumn.isAutoIncrement()) {
                field.addJavaDocLine(" * 自增字段");
            }
            if (introspectedColumn.isIdentity()) {
                field.addJavaDocLine(" * 主键字段");
            }

            if (isSensitiveField) {
                topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonIgnore");
                field.addAnnotation("@JsonIgnore");
                // 敏感字段也可以添加注释
                field.addJavaDocLine(" * 敏感字段，JSON序列化时忽略");
            }
            
            field.addJavaDocLine(" */");
        }
    }

    /**
     * 处理字段生成 - 添加注释注解
     */
    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass,
                                       IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable,
                                       ModelClassType modelClassType) {
        // 添加字段注释注解
        addFieldCommentAnnotation(field, introspectedColumn, introspectedTable, topLevelClass);
        return true;
    }

    /**
     * 扩展实体类生成逻辑
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {

        // 添加表注释注解
        addTableCommentAnnotation(topLevelClass, introspectedTable);

        TableConfiguration tableConfig = introspectedTable.getTableConfiguration();

        // === Jackson 注解 ===
        if (addJacksonIgnoreProperties) {
            addJacksonIgnore(topLevelClass, introspectedTable);
        }

        if (addSerializable) {
            addSerializableInterface(topLevelClass);
        }

        // 处理 implements 接口
        processImplementsInterfaces(topLevelClass, tableConfig);

        // 处理类级别注解
        processClassAnnotations(topLevelClass, tableConfig);

        // 处理框架注解
        processFrameworkAnnotations(topLevelClass, introspectedTable);

        // 添加默认功能
        if (!enableLombok) {
            addToStringMethod(topLevelClass, introspectedTable);
            addEqualsAndHashCodeMethods(topLevelClass, introspectedTable);
        } else {
            addLombokAnnotations(topLevelClass);
        }

        // === 新增：生成 API / Service / Provider / Mapper 等文件 ===
        generateBizFiles(topLevelClass, introspectedTable);

        return true;
    }

    private void generateBizFiles(TopLevelClass entityClass, IntrospectedTable introspectedTable) {
        // 实体类名，例如 User / Game
        String domainName = entityClass.getType().getShortName();
        String entityPackage = entityClass.getType().getPackageName(); // com.xxx.user.entity

        // 小驼峰 user / game
        String domainVarName = Character.toLowerCase(domainName.charAt(0)) + domainName.substring(1);

        // ==== 1. 生成 API 模块 ====
        if (StringUtility.stringHasValue(apiTargetProject)) {
            generateVoIfNotExists(domainName, entityPackage);
            generateProviderInterfaceIfNotExists(domainName, entityPackage);
        }

        // ==== 2. 生成 Service 模块 ====
        if (StringUtility.stringHasValue(serviceTargetProject)) {
            generateServiceClassIfNotExists(domainName, entityPackage);
            generateProviderImplIfNotExists(domainName, entityPackage);
            generateMapperInterfaceIfNotExists(domainName, entityPackage);
        }

        // ==== 3. 生成 Mapper XML ====
        if (StringUtility.stringHasValue(mapperXmlTargetProject)) {
            generateMapperXmlIfNotExists(domainName, introspectedTable);
        }
    }

    private void writeFileIfNotExists(String fullPath, String content) {
        try {
            java.io.File file = new java.io.File(fullPath);
            if (file.exists()) {
                // 已存在，则不覆盖
                return;
            }
            java.io.File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
                 java.io.OutputStreamWriter writer = new java.io.OutputStreamWriter(fos, java.nio.charset.StandardCharsets.UTF_8)) {
                writer.write(content);
            }
        } catch (Exception e) {
            // 打个日志即可（MBG 用的是 warnings 列表，这里简单输出）
            System.err.println("Generate file failed: " + fullPath + ", cause: " + e.getMessage());
        }
    }

    private void generateVoIfNotExists(String domainName, String entityPackage) {
        if (!StringUtility.stringHasValue(voPackage) || !StringUtility.stringHasValue(apiTargetProject)) {
            return;
        }
        String className = domainName + "VO";
        String packageDir = voPackage.replace('.', '/');
        String fullPath = apiTargetProject + "/" + packageDir + "/" + className + ".java";

        String sb = "package " + voPackage + ";\n\n" +
                "import com.fasterxml.jackson.annotation.JsonIgnoreProperties;\n" +
                "import lombok.Data;\n" +
                "import lombok.EqualsAndHashCode;\n" +
                "import lombok.ToString;\n" +
                "import " + entityPackage + "." + domainName + ";\n\n" +
                //"/**\n" +
                //" * " + domainName + " VO\n" +
                //" */\n" +
                "\n" +
                "@JsonIgnoreProperties(ignoreUnknown = true)\n" +
                "@Data\n" +
                "@EqualsAndHashCode(callSuper = true)\n" +
                "@ToString(callSuper = true)\n" +
                "public class " + className + " extends " + domainName + " {\n" +
                "}\n";

        writeFileIfNotExists(fullPath, sb);
    }

    private void generateProviderInterfaceIfNotExists(String domainName, String entityPackage) {
        if (!StringUtility.stringHasValue(providerPackage) || !StringUtility.stringHasValue(apiTargetProject)) {
            return;
        }
        String className = "I" + domainName + "Provider";
        String voClassName = domainName + "VO";

        String packageDir = providerPackage.replace('.', '/');
        String fullPath = apiTargetProject + "/" + packageDir + "/" + className + ".java";

        //String voImport = StringUtility.stringHasValue(voPackage)
        //        ? "import " + voPackage + "." + voClassName + ";\n"
        //        : "";

        String sb = "package " + providerPackage + ";\n\n" +
                "import com.xxx.base.provider.IProvider;\n"+
                "import " + entityPackage + "." + domainName + ";\n"+
                "import " + voPackage + "." + voClassName + ";\n"+
                "\n" +
                //"/**\n" +
                //" * " + domainName + " \n" +
                //" */\n" +
                "public interface " + className + " extends IProvider<" + domainName + ", " + voClassName + "> {\n\n" +
                //"    " + voClassName + " findById(Long id);\n\n" +
                //"    List<" + voClassName + "> listAll();\n\n" +
                "}\n";

        writeFileIfNotExists(fullPath, sb);
    }

    private void generateServiceClassIfNotExists(String domainName, String entityPackage) {
        if (!StringUtility.stringHasValue(servicePackage)
                || !StringUtility.stringHasValue(mapperPackage)
                || !StringUtility.stringHasValue(voPackage)
                || !StringUtility.stringHasValue(serviceTargetProject)) {
            return;
        }

        String className = domainName + "Service";
        String mapperClassName = domainName + "Mapper";
        String voClassName = domainName + "VO";

        String packageDir = servicePackage.replace('.', '/');
        String fullPath = serviceTargetProject + "/" + packageDir + "/" + className + ".java";

        String sb = "package " + servicePackage + ";\n\n" +
                "import com.xxx.base.service.BaseServiceImpl;\n" +
                "import " + entityPackage + "." + domainName + ";\n" +
                "import " + mapperPackage + "." + mapperClassName + ";\n" +
                "import " + voPackage + "." + voClassName + ";\n" +
                "import org.springframework.stereotype.Service;\n\n" +
                "@Service\n" +
                "public class " + className +
                " extends BaseServiceImpl<" +
                domainName + ", " +
                voClassName + ", " +
                mapperClassName + "> {\n\n" +
                "    final " + mapperClassName + " mapper;\n\n" +
                "    public " + className +
                "(" + mapperClassName + " mapper) {\n" +
                "        this.mapper = mapper;\n" +
                "    }\n\n" +
                "}\n";

        writeFileIfNotExists(fullPath, sb);
    }


    private void generateProviderImplIfNotExists(String domainName, String entityPackage) {
        if (!StringUtility.stringHasValue(providerPackage)
                || !StringUtility.stringHasValue(servicePackage)
                || !StringUtility.stringHasValue(serviceTargetProject)) {
            return;
        }

        String interfaceName = "I" + domainName + "Provider";
        String className = domainName + "Provider";
        String serviceClassName = domainName + "Service";
        String voClassName = domainName + "VO";

        String packageDir = providerPackage.replace('.', '/');
        String fullPath = serviceTargetProject + "/" + packageDir + "/" + className + ".java";

        String sb = "package " + providerPackage + ";\n\n" +
                "import com.baomidou.mybatisplus.core.metadata.IPage;\n" +
                "import com.baomidou.mybatisplus.extension.plugins.pagination.Page;\n" +
                "import com.xxx.base.vo.Response;\n" +
                "import " + entityPackage + "." + domainName + ";\n" +
                "import " + servicePackage + "." + serviceClassName + ";\n" +
                "import " + voPackage + "." + voClassName + ";\n" +
                "import org.apache.dubbo.config.annotation.DubboService;\n\n" +
                "import java.io.Serializable;\n" +
                "import java.util.List;\n\n" +
                "@DubboService\n" +
                "public class " + className +
                " implements " + interfaceName + " {\n\n" +
                "    final " + serviceClassName + " service;\n\n" +
                "    public " + className +
                "(" + serviceClassName + " service) {\n" +
                "        this.service = service;\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public IPage<" + voClassName + "> queryPage(Page<" + voClassName + "> pageParam, " + voClassName + " voParam) {\n" +
                "        return service.queryPage(pageParam, voParam);\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public List<" + voClassName + "> queryList(" + voClassName + " voParam) {\n" +
                "        return service.queryList(voParam);\n" +
                "    }\n\n" +


                "    public " + voClassName + " queryOne(" + voClassName + " voParam) {\n" +
                "        return service.queryOne(voParam);\n" +
                "    }\n\n" +

                "    @Override\n" +
                "    public Long queryCount(" + voClassName + " voParam) {\n" +
                "        return service.queryCount(voParam);\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public List<" + domainName + "> queryByIdList(List<? extends Serializable> idList) {\n" +
                "        return service.queryByIdList(idList);\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public " + domainName + " queryById(Serializable id) {\n" +
                "        return service.queryById(id);\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public Response create(" + domainName + " entity) {\n" +
                "        return service.create(entity);\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public Response deleteById(Serializable id) {\n" +
                "        return service.deleteById(id);\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public Response modifyById(" + domainName + " entity) {\n" +
                "        return service.modifyById(entity);\n" +
                "    }\n\n" +
                "}\n";

        writeFileIfNotExists(fullPath, sb);
    }

    private void generateMapperInterfaceIfNotExists(String domainName, String entityPackage) {
        if (!StringUtility.stringHasValue(mapperPackage) || !StringUtility.stringHasValue(serviceTargetProject)) {
            return;
        }

        String className = domainName + "Mapper";
        String packageDir = mapperPackage.replace('.', '/');
        String fullPath = serviceTargetProject + "/" + packageDir + "/" + className + ".java";

        String sb = "package " + mapperPackage + ";\n\n" +
                "import com.baomidou.mybatisplus.core.mapper.BaseMapper;\n" +
                "import " + entityPackage + "." + domainName + ";\n\n" +
                "public interface " + className +
                " extends BaseMapper<" + domainName + "> {\n\n" +
                "}\n";

        writeFileIfNotExists(fullPath, sb);
    }

    private void generateMapperXmlIfNotExists(String domainName, IntrospectedTable introspectedTable) {
        if (!StringUtility.stringHasValue(mapperXmlSubDir) || !StringUtility.stringHasValue(mapperXmlTargetProject)) {
            return;
        }
        String mapperName = domainName + "Mapper";

        String dir = mapperXmlSubDir; // 例如 mybatis-mapper/user
        String fullDir = mapperXmlTargetProject + "/" + dir;
        String fullPath = fullDir + "/" + mapperName + ".xml";

        String namespace = StringUtility.stringHasValue(mapperPackage)
                ? mapperPackage + "." + mapperName
                : mapperName;

        //String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();

        String sb = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
                "\n" +
                "<mapper namespace=\"" + namespace + "\">\n" +
                // 简单示例：selectByPrimaryKey / selectAll
                // 这里只写个模板，你可以根据 introspectedTable.getPrimaryKeyColumns() 拼真正的 SQL
                //"    <select id=\"selectAll\" resultType=\"" + introspectedTable.getBaseRecordType() + "\">\n" +
                //"        select * from " + tableName + "\n" +
                //"    </select>\n\n" +
                "</mapper>\n";

        writeFileIfNotExists(fullPath, sb);
    }

    /**
     * 处理 implements 接口
     */
    private void processImplementsInterfaces(TopLevelClass topLevelClass, TableConfiguration tableConfig) {
        String implementsInterfaces = tableConfig.getProperty("implements");
        if (StringUtility.stringHasValue(implementsInterfaces)) {
            String[] interfaces = implementsInterfaces.split(",");

            for (String interfaceName : interfaces) {
                interfaceName = interfaceName.trim();
                if (StringUtility.stringHasValue(interfaceName)) {
                    addInterface(topLevelClass, interfaceName);
                }
            }
        }
    }

    /**
     * 添加接口实现
     */
    private void addInterface(TopLevelClass topLevelClass, String interfaceName) {
        // 处理泛型接口
        if (interfaceName.contains("<") && interfaceName.contains(">")) {
            String baseInterface = interfaceName.substring(0, interfaceName.indexOf("<"));
            String genericType = interfaceName.substring(interfaceName.indexOf("<") + 1, interfaceName.indexOf(">"));

            if (baseInterface.contains(".")) {
                topLevelClass.addImportedType(new FullyQualifiedJavaType(baseInterface));
                String shortName = baseInterface.substring(baseInterface.lastIndexOf(".") + 1);
                topLevelClass.addSuperInterface(new FullyQualifiedJavaType(shortName + "<" + genericType + ">"));
            } else {
                topLevelClass.addSuperInterface(new FullyQualifiedJavaType(interfaceName));
            }
        } else {
            // 普通接口
            if (interfaceName.contains(".")) {
                topLevelClass.addImportedType(new FullyQualifiedJavaType(interfaceName));
                String shortName = interfaceName.substring(interfaceName.lastIndexOf(".") + 1);
                topLevelClass.addSuperInterface(new FullyQualifiedJavaType(shortName));
            } else {
                topLevelClass.addSuperInterface(new FullyQualifiedJavaType(interfaceName));
            }
        }
    }

    /**
     * 处理类级别注解
     */
    private void processClassAnnotations(TopLevelClass topLevelClass, TableConfiguration tableConfig) {
        String annotations = tableConfig.getProperty("annotations");
        if (StringUtility.stringHasValue(annotations)) {
            String[] annotationArray = annotations.split(",");

            for (String annotation : annotationArray) {
                annotation = annotation.trim();
                if (StringUtility.stringHasValue(annotation)) {
                    // 如果包含包名，添加导入
                    if (annotation.contains(".") && !annotation.startsWith("@")) {
                        topLevelClass.addImportedType(new FullyQualifiedJavaType(annotation));
                        String shortName = annotation.substring(annotation.lastIndexOf(".") + 1);
                        topLevelClass.addAnnotation("@" + shortName);
                    } else {
                        if (!annotation.startsWith("@")) {
                            annotation = "@" + annotation;
                        }
                        topLevelClass.addAnnotation(annotation);
                    }
                }
            }
        }
    }

    /**
     * 处理框架注解
     */
    private void processFrameworkAnnotations(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // JPA 实体注解
        if (enableJPA) {
            topLevelClass.addImportedType("javax.persistence.*");
            topLevelClass.addAnnotation("@Entity");
            topLevelClass.addAnnotation("@Table(name = \"" + introspectedTable.getFullyQualifiedTableNameAtRuntime() + "\")");

            if (enableAuditing) {
                topLevelClass.addImportedType("org.springframework.data.jpa.domain.support.AuditingEntityListener");
                topLevelClass.addAnnotation("@EntityListeners(AuditingEntityListener.class)");
            }
        }

        // Swagger 文档注解
        if (enableSwagger) {
            topLevelClass.addImportedType("io.swagger.v3.oas.annotations.media.Schema");
            String remarks = introspectedTable.getRemarks();
            if (StringUtility.stringHasValue(remarks)) {
                topLevelClass.addAnnotation("@Schema(description = \"" + remarks + "\")");
            } else {
                topLevelClass.addAnnotation("@Schema(description = \"" +
                        introspectedTable.getTableConfiguration().getDomainObjectName() + " 实体类\")");
            }
        }
    }

    /**
     * 添加 Lombok 注解
     */
    private void addLombokAnnotations(TopLevelClass topLevelClass) {
        topLevelClass.addImportedType("lombok.*");
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@Builder");
        topLevelClass.addAnnotation("@NoArgsConstructor");
        topLevelClass.addAnnotation("@AllArgsConstructor");
        topLevelClass.addAnnotation("@EqualsAndHashCode(callSuper = false)");
    }

    /**
     * 添加 Serializable 接口（非 Lombok 模式）
     */
    private void addJacksonIgnore(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonIgnoreProperties");
        topLevelClass.addAnnotation("@JsonIgnoreProperties(ignoreUnknown = true)");
    }

    /**
     * 添加 Serializable 接口（非 Lombok 模式）
     */
    private void addSerializableInterface(TopLevelClass topLevelClass) {
        topLevelClass.addImportedType(new FullyQualifiedJavaType("java.io.Serializable"));
        topLevelClass.addSuperInterface(new FullyQualifiedJavaType("Serializable"));

        Field serialVersionUID = new Field("serialVersionUID", new FullyQualifiedJavaType("long"));
        serialVersionUID.setVisibility(JavaVisibility.PRIVATE);
        serialVersionUID.setStatic(true);
        serialVersionUID.setFinal(true);
        serialVersionUID.setInitializationString("1L");
        topLevelClass.addField(serialVersionUID);
    }

    /**
     * 添加 toString 方法（非 Lombok 模式）
     */
    private void addToStringMethod(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        Method toStringMethod = new Method("toString");
        toStringMethod.setVisibility(JavaVisibility.PUBLIC);
        toStringMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
        toStringMethod.addAnnotation("@Override");

        StringBuilder sb = new StringBuilder();
        sb.append("return \"").append(topLevelClass.getType().getShortName()).append("{\" +");

        List<Field> fields = topLevelClass.getFields();
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            if (!"serialVersionUID".equals(field.getName())) {
                sb.append("\n                \"").append(field.getName()).append("=\" + ").append(field.getName());
                if (i < fields.size() - 1) {
                    sb.append(" + \", \" +");
                } else {
                    sb.append(" +");
                }
            }
        }
        sb.append("\n                '}';");

        toStringMethod.addBodyLine(sb.toString());
        topLevelClass.addMethod(toStringMethod);
    }

    /**
     * 添加 equals 和 hashCode 方法（非 Lombok 模式）
     */
    private void addEqualsAndHashCodeMethods(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.Objects"));

        // equals 方法
        Method equalsMethod = new Method("equals");
        equalsMethod.setVisibility(JavaVisibility.PUBLIC);
        equalsMethod.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        equalsMethod.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "obj"));
        equalsMethod.addAnnotation("@Override");

        equalsMethod.addBodyLine("if (this == obj) return true;");
        equalsMethod.addBodyLine("if (obj == null || getClass() != obj.getClass()) return false;");
        equalsMethod.addBodyLine(topLevelClass.getType().getShortName() + " that = (" +
                topLevelClass.getType().getShortName() + ") obj;");

        StringBuilder equalsBuilder = new StringBuilder("return ");
        List<Field> fields = topLevelClass.getFields();
        boolean first = true;
        for (Field field : fields) {
            if (!"serialVersionUID".equals(field.getName())) {
                if (!first) {
                    equalsBuilder.append(" && ");
                }
                equalsBuilder.append("Objects.equals(").append(field.getName())
                        .append(", that.").append(field.getName()).append(")");
                first = false;
            }
        }
        equalsBuilder.append(";");
        equalsMethod.addBodyLine(equalsBuilder.toString());
        topLevelClass.addMethod(equalsMethod);

        // hashCode 方法
        Method hashCodeMethod = new Method("hashCode");
        hashCodeMethod.setVisibility(JavaVisibility.PUBLIC);
        hashCodeMethod.setReturnType(FullyQualifiedJavaType.getIntInstance());
        hashCodeMethod.addAnnotation("@Override");

        StringBuilder hashBuilder = new StringBuilder("return Objects.hash(");
        first = true;
        for (Field field : fields) {
            if (!"serialVersionUID".equals(field.getName())) {
                if (!first) {
                    hashBuilder.append(", ");
                }
                hashBuilder.append(field.getName());
                first = false;
            }
        }
        hashBuilder.append(");");
        hashCodeMethod.addBodyLine(hashBuilder.toString());
        topLevelClass.addMethod(hashCodeMethod);
    }

    // === 阻止生成默认方法（因为使用 Lombok）===
    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return !enableLombok;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return !enableLombok;
    }

}