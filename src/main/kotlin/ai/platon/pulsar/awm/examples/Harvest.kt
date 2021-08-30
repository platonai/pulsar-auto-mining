package ai.platon.pulsar.awm.examples

import ai.platon.pulsar.awm.common.WebHarvester
import ai.platon.pulsar.browser.driver.BrowserSettings
import ai.platon.pulsar.common.AppPaths
import ai.platon.scent.ScentEnvironment
import ai.platon.scent.context.withContext
import java.awt.Desktop
import kotlin.system.exitProcess

/**
 * Extract all possible fields from a set of product pages automatically
 * */
fun main() {
    val portalUrl = "https://www.amazon.com/gp/bestsellers/electronics/565108"
    val args = "-expires 10d -itemExpires 100d -itemRequireSize 500000 -topLinks 100 -diagnose -verboseJson"

    BrowserSettings.withGUI()

    // Fetch the portal page, find out the most important item links set automatically,
    // and then extract all fields from the item pages using machine learning, unsupervised
    WebHarvester().harvest(portalUrl, args)

    // The report directory
    val reportDir = AppPaths.getProcTmp("report/harvest/corpus").toFile()
    Desktop.getDesktop().open(reportDir)

    // The final web mining result
    val clusterResult = reportDir.resolve("last-page-tables.htm")
    Desktop.getDesktop().open(clusterResult)

    exitProcess(0)
}
