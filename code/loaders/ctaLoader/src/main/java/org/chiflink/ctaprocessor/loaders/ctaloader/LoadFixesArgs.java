package org.chiflink.ctaprocessor.loaders.ctaloader;

/**
 * Created by ubuntu on 2/20/17.
 */

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.ExecutionConfig;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;



@Parameters(separators="=")
public class LoadFixesArgs extends ExecutionConfig.GlobalJobParameters
        implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;


    @Parameter(names = { "--kafka-zookeeper-host" })
    public String kafkaZookeeperHost = "localhost:2181";

    @Parameter(names = { "--kafka-bootstrap-server" })
    public String kafkaBootStrapServer = "localhost:9092";

    @Parameter(names = { "--kafka-topic" })
    public String kafkaTopic = "cta01";

    @Parameter(names = { "--input-file" })
    public String inputFile = "/home/ubuntu/data/20170215.csv";

    @Override
    protected Object clone() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.valueToTree(this);
        try {
            return mapper.treeToValue(mapper.valueToTree(this), LoadFixesArgs.class);
        } catch (JsonProcessingException e) { throw new RuntimeException(); }
    }

    @Override
    public Map<String, String> toMap() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) { throw new RuntimeException(); }
    }


}
