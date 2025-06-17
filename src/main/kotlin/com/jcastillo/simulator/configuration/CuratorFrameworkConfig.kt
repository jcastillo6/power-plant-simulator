package com.jcastillo.simulator.configuration

import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.ExponentialBackoffRetry
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CuratorFrameworkConfig {

    @Bean(destroyMethod = "close")
     fun curatorFramework(@Value("\${zookeeper.connection-string}") zookeeperConnection: String): CuratorFramework {
         return CuratorFrameworkFactory.newClient(
             zookeeperConnection,
             ExponentialBackoffRetry(1000, 3)
         ).apply { start() }
     }
}