package com.hyd.ssdb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyd.ssdb.conf.Cluster;
import com.hyd.ssdb.conf.Server;
import com.hyd.ssdb.conf.Sharding;
import com.hyd.ssdb.util.Configuration;
import com.hyd.ssdb.util.JSONUtil;
import com.hyd.ssdb.util.PropertyUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lilg1 on 2017/7/19.
 */
public class SsdbClientFactory implements FactoryBean<SsdbClient>, InitializingBean {

    /*  private static String env;

    public static String getEnv(){
        return env;
    }

    public static void setEnv(String aaa){
        env = aaa;
    }*/
    /**
     * 默认配置Ssdb客户端，无需指定主机及端口号
     *
     * @return
     */

    private  String environment;

    public  String getEnvironment() {
        return environment;
    }

    public  void setEnvironment(String env) {
        environment = env;
    }

    private SsdbClient ssdbClient;

    private static final Logger logger = LoggerFactory.getLogger(SsdbClientFactory.class);

//    public static SsdbClient getSsdbClient(String clientType) {
//        SsdbClient ssdbClient = null;
//        if (clientType == null || clientType.isEmpty())
//            clientType = "test";
//        ClientType type = (ClientType) Enum.valueOf(ClientType.class, clientType.toUpperCase());
//        switch (type) {
//            case CLUSTER:
//                ssdbClient = createSsdbClusterClient();
//                break;
//            case TEST:
//            default:
//                ssdbClient = createSsdbClient();
//                break;
//        }
//        return ssdbClient;
//    }

//    private static SsdbClient createSsdbClient() {
//
//        Properties prop = PropertyUtil.getProp();
//        if(prop.get("type").equals("server")){
//
//        }else if(prop.get("type").equals("sharding")){
//
//        }
//        String host = "";
//        int port = 111;
//        return new SsdbClient(host, port);
//    }
//
//    private static SsdbClient createSsdbClusterClient() {
//
//        Properties prop = PropertyUtil.getProp();
////        return new SsdbClient(host, port);
////
////        new SsdbClient();
////        new SsdbClient();
////        new SsdbClient();
//        return new SsdbClient(new Server());
//    }

    @Override
    public SsdbClient getObject() throws Exception {
        return ssdbClient;
    }

    @Override
    public Class<?> getObjectType() {
        return (this.ssdbClient != null ? this.ssdbClient.getClass() : SsdbClient.class);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (environment == null || environment.isEmpty()) {
            environment = "develop";
        }
        EnvEnum envEnum = Enum.valueOf(EnvEnum.class, environment.toLowerCase());
        List<Server> serverList = new ArrayList<Server>();
        String yml;
        switch (envEnum){
            case online:
                yml = Configuration.loadConfiguration("online/config/ssdb.json");
                break;
            case develop:
            default:
                yml = Configuration.loadConfiguration("develop/config/ssdb.yml");
        }
        Yaml yaml = new Yaml();
        Object obj = yaml.load(yml);
        Map map = (Map)obj;
        String type = (String)map.get("type");
        ServerEnum serverEnum = Enum.valueOf(ServerEnum.class,type.toLowerCase());
        switch(serverEnum){
            case server:
                Server server = Configuration.loadServerConfig();
                ssdbClient = new SsdbClient(server);
                break;
            case cluster:
                Cluster cluster = Configuration.loadClusterConfig();
                ssdbClient = new SsdbClient(cluster.getServers());
                break;
            case sharding:
                Sharding sharding = Configuration.loadShardingConfig();
                ssdbClient = new SsdbClient(sharding);
                break;
        }
    }

    //部署环境，每个环境一个配置文件
    private enum EnvEnum {
        develop,
        online;
    }

    //ssdb服务器拓扑类型，server(standalone单节点)，cluster(主从)，sharding(一致性hash分片)
    private enum ServerEnum{
        server,
        cluster,
        sharding;
    }
}
