/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.jcr2spi.state;

import org.apache.jackrabbit.spi.ItemId;
import org.apache.jackrabbit.spi.NodeId;

/**
 * The <code>ItemStateManager</code> interface provides methods for retrieving
 * <code>ItemState</code> and <code>NodeReferences</code> instances by id.
 */
public interface ItemStateManager {

    /**
     * Returns the <code>NodeState</code> of the root node.
     *
     * @return node state of the root node.
     * @throws ItemStateException
     */
    NodeState getRootState() throws ItemStateException;

    /**
     * Return an item state, given its item id.
     *
     * @param id item id
     * @return item state
     * @throws NoSuchItemStateException if the item does not exist
     * @throws ItemStateException if an error occurs
     */
    ItemState getItemState(ItemId id) throws NoSuchItemStateException, ItemStateException;

    /**
     * Return a flag indicating whether an item state for a given
     * item id exists.
     *
     * @param id item id
     * @return <code>true</code> if an item state exists,
     *         otherwise <code>false</code>
     */
    boolean hasItemState(ItemId id);  // TODO: throw ItemStateException in case of error?

    // DIFF JR: NodeId param instead of NodeReferenceId
    /**
     * Return a node references object, given its target id.
     *
     * @param id target id
     * @return node references object
     * @throws NoSuchItemStateException if the item does not exist
     * @throws ItemStateException if an error occurs
     */
    NodeReferences getNodeReferences(NodeId id)
        throws NoSuchItemStateException, ItemStateException;

    // DIFF JR: NodeId param instead of NodeReferenceId
    /**
     * Return a flag indicating whether a node references object
     * for a given target id exists.
     *
     * @param id target id
     * @return <code>true</code> if a node reference object exists for the given
     *         id, otherwise <code>false</code>.
     */
    boolean hasNodeReferences(NodeId id);
}
