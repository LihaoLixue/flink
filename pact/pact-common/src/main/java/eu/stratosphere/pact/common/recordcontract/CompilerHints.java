/***********************************************************************************************************************
 *
 * Copyright (C) 2010 by the Stratosphere project (http://stratosphere.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/

package eu.stratosphere.pact.common.recordcontract;

/**
 * A class encapsulating compiler hints describing the behavior of the user function behind the contract.
 * If set, the optimizer will use them to estimate the sizes of the intermediate results that are processed
 * by the user functions encapsulated by the PACTs. No matter how these numbers are set, the compiler will
 * always generate a valid plan. But if these numbers are set adequately, the plans generated by the
 * PACT compiler may be significantly faster.
 * <p>
 * The numbers given by the user need not be absolutely accurate. Most importantly, they should give information whether
 * the data volume increases or decreases during the operation.
 * <p>
 * The following numbers can be declared:
 * <ul>
 * <li>The key cardinality. This describes the number of distinct keys produced by the user function. For example, if
 * the function takes records containing dates from the last three years and the functions puts the date as the key,
 * then you can set this field to roughly 1100, as that is the number of different dates. If the key cardinality is
 * unknown, this property is <code>-1</code>.</li>
 * <li>The selectivity on the key/value pairs. This factor describes how the number of keys/value pairs is changed by
 * the function encapsulated in the contract. For example, if a function declares a selectivity of 0.5, it declares that
 * it produces roughly half as many key/value pairs as it consumes. Its default value is <code>-1.0</code>, indicating
 * that the selectivity is unknown.</li>
 * <li>The average number of values per distinct key. <code>-1.0</code> by default, indicating that this value is
 * unknown.</li>
 * <li>The average number of bytes per record. <code>-1.0</code> by default, indicating that this value is unknown.</li>
 * </ul>
 * 
 * @author Stephan Ewen (stephan.ewen@tu-berlin.de)
 */
public class CompilerHints {

	private long keyCardinality = -1;

	private float avgRecordsEmittedPerStubCall = 1.0f;

	private float avgValuesPerKey = -1.0f;

	private float avgBytesPerRecord = -1.0f;

	/**
	 * Default constructor. Creates a new <tt>CompilerHints</tt> object
	 * with the default values for the encapsulated fields.
	 */
	public CompilerHints() {
	}

	/**
	 * Gets the key cardinality for the contract containing these hints..
	 * 
	 * @return The key cardinality, or -1, if unknown.
	 */
	public long getKeyCardinality() {
		return keyCardinality;
	}

	/**
	 * Sets the key cardinality for the contract containing these hints..
	 * 
	 * @param keyCardinality
	 *        The key cardinality to set.
	 */
	public void setKeyCardinality(long keyCardinality) {
		if(keyCardinality < 0) {
			throw new IllegalArgumentException("Key Cardinality must be >= 0!");
		}
		this.keyCardinality = keyCardinality;
	}

	/**
	 * Gets the average number of values per distinct key from the contract containing these hints.
	 * 
	 * @return The average number of values per key, or -1.0, if not set.
	 */
	public float getAvgNumValuesPerKey() {
		return avgValuesPerKey;
	}

	/**
	 * Sets the average number of values per distinct key for the contract containing these hints.
	 * 
	 * @param avgNumValues
	 *        The average number of values per key to set.
	 */
	public void setAvgNumValuesPerKey(float avgNumValues) {
		if(avgNumValues < 0) {
			throw new IllegalArgumentException("Average Number of Values per Key must be >= 0");
		}
		this.avgValuesPerKey = avgNumValues;
	}

	/**
	 * Gets the average number of bytes per record (key/value pair) for the
	 * contract containing these hints.
	 * 
	 * @return The average number of bytes per record, or -1.0, if unknown.
	 */
	public float getAvgBytesPerRecord() {
		return avgBytesPerRecord;
	}

	/**
	 * Sets the average number of bytes per record (key/value pair) for the
	 * contract containing these hints.
	 * 
	 * @param avgBytes
	 *        The average number of bytes per record.
	 */
	public void setAvgBytesPerRecord(float avgBytes) {
		if(avgBytes < 0) {
			throw new IllegalArgumentException("Average Bytes per Record must be  >= 0!");
		}
		this.avgBytesPerRecord = avgBytes;
	}

	/**
	 * Gets the average number of emitted records per stub call.
	 * 
	 * @return The average number of emitted records per stub call.
	 */
	public float getAvgRecordsEmittedPerStubCall() {
		return avgRecordsEmittedPerStubCall;
	}

	/**
	 * Sets the average number of emitted records per stub call. 
	 * 
	 * @param avgRecordsEmittedPerStubCall
	 *        The average number of emitted records per stub call to set.
	 */
	public void setAvgRecordsEmittedPerStubCall(float avgRecordsEmittedPerStubCall) {
		if(avgRecordsEmittedPerStubCall < 0) {
			throw new IllegalArgumentException("Average Number of Emitted Records per Stub Call must be >= 0!");
		}
		this.avgRecordsEmittedPerStubCall = avgRecordsEmittedPerStubCall;
	}

}
