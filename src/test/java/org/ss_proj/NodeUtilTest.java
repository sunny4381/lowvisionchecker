package org.ss_proj;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void escapeSelector() {
        assertThat(NodeUtil.escapeSelector("some:id"), is("some\\:id"));
        assertThat(NodeUtil.escapeSelector("some.id"), is("some\\.id"));
    }
}