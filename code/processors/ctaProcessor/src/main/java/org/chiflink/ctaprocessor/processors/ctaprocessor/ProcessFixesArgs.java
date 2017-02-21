package org.chiflink.ctaprocessor.processors.ctaprocessor;

/**
 * Created by ubuntu on 2/21/17.
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
public class ProcessFixesArgs extends ExecutionConfig.GlobalJobParameters
        implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Parameter(names = { "--kafka-zookeeper-host" })
    public String kafkaZookeeperHost = "127.0.1.1:2181";

    @Parameter(names = { "--kafka-bootstrap-server" })
    public String kafkaBootStrapServer = "127.0.1.1:9092";

    @Parameter(names = { "--kafka-topic" })
    public String kafkaTopic = "cta01";

    @Override
    protected Object clone() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.valueToTree(this);
        try {
            return mapper.treeToValue(mapper.valueToTree(this), ProcessFixesArgs.class);
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
