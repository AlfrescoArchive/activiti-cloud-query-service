package org.activiti.cloud.services.query.elastic.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "org.activiti.cloud.services.query.elastic.model")
@ComponentScan(basePackages = {"org.activiti.cloud.services.query.elastic.model"})
public class ElasticConfiguration {

    private static Logger logger = LoggerFactory.getLogger(ElasticConfiguration.class);

    @Value("${elasticsearch.home:/Users/wzhou/Downloads/elasticsearch-5.5.0}")
    private String elasticsearchHome;

    @Value("${elasticsearch.clusterName:my-application")
    private String clusterName;

    @Bean
    public Client client() {
        try {
            final Path tmpDir = Files.createTempDirectory(Paths.get("/tmp/"),
                                                          "elasticsearch_data");
            logger.debug(tmpDir.toAbsolutePath().toString());

            final Settings elasticsearchSettings = Settings.builder()
                                                           .put("cluster.name", clusterName)
                                                           .put("client.transport.sniff", true)
                                                           .put("http.enabled", "false")
                                                           .put("path.data", tmpDir.toAbsolutePath().toString())
                                                           .put("path.home", elasticsearchHome)
                                                           .build();

            TransportClient client = new PreBuiltTransportClient(elasticsearchSettings);

            return client;

        } catch (final IOException ioex) {
            logger.error("Cannot create temp dir", ioex);
            throw new RuntimeException();
        }
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(client());
    }
}

/*@Configuration
@EnableElasticsearchRepositories(basePackages = "org.activiti.cloud.services.query.elastic.repository")
public class ElasticConfiguration {


    @Bean
    public NodeBuilder nodeBuilder() {
        return new NodeBuilder();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws IOException {
        File tmpDir = File.createTempFile("elastic", Long.toString(System.nanoTime()));
        System.out.println("Temp directory: " + tmpDir.getAbsolutePath());
        Settings.Builder elasticsearchSettings =
                Settings.settingsBuilder()
                        .put("http.enabled", "true") // 1
                        .put("index.number_of_shards", "1")
                        .put("path.data", new File(tmpDir, "data").getAbsolutePath()) // 2
                        .put("path.logs", new File(tmpDir, "logs").getAbsolutePath()) // 2
                        .put("path.work", new File(tmpDir, "work").getAbsolutePath()) // 2
                        .put("path.home", tmpDir); // 3



        return new ElasticsearchTemplate(nodeBuilder()
                .local(true)
                .settings(elasticsearchSettings.build())
                .node()
                .client());
    }
}*/
