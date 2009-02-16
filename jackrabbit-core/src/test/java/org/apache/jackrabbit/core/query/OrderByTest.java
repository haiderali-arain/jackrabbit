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
package org.apache.jackrabbit.core.query;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.NodeIterator;
import javax.jcr.Value;
import javax.jcr.PropertyType;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

/**
 * Tests queries with order by.
 */
public class OrderByTest extends AbstractQueryTest {

    public void testOrderByScore() throws RepositoryException {
        Node n1 = testRootNode.addNode("node1");
        Node n2 = testRootNode.addNode("node2");
        Node n3 = testRootNode.addNode("node3");

        n1.setProperty("text", "aaa");
        n1.setProperty("value", 3);
        n2.setProperty("text", "bbb");
        n2.setProperty("value", 2);
        n3.setProperty("text", "ccc");
        n3.setProperty("value", 2);

        testRootNode.save();

        String sql = "SELECT value FROM nt:unstructured WHERE " +
                "jcr:path LIKE '" + testRoot + "/%' ORDER BY jcr:score, value";
        Query q = superuser.getWorkspace().getQueryManager().createQuery(sql, Query.SQL);
        QueryResult result = q.execute();
        checkResult(result, 3);

        String xpath = "/" + testRoot + "/*[@jcr:primaryType='nt:unstructured'] order by jcr:score(), @value";
        q = superuser.getWorkspace().getQueryManager().createQuery(xpath, Query.XPATH);
        result = q.execute();
        checkResult(result, 3);
    }

    public void testChildAxisString() throws RepositoryException {
        checkChildAxis(new Value[]{getValue("a"), getValue("b"), getValue("c")});
    }

    public void testChildAxisLong() throws RepositoryException {
        checkChildAxis(new Value[]{getValue(1), getValue(2), getValue(3)});
    }

    public void testChildAxisDouble() throws RepositoryException {
        checkChildAxis(new Value[]{getValue(1.0), getValue(2.0), getValue(3.0)});
    }

    public void testChildAxisBoolean() throws RepositoryException {
        checkChildAxis(new Value[]{getValue(false), getValue(true)});
    }

