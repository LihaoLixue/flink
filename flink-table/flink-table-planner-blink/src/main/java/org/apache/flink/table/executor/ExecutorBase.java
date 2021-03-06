/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.table.executor;

import org.apache.flink.annotation.Internal;
import org.apache.flink.api.dag.Transformation;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.delegation.Executor;
import org.apache.flink.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link Executor} that is backed by a {@link StreamExecutionEnvironment}.
 */
@Internal
public abstract class ExecutorBase implements Executor {

	private static final String DEFAULT_JOB_NAME = "Flink Exec Table Job";

	private final StreamExecutionEnvironment executionEnvironment;
	protected List<Transformation<?>> transformations = new ArrayList<>();

	public ExecutorBase(StreamExecutionEnvironment executionEnvironment) {
		this.executionEnvironment = executionEnvironment;
	}

	@Override
	public void apply(List<Transformation<?>> transformations) {
		this.transformations.addAll(transformations);
	}

	public StreamExecutionEnvironment getExecutionEnvironment() {
		return executionEnvironment;
	}

	protected String getNonEmptyJobName(String jobName) {
		if (StringUtils.isNullOrWhitespaceOnly(jobName)) {
			return DEFAULT_JOB_NAME;
		} else {
			return jobName;
		}
	}
}
