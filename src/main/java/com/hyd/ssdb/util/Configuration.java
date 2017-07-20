package com.hyd.ssdb.util;

import com.hyd.ssdb.conf.Cluster;
import com.hyd.ssdb.conf.Server;
import com.hyd.ssdb.conf.Sharding;
import com.hyd.ssdb.sharding.ConsistentHashSharding;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lilg1 on 2017/7/20.
 */
public class Configuration {

    private static Object configYml;

    private static String ssdbType;

    public static String loadConfiguration(String path) {
        FileInputStream in = null;
        String result = null;
        try {
            URL classPath = Configuration.class.getResource("/");
            String proFilePath = classPath.toString();
            proFilePath = proFilePath.substring(6);
            proFilePath = proFilePath.replace("/", java.io.File.separator);

            if (!proFilePath.endsWith(java.io.File.separator)) {
                proFilePath = proFilePath + java.io.File.separator + path;
            } else {
                proFilePath = proFilePath + path;
            }
            in = new FileInputStream(proFilePath);
            result = readStream(in);
            Yaml yml = new Yaml();
            configYml = yml.load(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getSsdbType() {
        return ssdbType;
    }

    public static Server loadServerConfig() {
        Map map = (Map) configYml;
        Map configMap = (Map) map.get("Server");
        Server server = readConfig(configMap);
        return server;
    }

    public static Cluster loadClusterConfig() {
        Map map = (Map) configYml;
        List<Map> list = (List) map.get("Cluster");
        List<Server> serverList = new ArrayList<Server>();
        for (Map serverMap : list) {
            Map configMap = (Map) serverMap.get("Server");
            serverList.add(readConfig(configMap));
        }
        return new Cluster(serverList);
    }

    public static Sharding loadShardingConfig() {
        Map map = (Map) configYml;
        List<Map> clusters = (List) map.get("Sharding");

        List<Cluster> clusterList = new ArrayList<Cluster>();
        for (Map clusterMap : clusters) {
            List<Map> servers = (List) clusterMap.get("Cluster");
            List<Server> serverList = new ArrayList<Server>();
            for (Map serverMap : servers) {
                Map configMap = (Map) serverMap.get("Server");
                serverList.add(readConfig(configMap));
            }
            Cluster cluster = new Cluster(serverList);
            clusterList.add(cluster);
        }
        Sharding sharding = new ConsistentHashSharding(clusterList);
        return sharding;
    }

    public static Server readConfig(Map serverConfig) {
        String host = (String) serverConfig.get("host");
        Integer port = (int) serverConfig.get("port");
        Integer timeout = (int) serverConfig.get("timeout");
        Integer isMaster = (int) serverConfig.get("master");
        Server server = new Server(host, port, timeout, isMaster);
        return server;
    }

    public static String readStream(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
