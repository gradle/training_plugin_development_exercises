/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.gradlelab

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * A simple unit test for the 'com.gradlelab.greeting' plugin.
 */
class CoveragelockPluginTest {
    @Test fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("com.gradlelab.coveragelock")

        // Verify the result
        assertNotNull(project.tasks.findByName("greeting"))
    }
}
