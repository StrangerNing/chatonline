package com.chatonline.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create date 2018/8/29
 * <p>IBatis代码自动生成</p>
 *
 * @author 李志恒(zhiheng.li@ucarinc.com)
 * @since 1.0.
 */
public class IBatisGenerator {

    /**
     * ********************************   配置区  ***********************************
     */
    // 根据自己的实际路径修改（填写绝对路径）
    private static final String BASE_PATH = System.getProperty("user.dir") + "/ampots-os/src/main/java/";
    private static final String xml_base_path = System.getProperty("user.dir") + "/ampots-os/src/main/resources/";

    /**
     * 模块和表的映射
     */
    private static Map<String, String[]> moduleTableMapper;

    static {
        //配置模块和表的映射
        moduleTableMapper = new HashMap<>(8);
        moduleTableMapper.put("gps", new String[]{"t_task_gps", "t_task_gps_info", "t_task_gps_history"});
//        moduleTableMapper.put("order", new String[]{"t_fiveplus_order", "t_fiveplus_order_detail", "t_fiveplus_shopcart"});
//        moduleTableMapper.put("message", new String[]{"t_fiveplus_message", "t_fiveplus_msg_content"});
//        moduleTableMapper.put("log", new String[]{"t_fiveplus_log"});
//        moduleTableMapper.put("product", new String[]{"t_fiveplus_product", "t_fiveplus_product_category", "t_fiveplus_product_detail"});
//        moduleTableMapper.put("setting", new String[]{"t_fiveplus_message_content", "t_fiveplus_message_template", "t_fiveplus_parameter"});
//        moduleTableMapper.put("user", new String[]{"t_fiveplus_address", "t_fiveplus_area", "t_fiveplus_city", "t_fiveplus_province", "t_fiveplus_score", "t_fiveplus_user", "t_fiveplus_vip"});
//        moduleTableMapper.put("user", new String[]{"t_fiveplus_address", "t_fiveplus_area", "t_fiveplus_city", "t_fiveplus_province", "t_fiveplus_score", "t_fiveplus_user", "t_fiveplus_vip"});
//        moduleTableMapper.put("report", new String[]{"t_fiveplus_order_daily_report"});
//        moduleTableMapper.put("scheduler", new String[]{"t_fiveplus_schedule"});
    }
    /**
     * ********************************   非配置区  ***********************************
     */

    /**
     * 数据库链接配置
     */
    private final String driverName = "com.mysql.jdbc.Driver";
    private final String user = "root";
    private final String password = "123456";
    private final String url = "jdbc:mysql://10.112.11.24:3306/ampots?characterEncoding=utf8";

    // 表生成实体后所在的包
    private static String bean_package;
    // dao和impl生成后所在的包
    private static String dao_package;
    // entity_sql所在的资源路径
    private static String entity_path;
    // 生成基础sql所在的路径 xml_base_path + entity_path
    private static String xml_path;
    // 扩展sql所在的路径 xml_base_path + extend_path
    private static String xml_extend_path;
    // 如果文件已存在，是否覆盖
    private boolean overWrite = true;
    // bean相对路径
    private static String bean_path;
    // mapper的相对路径
    private static String mapper_path;
    // Dao基类所在的包
    private static final String BASE_DAO_PACKAGE = "com.amp.admin.nc.base.dao";
    // DaoImpl基类所在的包
    private static final String BASE_DAO_IMPL_PACKAGE = "com.amp.admin.nc.base.dao.impl";

    //实体基类名称
    private static final String BASE_MODEL = "BaseModel";
    //BaseDao
    private static final String BASE_DAO = "BaseDao";
    //BaseDaoImpl
    private static final String BASE_DAO_IMPL = "BaseDaoImpl";
    //扩展mapperxml所在的命名空间
    private static String mapper_ext_namespace;

    /**
     * 表名
     */
    private String tableName = "";

    /**
     * 表前缀
     */
    private String tablePre = "t_task_";

