package org.ss_proj.cdt;

import com.github.kklisura.cdt.protocol.types.dom.Node;
import com.github.kklisura.cdt.services.ChromeDevToolsService;

import java.util.List;

public class NodeListImpl implements org.w3c.dom.NodeList {
    private final ChromeDevToolsService service;
    private final List<Integer> nodeList;

    public NodeListImpl(ChromeDevToolsService service, List<Integer> nodeList) {
        this.service = service;
        this.nodeList = nodeList;
    }

    private static final org.w3c.dom.NodeList EMPTY_LIST = new org.w3c.dom.NodeList() {
        @Override
        public org.w3c.dom.Node item(int index) {
            return null;
        }

        @Override
        public int getLength() {
            return 0;
        }
    };

    public static org.w3c.dom.NodeList emptyList() {
        return EMPTY_LIST;
    }

    @Override
    public org.w3c.dom.Node item(int index) {
        if (index < 0 || index >= this.nodeList.size()) {
            return null;
        }

        final Integer nodeId = this.nodeList.get(index);
        if (nodeId == null) {
            return null;
        }

        final Node node = this.service.getDOM().describeNode(nodeId, null, null, null, Boolean.FALSE);

        return new NodeImpl(this.service, node);
    }

    @Override
    public int getLength() {
        return this.nodeList.size();
    }
}
