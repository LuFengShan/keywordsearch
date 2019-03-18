package com.gx.ksw.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.Map;

/**
 * freemarker模板工具类
 *
 * <p>1、配置模板实例{@link #createConfigurationInstance(String)}；<br>2、封装map数据{@link #createDataModel(Map, String, Object)}；<br>
 * 3、加载模板{@link #getTemplate(Configuration, String)}；<br>4、合并模板和数据模型{@link #mergingTemplateWithDataModel(Map, Template, String)}
 * <pre>调用示例代码：
 // 1.模板文件名称
 String templateLocation = "test.ftlh";
 // 2.生成文件的位置(xml,doc,html,htm)
 String fileLocation = "E://test.html";
 // 3.获取一个配置文件
 Configuration cfg = new FreemarkerUtil().createConfigurationInstance("/com/test");
 // 4.封装数据
 Map<String, Object> root = new HashMap<>();
 FreemarkerUtil.createDataModel(root, "user", "Big Joe");
 Product latest = new Product();
 latest.setUrl("products/greenmouse.html");
 latest.setName("green mouse");
 FreemarkerUtil.createDataModel(root, "latestProduct", latest);
 // 5.加载模板
 Template temp = FreemarkerUtil.getTemplate(cfg, templateLocation);
 // 6.合并模板和数据模型
 FreemarkerUtil.mergingTemplateWithDataModel(root, temp, fileLocation);
 *  </pre>
 *
 * @ClassName FreemarkerUtil
 * @author sgx
 * @date 2017年11月16日 下午3:58:00
 */
public class FreemarkerUtil {
    /**
     * 创建一个配置实例，我这里是用的加载类路径下面的模板
     * <p> 首先，您必须创建一个freemarker.template.Configuration实例并调整其设置。 配置实例是存储FreeMarker应用程序级别设置的中心位置。 此外，它还处理预解析模板的创建和缓存
     * @param classLocation 要加载的类路径下面
     * @return Configuration
     * @Title createConfigurationInstance
     * @author sunguangxu
     * @date 2017年11月16日 下午2:52:28
     * @version 201711-tenderA
     */
    public Configuration createConfigurationInstance(String classLocation) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_24);
        cfg.setClassForTemplateLoading(this.getClass(), classLocation);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        // cfg.setWrapUncheckedExceptions(true);
        return cfg;
    }

    /**
     * 1.合并模板和数据模型；2、指定生成文件的位置；3、关闭输出流；4、最后输出文件的位置
     *
     * @param root 数据模型
     * @param temp 模板
     * @param fileLocation 生成文件的位置
     * @return String 返回生成文件的位置
     * @Title mergingTemplateWithDataModel
     * @author sunguangxu
     * @date 2017年11月16日 下午3:20:37
     * @version 201711-tenderA
     */
    public static String mergingTemplateWithDataModel(Map<String, Object> root, Template temp, String fileLocation) {
        try {
            File outFile = new File(fileLocation);
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
            temp.process(root, out);
            out.close();
            return outFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 构建数据模型，封装到map中
     * <p>在封装数据之前，必须有个初始化代码
     * <pre>
     * Map<String, Object> root = new HashMap<>();
     * rwc.createDataModel(root, "user", "Big Joe");
     * </pre>
     * @param root 根map(把数据封装到map中)
     * @param key 键值
     * @param object 数据(各种类型)
     * @return Map<String,Object>
     * @Title createDataModel
     * @author sunguangxu
     * @date 2017年11月23日 上午9:59:06
     * @version 201711-tenderA
     */
    public static Map<String, Object> createDataModel(Map<String, Object> root, String key, Object object) {
        root.put(key, object);
        return root;
    }

    /**
     * 获取模板
     * <p>使用缓存
     * @param cfg freemarker的配置实例
     * @param templateLocation 模板位置
     * @return Template
     * @Title getTemplate
     * @author sunguangxu
     * @date 2017年11月16日 下午3:46:00
     * @version 201711-tenderA
     */
    public static Template getTemplate(Configuration cfg, String templateLocation) {
        Template temp = null;
        try {
            temp = cfg.getTemplate(templateLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
}
