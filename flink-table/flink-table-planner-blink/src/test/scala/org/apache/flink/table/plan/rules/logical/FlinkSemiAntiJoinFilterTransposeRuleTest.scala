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

package org.apache.flink.table.plan.rules.logical

import org.apache.flink.api.scala._
import org.apache.flink.table.api.scala._
import org.apache.flink.table.plan.optimize.program.{FlinkBatchProgram, FlinkHepRuleSetProgramBuilder, HEP_RULES_EXECUTION_TYPE}
import org.apache.flink.table.util.TableTestBase

import org.apache.calcite.plan.hep.HepMatchOrder
import org.apache.calcite.tools.RuleSets
import org.junit.{Before, Test}

/**
  * Test for [[FlinkSemiAntiJoinFilterTransposeRule]].
  */
class FlinkSemiAntiJoinFilterTransposeRuleTest extends TableTestBase {

  private val util = batchTestUtil()

  @Before
  def setup(): Unit = {
    util.buildBatchProgram(FlinkBatchProgram.DEFAULT_REWRITE)
    util.tableEnv.getConfig.getCalciteConfig.getBatchProgram.get.addLast(
      "rules",
      FlinkHepRuleSetProgramBuilder.newBuilder
        .setHepRulesExecutionType(HEP_RULES_EXECUTION_TYPE.RULE_SEQUENCE)
        .setHepMatchOrder(HepMatchOrder.BOTTOM_UP)
        .add(RuleSets.ofList(
          FlinkSemiAntiJoinProjectTransposeRule.INSTANCE,
          FlinkSemiAntiJoinFilterTransposeRule.INSTANCE))
        .build()
    )

    util.addTableSource[(Int, Long, String)]("MyTable1", 'a, 'b, 'c)
    util.addTableSource[(Int, Long, String)]("MyTable2", 'd, 'e, 'f)
  }

  @Test
  def testSemiJoinFilterTranspose(): Unit = {
    val sqlQuery =
      """
        |SELECT * FROM (SELECT * FROM MyTable1 WHERE a > 10) t
        |    WHERE b IN (SELECT e FROM MyTable2)
      """.stripMargin
    util.verifyPlan(sqlQuery)
  }

  @Test
  def testAntiJoinFilterTranspose(): Unit = {
    val sqlQuery =
      """
        |SELECT * FROM (SELECT * FROM MyTable1 WHERE a > 10) t
        |    WHERE b NOT IN (SELECT e FROM MyTable2)
      """.stripMargin
    util.verifyPlan(sqlQuery)
  }

}
