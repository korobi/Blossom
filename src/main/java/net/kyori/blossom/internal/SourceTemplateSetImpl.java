/*
 * This file is part of blossom, licensed under the GNU Lesser General Public License.
 *
 * Copyright (c) 2023 KyoriPowered
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package net.kyori.blossom.internal;

import java.util.function.Function;
import javax.inject.Inject;
import net.kyori.blossom.GenerateTemplates;
import net.kyori.blossom.SourceTemplateSet;
import org.gradle.api.GradleException;
import org.gradle.api.file.Directory;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskProvider;
import org.jetbrains.annotations.NotNull;

public abstract class SourceTemplateSetImpl extends TemplateSetImpl implements SourceTemplateSet {
  private Function<SourceSet, SourceDirectorySet> sourceLens;

  @Inject
  public SourceTemplateSetImpl(final String name) {
    super(name);
  }

  @Override
  public Directory resolveOutputDirectory(final Directory generatedDir) {
    return generatedDir.dir("sources/blossom-" + this.getName());
  }

  @Override
  public void registerOutputWithSet(final SourceSet destination, final TaskProvider<GenerateTemplates> generateTask) {
    if (this.sourceLens == null) {
      throw new GradleException("The template set '" + this.getName() + "' in source set '" + destination.getName() + "' has not been configured for any language!");
    }

    this.sourceLens.apply(destination).srcDir(generateTask.map(GenerateTemplates::getOutputDir));
  }

  private static Function<SourceSet, SourceDirectorySet> lensForNamedExtension(final String extensionName) {
    return set -> {
      final Object extension = set.getExtensions().getByName(extensionName);
      if (!(extension instanceof SourceDirectorySet)) {
        throw new GradleException("The extension '" + extensionName + "' on source set '" + set.getName() + " was supposed to be a SourceDirectorySet, but instead it was a " + extension.getClass().getName());
      }

      return (SourceDirectorySet) extension;
    };
  }

  @Override
  public void java() {
    this.sourceLens = SourceSet::getJava;
  }

  @Override
  public void kotlin() {
    this.sourceLens = lensForNamedExtension("kotlin");
  }

  @Override
  public void namedLanguageExtension(final @NotNull String name) {
    this.sourceLens = lensForNamedExtension(name);
  }
}
