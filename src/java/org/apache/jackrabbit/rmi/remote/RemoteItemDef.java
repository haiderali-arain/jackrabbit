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
package org.apache.jackrabbit.rmi.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote version of the JCR {@link javax.jcr.nodetype.ItemDef ItemDef}
 * interface. Used by the
 * {@link org.apache.jackrabbit.rmi.server.ServerItemDef ServerItemDef} and
 * {@link org.apache.jackrabbit.rmi.client.ClientItemDef ClientItemDef}
 * adapter base classes to provide transparent RMI access to remote item
 * definitions.
 * <p>
 * The methods in this interface are documented only with a reference
 * to a corresponding ItemDef method. The remote object will simply forward
 * the method call to the underlying ItemDef instance. Argument and return
 * values, as well as possible exceptions, are copied over the network.
 * Compex {@link javax.jcr.nodetype.NodeType NodeType} return values
 * are returned as remote references to the
 * {@link org.apache.jackrabbit.rmi.remote.RemoteNodeType RemoteNodeType}
 * interface. RMI errors are signalled with RemoteExceptions.
 *
 * @author Jukka Zitting
 * @see javax.jcr.nodetype.ItemDef
 * @see org.apache.jackrabbit.rmi.client.ClientItemDef
 * @see org.apache.jackrabbit.rmi.server.ServerItemDef
 */
public interface RemoteItemDef extends Remote {

    /**
     * @see javax.jcr.nodetype.ItemDef#getDeclaringNodeType()
     * @throws RemoteException on RMI errors
     */
    RemoteNodeType getDeclaringNodeType() throws RemoteException;

    /**
     * @see javax.jcr.nodetype.ItemDef#getName()
     * @throws RemoteException on RMI errors
     */
    String getName() throws RemoteException;

    /**
     * @see javax.jcr.nodetype.ItemDef#isAutoCreate()
     * @throws RemoteException on RMI errors
     */
    boolean isAutoCreate() throws RemoteException;

    /**
     * @see javax.jcr.nodetype.ItemDef#isMandatory()
     * @throws RemoteException on RMI errors
     */
    boolean isMandatory() throws RemoteException;

    /**
     * @see javax.jcr.nodetype.ItemDef#getOnParentVersion()
     * @throws RemoteException on RMI errors
     */
    int getOnParentVersion() throws RemoteException;

    /**
     * @see javax.jcr.nodetype.ItemDef#isProtected()
     * @throws RemoteException on RMI errors
     */
    boolean isProtected() throws RemoteException;

}
