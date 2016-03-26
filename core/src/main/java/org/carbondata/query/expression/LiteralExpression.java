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

package org.carbondata.query.expression;

import org.carbondata.query.molapfilterinterface.ExpressionType;
import org.carbondata.query.molapfilterinterface.RowIntf;

public class LiteralExpression extends LeafExpression {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Object value;
    private DataType dataType;

    public LiteralExpression(Object value, DataType dataType) {
        this.value = value;
        this.dataType = dataType;
        
        
        /*switch(dataType)
        {
        case StringType:
            this.value = value;
            break;
        case IntegerType:
            this.value = Integer.parseInt(value);
            break;
        case DoubleType:
            this.value = Double.parseDouble(value);
            break;
        default:
            break;
        }*/

    }

    @Override
    public ExpressionResult evaluate(RowIntf value) {
        ExpressionResult expressionResult = new ExpressionResult(dataType, this.value);
        return expressionResult;
    }

    @Override
    public ExpressionType getFilterExpressionType() {
        // TODO Auto-generated method stub
        return ExpressionType.LITERAL;
    }

    @Override
    public String getString() {
        // TODO Auto-generated method stub
        return "LiteralExpression(" + value + ')';
    }

}
