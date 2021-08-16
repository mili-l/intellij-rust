/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.lang.core.macros.proc

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import org.rust.cargo.toolchain.RsToolchainBase
import org.rust.cargo.toolchain.wsl.RsWslToolchain
import org.rust.ide.experiments.RsExperiments
import org.rust.openapiext.isFeatureEnabled

@Service
class ProcMacroApplicationService : Disposable {

    private var sharedServerLocal: ProcMacroServerPool? = null
    private var sharedServerWsl: ProcMacroServerPool? = null

    @Synchronized
    fun getServer(toolchain: RsToolchainBase): ProcMacroServerPool? {
        if (!isEnabled()) return null

        var server = if (toolchain is RsWslToolchain) sharedServerWsl else sharedServerLocal
        if (server == null) {
            server = ProcMacroServerPool.tryCreate(toolchain, this)
            if (toolchain is RsWslToolchain) {
                sharedServerWsl = server
            } else {
                sharedServerLocal = server
            }
        }
        return server
    }

    override fun dispose() {}

    companion object {
        fun getInstance(): ProcMacroApplicationService = service()
        fun isEnabled(): Boolean = isFeatureEnabled(RsExperiments.PROC_MACROS)
            && isFeatureEnabled(RsExperiments.EVALUATE_BUILD_SCRIPTS)
    }
}