    /**
     * 中间表前缀
     */
    private String relation_tablePre = tablePre + "r_";

    /**
     * 生成的bean名称
     */
    private String beanName = null;

    /**
     * 实体后缀
     */
    private String beanSuffix = "";

    private String mapperName = null;


    private String[] ignore_fields = new String[]{
            "id",
            "create_emp_id",
            "create_emp_name",
            "create_account_id",
            "create_account",
            "create_dept_id",
            "create_dept_name",
            "create_time",
            "modify_emp_id",
            "modify_emp_name",
            "modify_account_id",
            "modify_account",
            "modify_time",
            "version",
            "is_deleted",
            "remark"};

    private Connection conn = null;

    private final static Map<String, String> dbTypes = new HashMap<>();

    static {
        dbTypes.put("char", "String");
        dbTypes.put("varchar", "String");
        dbTypes.put("text", "String");
        dbTypes.put("bigint", "Long");
        dbTypes.put("double", "Double");
        dbTypes.put("float", "Double");
        dbTypes.put("int", "Integer");
        dbTypes.put("tinyint", "Integer");
        dbTypes.put("date", "java.util.Date");
        dbTypes.put("timestamp", "java.util.Date");
        dbTypes.put("datetime", "java.util.Date");
        dbTypes.put("bit", "Boolean");
        dbTypes.put("decimal", "java.math.BigDecimal");
        dbTypes.put("blob", "byte[]");
    }

