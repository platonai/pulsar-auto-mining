package ai.platon.pulsar.awm.common

import ai.platon.pulsar.common.AppPaths
import ai.platon.scent.ScentContext
import ai.platon.scent.context.ScentContexts
import ai.platon.scent.context.support.DefaultClassPathXmlScentContext
import ai.platon.scent.dom.HarvestOptions
import kotlinx.coroutines.runBlocking
import java.awt.Desktop
import java.io.File
import java.nio.file.Files

open class WebHarvester(
    val context: ScentContext = ScentContexts.activate(DefaultClassPathXmlScentContext())
) {
    val session = context.createSession()

    val defaultArgs = """
        |-expires 1d -itemExpires 1d -scrollCount 6 -itemScrollCount 4
        |-nScreens 5 -polysemous -diagnose -nVerbose 1
        |-pageLoadTimeout 60s -showTip -showImage -cellType PLAIN_TEXT
        """.trimMargin()

    fun harvest(url: String) = harvest(url, defaultArgs)

    fun harvest(url: String, args: String) = harvest(url, session.options(args))

    fun harvest(url: String, options: HarvestOptions) = doHarvest(url, options)

    private fun doHarvest(url: String, options: HarvestOptions) {
        val result = runBlocking { session.harvest(url, options) }
        session.buildAll(result.tableGroup, options)

        val json = session.buildJson(result.tableGroup)
        val path = AppPaths.REPORT_DIR.resolve("harvest/corpus/last-page-tables.json")
        Files.writeString(path, json)
    }
}