    public void testChildAxisCalendar() throws RepositoryException {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.MINUTE, 1);
        Calendar c3 = Calendar.getInstance();
        c3.add(Calendar.MINUTE, 2);
        checkChildAxis(new Value[]{getValue(c1), getValue(c2), getValue(c3)});
    }

    public void testChildAxisName() throws RepositoryException {
        checkChildAxis(new Value[]{getNameValue("a"), getNameValue("b"), getNameValue("c")});
    }

    public void testChildAxisPath() throws RepositoryException {
        checkChildAxis(new Value[]{getPathValue("a"), getPathValue("b"), getPathValue("c")});
    }

    public void testChildAxisDeep() throws RepositoryException {
        Node n1 = testRootNode.addNode("node1");
        n1.addNode("a").addNode("b"); // no property
        Node n2 = testRootNode.addNode("node2");
        n2.addNode("a").addNode("b").addNode("c").setProperty("prop", "a");
        Node n3 = testRootNode.addNode("node2");
        n3.addNode("a").addNode("b").addNode("c").setProperty("prop", "b");
        testRootNode.save();

        List expected = Arrays.asList(new String[]{n1.getPath(), n2.getPath(), n3.getPath()});
        String xpath = testPath + "/* order by a/b/c/@prop";
        assertEquals(expected, collectPaths(executeQuery(xpath)));

        // descending
        Collections.reverse(expected);
        xpath = testPath + "/* order by a/b/c/@prop descending";
        assertEquals(expected, collectPaths(executeQuery(xpath)));
    }

    public void testChildAxisNoValue() throws RepositoryException {
        Node n1 = testRootNode.addNode("node1");
        n1.addNode("child").setProperty("prop", "a");
        Node n2 = testRootNode.addNode("node2");
        n2.addNode("child");
        testRootNode.save();

        List expected = Arrays.asList(new String[]{n2.getPath(), n1.getPath()});
        String xpath = testPath + "/* order by child/@prop";
        assertEquals(expected, collectPaths(executeQuery(xpath)));

        // descending
        Collections.reverse(expected);
        xpath = testPath + "/* order by child/@prop descending";
        assertEquals(expected, collectPaths(executeQuery(xpath)));

        // reverse order in content
        n1.getNode("child").getProperty("prop").remove();
        n2.getNode("child").setProperty("prop", "a");
        testRootNode.save();

        Collections.reverse(expected);
        assertEquals(expected, collectPaths(executeQuery(xpath)));
    }

    public void testChildAxisMixedTypes() throws RepositoryException {
        // when differing types are used then the class name of the type
        // is used for comparison:
        // java.lang.Double < java.lang.Integer
        checkChildAxis(new Value[]{getValue(2.0), getValue(1)});
    }

    public void disabled_testPerformance() throws RepositoryException {
        createNodes(testRootNode, 10, 4, 0, new NodeCreationCallback() {
            public void nodeCreated(Node node, int count) throws
                    RepositoryException {
                node.addNode("child").setProperty("property", "value" + count);
                // save once in a while
                if (count % 1000 == 0) {
                    superuser.save();
                    System.out.println("added " + count + " nodes so far.");
                }
            }
        });
        superuser.save();

        String xpath = testPath + "//*[child/@property] order by child/@property";
        for (int i = 0; i < 3; i++) {
            long time = System.currentTimeMillis();
            Query query = qm.createQuery(xpath, Query.XPATH);
            ((QueryImpl) query).setLimit(20);
            query.execute().getNodes().getSize();
            time = System.currentTimeMillis() - time;
            System.out.println("executed query in " + time + " ms.");
        }
    }

    //------------------------------< helper >----------------------------------

    private Value getValue(String value) throws RepositoryException {
        return superuser.getValueFactory().createValue(value);
    }

    private Value getValue(long value) throws RepositoryException {
        return superuser.getValueFactory().createValue(value);
    }

    private Value getValue(double value) throws RepositoryException {
        return superuser.getValueFactory().createValue(value);
    }

    private Value getValue(boolean value) throws RepositoryException {
        return superuser.getValueFactory().createValue(value);
    }

    private Value getValue(Calendar value) throws RepositoryException {
        return superuser.getValueFactory().createValue(value);
    }

    private Value getNameValue(String value) throws RepositoryException {
        return superuser.getValueFactory().createValue(value, PropertyType.NAME);
    }

    private Value getPathValue(String value) throws RepositoryException {
        return superuser.getValueFactory().createValue(value, PropertyType.PATH);
    }

    /**
     * Checks if order by with a relative path works on the the passed values.
     * The values are expected to be in ascending order.
     *
     * @param values the values in ascending order.
     * @throws RepositoryException if an error occurs.
     */
    private void checkChildAxis(Value[] values) throws RepositoryException {
        List expected = new ArrayList();
        for (int i = 0; i < values.length; i++) {
            Node n = testRootNode.addNode("node" + i);
            expected.add(n.getPath());
            n.addNode("child").setProperty("prop", values[i]);
        }
        testRootNode.save();

        String xpath = testPath + "/* order by child/@prop";
        assertEquals(expected, collectPaths(executeQuery(xpath)));

        // descending
        Collections.reverse(expected);
        xpath = testPath + "/* order by child/@prop descending";
        assertEquals(expected, collectPaths(executeQuery(xpath)));

        // reverse order in content
        Collections.reverse(Arrays.asList(values));
        for (int i = 0; i < values.length; i++) {
            Node child = testRootNode.getNode("node" + i).getNode("child");
            child.setProperty("prop", values[i]);
        }
        testRootNode.save();

        Collections.reverse(expected);
        assertEquals(expected, collectPaths(executeQuery(xpath)));
    }

    private static List collectPaths(QueryResult result)
            throws RepositoryException {
        List paths = new ArrayList();
        for (NodeIterator it = result.getNodes(); it.hasNext(); ) {
            paths.add(it.nextNode().getPath());
        }
        return paths;
    }

    private int createNodes(Node n, int nodesPerLevel, int levels,
                            int count, NodeCreationCallback callback)
            throws RepositoryException {
        levels--;
        for (int i = 0; i < nodesPerLevel; i++) {
            Node child = n.addNode("node" + i);
            count++;
            callback.nodeCreated(child, count);
            if (levels > 0) {
                count = createNodes(child, nodesPerLevel, levels, count, callback);
            }
        }
        return count;
    }

    private static interface NodeCreationCallback {

        public void nodeCreated(Node node, int count) throws RepositoryException;
    }
}
