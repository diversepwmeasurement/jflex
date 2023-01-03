/*
 * Copyright (C) 2018-2020 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
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
package de.jflex.ucd_generator;

import com.google.common.base.Preconditions;
import de.jflex.ucd.UcdFileType;
import de.jflex.ucd.UcdVersion;
import de.jflex.ucd_generator.ucd.UcdVersions;
import de.jflex.version.Version;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {

  private static final String ARG_VERSION = "--version=";
  private static final String ARG_OUT = "--out=";

  /** Args: {@code --version=X file file file --version=Y file file} */
  public static void main(String[] argv) throws Exception {
    UcdGeneratorParams params = parseArgs(argv);
    if (params.ucdVersions().versionSet().isEmpty()) {
      System.out.println("No unicode version specified. Nothing to do.");
      printUsage();
      return;
    }
    System.out.println("Params " + params);
    UcdGenerator.generate(params);
  }

  private static void printUsage() {
    System.out.println("bazel run //java/de/jflex/ucd_generator:Main -- \\");
    System.out.println(
        String.format("  %s1.2.3 /absolute/path/to/ucd_1.2.3_files \\", ARG_VERSION));
    System.out.println(String.format("  %s4.5.6 /absolute/path/to/ucd_4.5.6_files", ARG_VERSION));
    System.out.println("or more simply, just run");
    System.out.println();
    System.out.println("    bazel build //java/de/jflex/ucd_generator:gen_unicode_properties");
    System.out.println();
    System.out.println("See also java/de/jflex/ucd_generator/README.md");
  }

  private static UcdGeneratorParams parseArgs(String[] argv) throws FileNotFoundException {
    UcdGeneratorParams.Builder params = UcdGeneratorParams.builder();
    UcdVersions.Builder versions = UcdVersions.builder();
    ArrayList<String> files = new ArrayList<>();
    for (int i = argv.length - 1; i >= 0; i--) {
      String arg = argv[i];
      if (arg.startsWith(ARG_VERSION)) {
        String version = arg.substring(ARG_VERSION.length());
        Preconditions.checkArgument(!version.isEmpty(), "Version cannot be empty");
        versions.put(version, UcdVersion.findUcdFiles(new Version(version), files));
        files.clear();
      }
      if (arg.startsWith(ARG_OUT)) {
        String outputDir = arg.substring(ARG_OUT.length());
        params.setOutputDir(new File(outputDir));
      } else {
        files.add(arg);
      }
    }

    Preconditions.checkArgument(
        params.outputDir() != null, "Option --%s must be specified", ARG_OUT);

    UcdVersions ucdVersions = versions.build();
    for (Version v : ucdVersions.versionSet()) {
      Preconditions.checkNotNull(
          ucdVersions.get(v).getFile(UcdFileType.UnicodeData),
          "Missing UnicodeData.txt. Try: --version=%s /path/to/UnicodeData.txt",
          v);
    }

    return params.setUcdVersions(ucdVersions).build();
  }

  private Main() {}
}
