/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.carbondata.core.datastorage.store.compression.type;

import java.nio.ByteBuffer;

import org.carbondata.common.logging.LogService;
import org.carbondata.common.logging.LogServiceFactory;
import org.carbondata.core.datastorage.store.compression.Compressor;
import org.carbondata.core.datastorage.store.compression.SnappyCompression;
import org.carbondata.core.datastorage.store.compression.ValueCompressonHolder;
import org.carbondata.core.datastorage.store.compression.ValueCompressonHolder.UnCompressValue;
import org.carbondata.core.datastorage.store.dataholder.MolapReadDataHolder;
import org.carbondata.core.util.MolapCoreLogEvent;
import org.carbondata.core.util.ValueCompressionUtil;
import org.carbondata.core.util.ValueCompressionUtil.DataType;

public class UnCompressNonDecimalMaxMinInt implements UnCompressValue<int[]> {
    /**
     * Attribute for Molap LOGGER
     */
    private static final LogService LOGGER =
            LogServiceFactory.getLogService(UnCompressNonDecimalMaxMinInt.class.getName());
    /**
     * intCompressor.
     */
    private static Compressor<int[]> intCompressor =
            SnappyCompression.SnappyIntCompression.INSTANCE;
    /**
     * value.
     */
    private int[] value;

    @Override
    public void setValue(int[] value) {
        this.value = value;

    }

    @Override
    public UnCompressValue getNew() {
        try {
            return (UnCompressValue) clone();
        } catch (CloneNotSupportedException ex1) {
            LOGGER.error(MolapCoreLogEvent.UNIBI_MOLAPCORE_MSG, ex1, ex1.getMessage());
        }
        return null;
    }

    @Override
    public UnCompressValue compress() {

        UnCompressNonDecimalMaxMinByte byte1 = new UnCompressNonDecimalMaxMinByte();
        byte1.setValue(intCompressor.compress(value));
        return byte1;

    }

    @Override
    public byte[] getBackArrayData() {
        return ValueCompressionUtil.convertToBytes(value);
    }

    @Override
    public void setValueInBytes(byte[] value) {
        ByteBuffer buffer = ByteBuffer.wrap(value);
        this.value = ValueCompressionUtil.convertToIntArray(buffer, value.length);
    }

    /**
     * @see ValueCompressonHolder.UnCompressValue#getCompressorObject()
     */
    @Override
    public UnCompressValue getCompressorObject() {
        return new UnCompressNonDecimalMaxMinByte();
    }

    @Override
    public MolapReadDataHolder getValues(int decimal, Object maxValueObject) {
        double maxValue = (double) maxValueObject;
        double[] vals = new double[value.length];
        MolapReadDataHolder dataHolderInfo = new MolapReadDataHolder();
        for (int i = 0; i < vals.length; i++) {
            vals[i] = value[i] / Math.pow(10, decimal);

            if (value[i] == 0) {
                vals[i] = maxValue;
            } else {
                vals[i] = (maxValue - value[i]) / Math.pow(10, decimal);
            }

        }
        dataHolderInfo.setReadableDoubleValues(vals);
        return dataHolderInfo;
    }

    @Override
    public UnCompressValue uncompress(DataType dataType) {
        return null;
    }

}
