@file:Suppress("LeakingThis") // Following official Gradle guidance

package com.varabyte.kobweb.gradle.application.extensions

import com.varabyte.kobweb.gradle.application.GENERATED_ROOT
import com.varabyte.kobweb.gradle.application.SITE_ROOT
import com.varabyte.kobweb.gradle.application.RESOURCE_SUFFIX
import com.varabyte.kobweb.gradle.application.SRC_SUFFIX
import org.gradle.api.Project
import org.gradle.api.provider.Property
import java.io.File

abstract class KobwebConfig {
    /**
     * The string path to the root where generated code will be written to, relative to the project root.
     */
    abstract val genDir: Property<String>

    /**
     * The string path to the root where exported site will be created under, relative to the project root.
     */
    abstract val siteDir: Property<String>

    /**
     * The root package of all pages.
     *
     * Any composable function not under this root will be ignored, even if annotated by @Page.
     *
     * An initial '.' means this should be prefixed by the project group, e.g. ".pages" -> "com.example.pages"
     */
    abstract val pagesPackage: Property<String>

    /**
     * The path of public resources inside the project's resources folder, e.g. "public" ->
     * "src/jsMain/resources/public"
     */
    abstract val publicPath: Property<String>

    init {
        genDir.convention(GENERATED_ROOT)
        siteDir.convention(SITE_ROOT)
        pagesPackage.convention(".pages")
        publicPath.convention("public")
    }

    fun getGenSrcRoot(project: Project): File = project.layout.buildDirectory.dir("${genDir.get()}$SRC_SUFFIX").get().asFile
    fun getGenResRoot(project: Project): File = project.layout.buildDirectory.dir("${genDir.get()}$RESOURCE_SUFFIX").get().asFile

    /**
     * Given a [project], get the fully qualified packages name, e.g. ".pages" -> "org.example.pages"
     */
    fun getQualfiedPagesPackage(project: Project): String = project.prefixQualifiedPackage(pagesPackage.get())
}