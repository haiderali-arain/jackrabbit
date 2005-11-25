/*
 * Copyright 2004-2005 The Apache Software Foundation or its licensors,
 *                     as applicable.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.core.nodetype.compact;

/**
 * ParseException
 */
public class ParseException extends Exception {

    /**
     * the line number where the error occurred
     */
    private final int lineNumber;

    /**
     * the column number where the error occurred
     */
    private final int colNumber;

    /**
     * the systemid of the source that produced the error
     */
    private final String systemId;


    /**
     * Constructs a new instance of this class with <code>null</code> as its
     * detail message.
     */
    public ParseException(int lineNumber, int colNumber, String systemId) {
        super();
        this.lineNumber = lineNumber;
        this.colNumber = colNumber;
        this.systemId = systemId;
    }

    /**
     * Constructs a new instance of this class with the specified detail
     * message.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ParseException(String message, int lineNumber, int colNumber, String systemId) {
        super(message);
        this.lineNumber = lineNumber;
        this.colNumber = colNumber;
        this.systemId = systemId;
    }

    /**
     * Constructs a new instance of this class with the specified detail
     * message and root cause.
     *
     * @param message   the detail message. The detail message is saved for
     *                  later retrieval by the {@link #getMessage()} method.
     * @param rootCause root failure cause
     */
    public ParseException(String message, Throwable rootCause, int lineNumber, int colNumber, String systemId) {
        super(message, rootCause);
        this.lineNumber = lineNumber;
        this.colNumber = colNumber;
        this.systemId = systemId;
    }

    /**
     * Constructs a new instance of this class with the specified root cause.
     *
     * @param rootCause root failure cause
     */
    public ParseException(Throwable rootCause, int lineNumber, int colNumber, String systemId) {
        super(rootCause);
        this.lineNumber = lineNumber;
        this.colNumber = colNumber;
        this.systemId = systemId;
    }

    /**
     * {@inheritDoc}
     */
    public String getMessage() {
        StringBuffer b = new StringBuffer(super.getMessage());
        String delim =" (";
        if (systemId != null && !systemId.equals("")) {
            b.append(delim);
            b.append(systemId);
            delim = ", ";
        }
        if (lineNumber>=0) {
            b.append(delim);
            b.append("line ");
            b.append(lineNumber);
            delim = ", ";
        }
        if (colNumber>=0) {
            b.append(delim);
            b.append("col ");
            b.append(colNumber);
            delim = ", ";
        }
        if (delim.equals(", ")) {
            b.append(")");
        }
        return b.toString();
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return super.toString();// + " (" + systemId + ", line " + lineNumber +", col " + colNumber +")";
    }

}