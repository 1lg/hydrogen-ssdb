package com.hyd.ssdb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by lilg1 on 2017/7/19.
 */
public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

    private static Properties prop;

    public static void setProp(Properties property){
        prop = property;
    }

    public static Properties getProp(){
        return prop;
    }

    public static void loadProperty(String path) {
        prop = new Properties();
        try {
            String configFile = path;
            //读取属性文件a.properties
            URL classPath = PropertyUtil.class.getResource("/");
            String proFilePath = classPath.toString();
            proFilePath = proFilePath.substring(6);
            proFilePath = proFilePath.replace("/", java.io.File.separator);

            if (!proFilePath.endsWith(java.io.File.separator)) {
                proFilePath = proFilePath + java.io.File.separator + configFile;
            } else {
                proFilePath = proFilePath + configFile;
            }
            InputStream in = new BufferedInputStream(new FileInputStream(proFilePath));
            prop.load(in);     ///加载属性列表
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
                System.out.println(key + ":" + prop.getProperty(key));
            }
            in.close();
            logger.info("---- ssdb configuration load finished env:{} ----",prop.get("Env"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