    public static void main(String[] args) {
        try {
            IBatisGenerator generator = new IBatisGenerator();
            for (Map.Entry<String, String[]> entry : moduleTableMapper.entrySet()) {
                String module = entry.getKey();
                String[] tables = entry.getValue();
                init(module);
                for (String table : tables) {
                    generator.tableName = table;
                    generator.generateForDatabase();
                }
            }

            //所有表
//            generator.tableName = null;
//            generator.generateForDatabase();
            // 自动打开生成文件的目录
//            Runtime.getRuntime().exec("cmd /c start explorer E:\\");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 包名、路径根据模块初始化
     *
     * @param module
     */
    private static void init(String module) {
        bean_package = String.format("com.amp.ampots.module.%s.model.po", module);
        dao_package = String.format("com.amp.ampots.module.%s.dao", module);
        entity_path = String.format("com/amp/ampots/sql/%s", module);
        xml_path = xml_base_path + entity_path;
        bean_path = BASE_PATH + (bean_package.replace(".", "/"));
        mapper_path = BASE_PATH + (dao_package.replace(".", "/"));
        mapper_ext_namespace = dao_package + ".ext";
    }

    /**
     * 获取所有的表
     *
     * @return
     * @throws SQLException
     */
    private List<String> getTables() throws SQLException {
        List<String> tables = new ArrayList<String>();
        if (tableName != null) {
            tables.add(tableName);
            return tables;
        }
        PreparedStatement pstate = conn.prepareStatement("SHOW TABLES");
        ResultSet results = pstate.executeQuery();
        while (results.next()) {
            String tableName = results.getString(1);
            tables.add(tableName);
        }
        return tables;
    }

    /**
     * @Description 处理获取到的这张表名称，转为为驼峰命名
     */
    private void processTableName(String table) {
        StringBuffer sb = new StringBuffer(table.length());
//        String tableNew = table.toLowerCase();//转换为小写
        String tableNew = getTableName(table.toLowerCase());

        String[] tables = tableNew.split("_");
        String temp;
        for (int i = 0; i < tables.length; i++) {
            temp = tables[i].trim();
            sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
        }
        beanName = sb.toString();
        mapperName = beanName + "Dao";
    }

    /**
     * 获取表名，包含中间表的处理
     *
     * @return
     */
    private String getTableName(String table) {
        String tableNew = table.toLowerCase();//转换为小写
        if (relation_tablePre != null && relation_tablePre.length() > 0) {
            if (tableNew.startsWith(relation_tablePre)) {
                tableNew = tableNew.substring(relation_tablePre.length());
                return tableNew;
            }
        }
        if (tablePre != null && tablePre.length() > 0) {
            if (tableNew.startsWith(tablePre)) {
                tableNew = tableNew.substring(tablePre.length());
            }
        }
        return tableNew;
    }

    /**
     * @Description 处理数据库字段信息
     */
    private String processField(String field) {
        StringBuffer sb = new StringBuffer(field.length());
        String[] fields = field.split("_");
        String temp;
        sb.append(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            temp = fields[i].trim();
            sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
        }
        return sb.toString();
    }

    /**
     * @Description 将实体类名首字母改为小写
     */
    private String processResultMapId(String beanName) {
        beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
        return beanName;
    }


    /**
     * 构建类上面的注释
     */
    private BufferedWriter buildClassComment(BufferedWriter bw, String text) throws IOException {
        bw.newLine();
        bw.write("/**");
        bw.newLine();
        bw.write(" * " + text);
        bw.newLine();
        bw.write(" * ");
        bw.newLine();
        bw.write(" * @author  ");
        bw.newLine();
        bw.write(" * @date  ");
        bw.newLine();
        bw.write(" */");
        return bw;
    }


    /**
     * @param bw
     * @param description 方法描述
     * @param returnObjec 返回参数
     * @param params      入参
     * @return
     * @throws IOException
     */
    private BufferedWriter buildMethodComment(BufferedWriter bw, String description, String returnObjec, String... params) throws IOException {
        bw.newLine();
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * " + description);
        bw.newLine();
        if (null != params && params.length > 0) {
            for (String param : params) {
                bw.write("\t * @param " + param);
                bw.newLine();
            }
        }
        if (StringUtils.isNotEmpty(returnObjec)) {
            bw.write("\t * @return " + returnObjec);
            bw.newLine();
        }
        bw.write("\t */");
        return bw;
    }


    /**
     * @Description 根据表的字段名称、字段类型、字段注释、表注释生成实体类
     */
    private void buildEntityBean(List<String> columns, List<String> types, List<String> comments, String tableComment)
            throws IOException {

        File folder = new File(bean_path);
        //文件夹不存在则创建文件夹
        if (!folder.exists()) {
            folder.mkdirs();
        }

        beanName += beanSuffix;

        File beanFile = new File(bean_path, beanName + ".java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(beanFile)));
        bw.write("package " + bean_package + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import com.amp.ampots.base.model.BaseModel;");
        bw.newLine();
        bw.write("import lombok.AllArgsConstructor;");
        bw.newLine();
        bw.write("import lombok.Builder;");
        bw.newLine();
        bw.write("import lombok.Data;");
        bw.newLine();
        bw.write("import lombok.NoArgsConstructor;");
        bw.newLine();
        bw = buildClassComment(bw, tableComment);
        bw.newLine();
        //lombok注解
        bw.write("@Data");
        bw.newLine();
        bw.write("@Builder");
        bw.newLine();
        bw.write("@AllArgsConstructor");
        bw.newLine();
        bw.write("@NoArgsConstructor");
        bw.newLine();
        bw.write("public class " + beanName + " extends " + BASE_MODEL + " {");
        bw.newLine();
        bw.newLine();
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            String columnName = columns.get(i);
            if (ArrayUtils.contains(ignore_fields, columnName)) {
                //忽略字段
                continue;
            }
            bw.write("\t/**");
            bw.newLine();
            bw.write("\t * " + comments.get(i));
            bw.newLine();
            bw.write("\t */");
            bw.newLine();
            //处理数据类型							处理字段信息
            bw.write("\tprivate " + dbTypes.get(types.get(i)) + " " + processField(columnName) + ";");
            bw.newLine();
            bw.newLine();
        }
        bw.newLine();
        bw.write("}");
        bw.newLine();
        bw.flush();
        bw.close();
    }


    /**
     * @throws IOException
     */
    private void buildMapper() throws IOException {
        File folder = new File(mapper_path);
        //文件夹不存在则创建文件夹
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File mapperFile = new File(mapper_path, mapperName + ".java");
        if (mapperFile.exists() && !overWrite) {
            return;
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile), "utf-8"));
        bw.write("package " + dao_package + ";");
        bw.newLine();
        bw.write("import " + bean_package + "." + beanName + ";");
        bw.newLine();
        bw.write("import " + BASE_DAO_PACKAGE + "." + BASE_DAO + ";");
        bw = buildClassComment(bw, mapperName + "数据库操作接口类");
        bw.newLine();
        bw.write("public interface " + mapperName + " extends " + BASE_DAO + "<" + beanName + ">" + " {");
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
        buildMapperImpl();
    }

    /**
     * @Description 构建Mapper文件
     */
    private void buildMapperImpl() throws IOException {
        File folder = new File(mapper_path + "/impl");
        //文件夹不存在则创建文件夹
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File mapperFile = new File(mapper_path + "/impl", mapperName + "Impl.java");
        if (mapperFile.exists() && !overWrite) {
            return;
        }
        String mapperImplName = mapperName + "Impl";

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile), "utf-8"));
        bw.write("package " + dao_package + ".impl;");
        bw.newLine();
        bw.write("import " + bean_package + "." + beanName + ";");
        bw.newLine();
        bw.write("import " + dao_package + "." + mapperName + ";");
        bw.newLine();
        bw.write("import " + BASE_DAO_IMPL_PACKAGE + "." + BASE_DAO_IMPL + ";");
        bw.newLine();
        bw.write("import org.springframework.stereotype.Repository;");
        bw = buildClassComment(bw, mapperName + "数据库操作接口类");
        bw.newLine();
        bw.write("@Repository");
        bw.newLine();
        bw.write("public class " + mapperImplName + " extends " + BASE_DAO_IMPL + "<" + beanName + ">" + " implements " + mapperName + " {");
        bw.newLine();
        bw.write("\t private static final String NAMESPACE_MAPPER =\"" + dao_package + "." + mapperName + "\";");
        bw.newLine();
        bw.write("\t private static final String NAMESPACE_MAPPER_EXT =\"" + mapper_ext_namespace + "." + mapperName + "\";");
        bw.newLine();
        bw.newLine();
        bw.write("\t protected " + mapperImplName + "() {");
        bw.newLine();
        bw.write("\t\t super(NAMESPACE_MAPPER);");
        bw.newLine();
        bw.write("\t }");
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
    }


    /**
     * @Description 构建对应的Mybatis Xml 映射文件
     */
    private void buildMapperXml(List<String> columns, List<String> types) throws IOException {
        File folder = new File(xml_path);
        //文件夹不存在则创建文件夹
        if (!folder.exists()) {
            folder.mkdirs();
        }
        StringBuilder fileMapperName = new StringBuilder();
        fileMapperName.append(beanName.substring(0, 1).toLowerCase()).append(beanName.substring(1, beanName.length() - beanSuffix.length()));
        File mapperXmlFile = new File(xml_path, fileMapperName + "_sql.xml");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperXmlFile)));
        //基础信息
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        bw.newLine();
        bw.write("<!DOCTYPE sqlMap PUBLIC \"-//ibatis.apache.org//DTD SQL Map 2.0//EN\" \"http://ibatis.apache.org/dtd/sql-map-2.dtd\" >");
        bw.newLine();
        bw.write("<sqlMap namespace=\"" + dao_package + "." + mapperName + "\">");
        bw.newLine();
        buildSQL(bw, columns, types);
        bw.newLine();
        bw.write("</sqlMap>");
        bw.flush();
        bw.close();
    }

    /**
     * @Description 构建对应的Mybatis Xml 扩展映射文件
     */
    private void buildExtMapperXml() throws IOException {
        File folder = new File(xml_extend_path);
        //文件夹不存在则创建文件夹
        if (!folder.exists()) {
            folder.mkdirs();
        }
        StringBuilder fileMapperName = new StringBuilder();
        fileMapperName.append(beanName.substring(0, 1).toLowerCase()).append(beanName.substring(1, beanName.length() - beanSuffix.length()));
        File mapperXmlFile = new File(xml_extend_path, fileMapperName + "_ext_sql.xml");
        if (mapperXmlFile.exists() && !overWrite) {
            return;
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperXmlFile)));
        //基础信息
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        bw.newLine();
        bw.write("<!DOCTYPE sqlMap PUBLIC \"-//ibatis.apache.org//DTD SQL Map 2.0//EN\" \"http://ibatis.apache.org/dtd/sql-map-2.dtd\" >");
        bw.newLine();
        bw.write("<sqlMap namespace=\"" + mapper_ext_namespace + "." + mapperName + "\">");
        bw.newLine();
        bw.write("</sqlMap>");
        bw.flush();
        bw.close();
    }

    /**
     * @Description 对应的方法SQL编写
     */
    private void buildSQL(BufferedWriter bw, List<String> columns, List<String> types) throws IOException {
        int size = columns.size();
        bw.newLine();
        String resultClass = bean_package + "." + beanName;
        // 查询 根据对象的值不为null的进行查找
        bw.write("\t<!-- 根据对象的不为null的值作为条件进行查找 -->");
        bw.newLine();
        bw.write("\t<select id=\"getEntityById\" resultClass=\"" + resultClass + "\" parameterClass=\"long\">");
        bw.newLine();
        bw.write("\t\t SELECT");
        for (int i = 0; i < size; i++) {
            bw.newLine();
            bw.write("\t\t\t s." + columns.get(i) + " AS " + this.processField(columns.get(i)));
            if (i != size - 1) {
                bw.write(",");
            }
        }
        bw.newLine();
        bw.write("\t\t FROM ");
        bw.newLine();
        bw.write("\t\t\t " + tableName + " s");
        bw.newLine();
        bw.write("\t\t WHERE");
        bw.newLine();
        bw.write("\t\t\t s." + columns.get(0) + "=#" + this.processField(columns.get(0)) + "#");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();

        //where条件
        bw.newLine();
        bw.write("\t<sql id=\"where_query\">");
        bw.newLine();
        for (int i = 0; i < size; i++) {
            bw.newLine();
            bw.write("\t\t <isNotEmpty property=\"" + this.processField(columns.get(i)) + "\" prepend=\"and\">");
            bw.newLine();
            bw.write("\t\t\t s." + columns.get(i) + " = #" + this.processField(columns.get(i)) + "#");
            bw.newLine();
            bw.write("\t\t </isNotEmpty>");
        }
        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();

        // 列表查询
        bw.write("\t<!-- 列表查询 -->");
        bw.newLine();
        bw.write("\t<select id=\"queryEntityList\" resultClass=\"" + resultClass + "\" parameterClass=\"" + resultClass + "\">");
        bw.newLine();
        bw.write("\t\t SELECT");
        for (int i = 0; i < size; i++) {
            bw.newLine();
            bw.write("\t\t s." + columns.get(i) + " AS " + this.processField(columns.get(i)));
            if (i != size - 1) {
                bw.write(",");
            }
        }
        bw.newLine();
        bw.write("\t\t FROM " + tableName + " s");
        bw.newLine();
        bw.write("\t\t WHERE 1=1");
        bw.newLine();
        bw.write("\t\t <include refid=\"where_query\"/>");
        bw.newLine();
        bw.write("\t\t <!--  <isEmpty property=\"queryType\"> order by s.id DESC </isEmpty>  -->");
        bw.newLine();
        bw.write("\t\t <!--  <isNotEmpty property=\"rows\" prepend=\" \"> limit #start#,#rows#</isNotEmpty>/>  -->");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();

        // 条数查询
        bw.write("\t<!-- 查找数据总记录数 -->");
        bw.newLine();
        bw.write("\t<select id=\"queryEntityCount\" resultClass=\"java.lang.Integer\" parameterClass=\"" + resultClass + "\">");
        bw.newLine();
        bw.write("\t\t SELECT count(1)");
        bw.newLine();
        bw.write("\t\t FROM " + tableName + " s");
        bw.newLine();
        bw.write("\t\t WHERE 1=1");
        bw.newLine();
        bw.write("\t\t <include refid=\"where_query\"/>");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();

        bw.write("\t<!-- 插入 -->");
        bw.newLine();
        bw.write("\t<insert id=\"insertEntity\" parameterClass=\"" + resultClass + "\">");
        bw.newLine();
        bw.write("\t\t insert into " + tableName);
        bw.newLine();
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                bw.write("\t\t\t (");
            }
            bw.write(columns.get(i));
            if (i != size - 1) {
                bw.write(", ");
            } else {
                bw.write(")");
            }
        }
        bw.write("\t\t values (");
        for (int i = 0; i < size; i++) {
            bw.write("#" + this.processField(columns.get(i)) + "#");
            if (i != size - 1) {
                bw.write(",");
            }
        }
        bw.write(")");
        bw.newLine();
        bw.write("\t\t<selectKey keyProperty=\"" + this.processField(columns.get(0)) + "\">");
        bw.newLine();
        bw.write("\t\t\tselect LAST_INSERT_ID() AS " + this.processField(columns.get(0)));
        bw.newLine();
        bw.write("\t\t</selectKey>");
        bw.newLine();
        bw.write("\t</insert>");
        bw.newLine();
        bw.newLine();


        // 通过主键更新所有值
        bw.write("\t<!-- 通过主键更新所有值 -->");
        bw.newLine();
        bw.write("\t<update id=\"updateEntityById\" parameterClass=\"" + resultClass + "\">");
        bw.newLine();
        bw.write("\t\tupdate " + tableName + " set");
        bw.newLine();
        bw.write("\t\t");
        for (int i = 0; i < size; i++) {
            bw.write(columns.get(i) + " = #" + this.processField(columns.get(i)) + "#");
            if (i != size - 1) {
                bw.write(", ");
            }
        }
        bw.newLine();
        bw.write("\t\twhere " + columns.get(0) + "=#" + this.processField(columns.get(0)) + "#");
        bw.newLine();
        bw.write("\t</update>");
        bw.newLine();

        // 通过主键更新非空值
        bw.write("\t<!-- 通过主键更新非空值 -->");
        bw.newLine();
        bw.write("\t<update id=\"updateById\" parameterClass=\"" + resultClass + "\">");
        bw.newLine();
        bw.write("\t\tupdate " + tableName + " set id = #id#");
        for (int i = 1; i < size; i++) {
            bw.newLine();
            bw.write("\t\t<isNotEmpty property=\"" + processField(columns.get(i)) + "\" prepend=\",\">");
            bw.newLine();
            bw.write("\t\t\t" + columns.get(i) + " = #" + processField(columns.get(i)) + "#");
            bw.newLine();
            bw.write("\t\t</isNotEmpty>");
        }
        bw.newLine();
        bw.write("\t\twhere " + columns.get(0) + "=#" + this.processField(columns.get(0)) + "#");
        bw.newLine();
        bw.write("\t</update>");
        bw.newLine();
    }

    /**
     * @Description 处理数据类型，值留下类型   varchar(20) = varchar
     */
    private String processTypeNum(String type) {
        type = type.replaceAll("([0-9])", "").replace("(", "").replace(")", "").trim().toUpperCase();
        if (type.equalsIgnoreCase("int")) {
            type = "INTEGER";
        }
        if (type.equalsIgnoreCase("text")) {
            type = "VARCHAR";
        }
        return type;
    }

    /**
     * @Description 获取所有的数据库表注释
     */
    private Map<String, String> getTableComment() throws SQLException {
        Map<String, String> maps = new HashMap<String, String>();
        PreparedStatement pstate = conn.prepareStatement("SHOW TABLE STATUS");
        ResultSet results = pstate.executeQuery();
        while (results.next()) {
            String tableName = results.getString("NAME");
            String comment = results.getString("COMMENT");
            maps.put(tableName, comment);
        }
        return maps;
    }

    /**
     * 初始化数据库链接
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private void init() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        conn = DriverManager.getConnection(url, user, password);
    }

    /**
     * @Description 生成，Bean文件、Dao文件、Xml文件
     */
    public void generateForDatabase() throws ClassNotFoundException, SQLException, IOException {
        init(); //初始化数据库连接
        String prefix = "show full fields from "; //查找某张表的所有字段信息，字段名称、字段类型、字段备注
        List<String> columns;//字段名称
        List<String> types;//字段类型
        List<String> comments;//字段备注
        PreparedStatement pstate;
        List<String> tables = getTables(); //获取到数据库的所有表
        Map<String, String> tableComments = getTableComment();//获取到所有表的注释
        for (String table : tables) {
            columns = new ArrayList<>();
            types = new ArrayList<>();
            comments = new ArrayList<>();
            //查找某张表的所有字段信息，字段名称、字段类型、字段备注
            pstate = conn.prepareStatement(prefix + table);
            ResultSet results = pstate.executeQuery();
            while (results.next()) {
                columns.add(results.getString("FIELD"));
                types.add(results.getString("TYPE").replaceAll("\\(.*\\)", "").replace("unsigned", "").trim());
                comments.add(results.getString("COMMENT"));
            }
            tableName = table;
            processTableName(table);//处理获取到的这张表名称，转为为驼峰命名
            //this.outputBaseBean();
            String tableComment = tableComments.get(tableName);
            //创建对应的EntityBean
            buildEntityBean(columns, types, comments, tableComment);
//            buildMapper();//构建Mapper文件
            //构建对应的Mybatis Xml 映射文件
            buildMapperXml(columns, types);
            //构建对应的Mybatis Xml 扩展映射文件
//            buildExtMapperXml();
        }
        conn.close();
    }

    /**
     * @Description 只生成部分表的，Bean文件、Dao文件、Xml文件
     */
    public void generateForTable(List<String> tableNames) throws ClassNotFoundException, SQLException, IOException {
        init(); //初始化数据库连接
        String prefix = "show full fields from "; //查找某张表的所有字段信息，字段名称、字段类型、字段备注
        List<String> columns = null;//字段名称
        List<String> types = null;//字段类型
        List<String> comments = null;//字段备注
        PreparedStatement pstate = null;
        Map<String, String> tableComments = getTableComment();//获取到所有表的注释
        for (String table : tableNames) {
            columns = new ArrayList<String>();
            types = new ArrayList<String>();
            comments = new ArrayList<String>();
            //查找某张表的所有字段信息，字段名称、字段类型、字段备注
            pstate = conn.prepareStatement(prefix + table);
            ResultSet results = pstate.executeQuery();
            while (results.next()) {
                columns.add(results.getString("FIELD"));
                types.add(results.getString("TYPE"));
                comments.add(results.getString("COMMENT"));
            }
            tableName = table;
            processTableName(table);//处理获取到的这张表名称，转为为驼峰命名
            //this.outputBaseBean();
            String tableComment = tableComments.get(tableName);
            //创建对应的EntityBean
            buildEntityBean(columns, types, comments, tableComment);
            buildMapper();//构建Mapper文件
            //构建对应的Mybatis Xml 映射文件
            buildMapperXml(columns, types);
            //构建对应的ibatis xml 扩展文件
//            buildExtMapperXml();
        }
        conn.close();
    }

}
