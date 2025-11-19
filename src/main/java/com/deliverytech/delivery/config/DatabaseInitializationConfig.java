package com.deliverytech.delivery.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.lang.NonNull;

@Configuration
public class DatabaseInitializationConfig {

  @Bean
  public DataSourceInitializer dataSourceInitializer(@NonNull DataSource dataSource) {
    var script = new FileSystemResource("data/data.sql");
    if (!script.exists()) {
      var initializer = new DataSourceInitializer();
      initializer.setDataSource(dataSource);
      initializer.setEnabled(false);
      return initializer;
    }

    var populator = new ResourceDatabasePopulator();
    populator.addScript(script);

    var initializer = new DataSourceInitializer();
    initializer.setDataSource(dataSource);
    initializer.setDatabasePopulator(populator);
    initializer.setEnabled(true);
    return initializer;
  }
}
