/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.gradlelab

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.file.RegularFile
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport

/**
 * Gradle Build Tool Plugin Development training exercise.
 */
class CoveragelockPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        // Create and initialize extension.
        val extension = project.extensions.create("coverageLockIn", CoverageLockInExtension::class.java)
        initializeExtension(extension, project)

        // Apply the Jacoco plugin if it isn't already applied.
        project.plugins.apply(JacocoPlugin::class.java)

        // Configure the Jacoco tasks.
        configureJacoco(project, extension)
    }

    /**
     * Initialize the extension.
     */
    private fun initializeExtension(
        extension: CoverageLockInExtension,
        project: Project
    ) {
        val defaultLockInFile : RegularFile = project.layout.projectDirectory.file("coverage_lock_in.txt")
        extension.coverageFile.convention(defaultLockInFile)
        extension.goal.convention(0.8f)
        extension.counter.convention("LINE")
        extension.onCi.convention(false)
        extension.internalCurrentCoverage.convention(
            project.providers.fileContents(extension.coverageFile)
                .asText.map { it.toFloat() }.orElse(extension.goal))
        extension.internalCurrentCoverage.disallowChanges()
    }

    /**
     * Configures the Jacoco tasks.
     */
    private fun configureJacoco(project: Project, extension: CoverageLockInExtension) {
        project.tasks.named("jacocoTestReport", JacocoReport::class.java) { task ->
            task.dependsOn("test")
            task.reports.xml.required.set(true)
            task.finalizedBy("jacocoTestCoverageVerification")
        }

        project.tasks.named("jacocoTestCoverageVerification", JacocoCoverageVerification::class.java) { task ->
            task.dependsOn("jacocoTestReport")

            task.violationRules.rule { rule ->
                rule.limit { limit ->
                    limit.counter = extension.counter.get()
                    limit.value = "COVEREDRATIO"
                    limit.minimum = extension.internalCurrentCoverage.get().toBigDecimal()
                }
            }
        }
    }
}