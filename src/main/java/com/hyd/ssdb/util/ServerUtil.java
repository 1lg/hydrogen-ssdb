package com.hyd.ssdb.util;

import com.hyd.ssdb.conf.Cluster;
import com.hyd.ssdb.conf.Server;
import com.hyd.ssdb.conf.Sharding;
import com.hyd.ssdb.sharding.ConsistentHashSharding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lilg1 on 2017/7/19.
 */
public class ServerUtil {


    public Cluster getCluster(Properties prop){

        List<Server> serverList = new ArrayList<Server>();
//        for(prop){
//            Server server = new Server();
//            serverList.add
//        }
        Server server = new Server();
        Cluster cluster = new Cluster(server);
        return cluster;
    }

    public Sharding getSharding(Properties prop){
        List<Cluster> clusterList = new ArrayList<Cluster>();
        for(Map.Entry entry : prop.entrySet()){
            if(entry.getKey().toString().startsWith("ssdbserver")){
                String configStr = entry.getValue().toString();
                String[] configArr = configStr.split(";");
                for(String configItem : configArr){
                    if(true) {
                        configItem.startsWith("host");
                    }
                }
            }
        }
        Sharding shadring= new ConsistentHashSharding(clusterList);
        return shadring;
    }
}
