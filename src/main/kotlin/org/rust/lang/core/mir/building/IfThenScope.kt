/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.lang.core.mir.building

class IfThenScope(
    val scope: Scope,
    val elseDrops: DropTree,
)
