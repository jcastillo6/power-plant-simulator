package com.jcastillo.simulator

import io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME
import org.junit.jupiter.api.Tag
import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite

@Tag("integration-test")
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.jcastillo.simulator.steps")
class CucumberTest
