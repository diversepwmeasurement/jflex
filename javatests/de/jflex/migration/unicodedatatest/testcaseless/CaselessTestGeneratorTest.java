/*
 * Copyright (C) 2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.jflex.migration.unicodedatatest.testcaseless;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.migration.unicodedatatest.testcaseless.CaselessTestGenerator.Equivalences;
import java.nio.file.Paths;
import org.junit.Test;

public class CaselessTestGeneratorTest {

  /**
   * Test that caseless equivalences have been parsed.
   *
   * <pre>
   * {@code AC00;<Hangul Syllable,First>;Lo;0;L;;;;;N;;;;;}.
   * </pre>
   */
  @Test
  public void parseUnicodeData() throws Exception {
    Equivalences<Integer> equivalences =
        CaselessTestGenerator.parseUnicodeData(Paths.get("external/ucd_7_0/UnicodeData.txt"));
    assertThat(equivalences.get(0x0041)).containsExactly(0x0041, 0x0061);
    assertThat(equivalences.get(0xAC00)).containsExactly(0xAC00);
  }
}
