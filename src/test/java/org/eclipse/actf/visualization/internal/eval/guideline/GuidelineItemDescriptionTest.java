package org.eclipse.actf.visualization.internal.eval.guideline;

import org.junit.Test;

import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class GuidelineItemDescriptionTest {
    @Test
    public void getDescription001() throws Exception {
        assertThat(GuidelineItemDescription.getDescription(""), isEmptyString());
    }

    @Test
    public void getDescription002() throws Exception {
        assertThat(GuidelineItemDescription.getDescription("C_1000.0"), not(isEmptyString()));
    }

    @Test
    public void getDescription003() throws Exception {
        assertThat(GuidelineItemDescription.getDescription("B_5"), not(isEmptyString()));
    }
}
