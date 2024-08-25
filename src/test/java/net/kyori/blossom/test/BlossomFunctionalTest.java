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
package net.kyori.blossom.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.kyori.mammoth.test.GradleFunctionalTest;
import net.kyori.mammoth.test.GradleParameters;
import net.kyori.mammoth.test.TestVariant;
import net.kyori.mammoth.test.TestVariantResource;

@GradleFunctionalTest
@GradleParameters({"--warning-mode", "fail", "--stacktrace"}) // parameters for all variants
@TestVariant(gradleVersion = "7.6.4", maximumRuntimeVersion = 20)
@TestVariant(gradleVersion = "8.9", extraArguments = {"--configuration-cache"}) // last version with non-deprecated support for runtimes <17
@TestVariant(gradleVersion = "8.10", extraArguments = {"--configuration-cache"}, minimumRuntimeVersion = 17)
@TestVariantResource(value = "/injected-gradle-versions", optional = true, minimumRuntimeVersion = 17) // newer Gradle versions deprecate running on JDK <17, and this is only for RC's
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
public @interface BlossomFunctionalTest {

}
