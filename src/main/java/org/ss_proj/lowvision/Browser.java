package org.ss_proj.lowvision;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kklisura.cdt.protocol.commands.DOM;
import com.github.kklisura.cdt.protocol.commands.Page;
import com.github.kklisura.cdt.protocol.commands.Runtime;
import com.github.kklisura.cdt.protocol.events.runtime.ExecutionContextCreated;
import com.github.kklisura.cdt.protocol.support.types.EventHandler;
import com.github.kklisura.cdt.protocol.types.page.*;
import com.github.kklisura.cdt.protocol.types.runtime.*;
import com.github.kklisura.cdt.services.ChromeDevToolsService;
import com.google.common.io.Resources;
import org.eclipse.actf.model.ui.*;
import org.eclipse.actf.model.ui.editor.browser.ICurrentStyles;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserStyleInfo;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.ss_proj.cdt.Rect;
import org.ss_proj.cdt.Util;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Browser implements IWebBrowserACTF, IModelService {
    private final ChromeDevToolsService service;
    private final String serviceId;
    private final Runtime runtime;
    private final Page page;
    private String frameId = null;
    private Integer executionContextId = null;

    public Browser(final ChromeDevToolsService service, final String serviceId) {
        this.service = service;
        this.serviceId = serviceId;

        this.runtime = service.getRuntime();
        this.runtime.enable();
        this.runtime.onExecutionContextCreated(new EventHandler<ExecutionContextCreated>() {
            @Override
            public void onEvent(ExecutionContextCreated event) {
                setExecutionContextId(event.getContext().getId());
            }
        });

        this.page = service.getPage();
        this.page.enable();
        this.page.setLifecycleEventsEnabled(true);
    }

    @Override
    public void setFocusAddressText(boolean selectAll) {
        throw new NotImplementedException();
    }

    @Override
    public void showAddressText(boolean flag) {
        throw new NotImplementedException();
    }

    public Integer getExecutionContextId() {
        return this.executionContextId;
    }

    public void setExecutionContextId(final Integer executionContextId) {
        this.executionContextId = executionContextId;
    }

    @Override
    public void navigate(String url) {
        final Navigate navigate;
        try {
            navigate = Util.navigateAndWait(this.service, url, 10000);
        } catch (InterruptedException e) {
            throw new RuntimeException("load timeout");
        }

        this.frameId = navigate.getFrameId();
    }

    @Override
    public void goBackward() {
        final NavigationEntry entry = findNavigationEntry(-1);
        if (entry == null) {
            return;
        }

        this.page.navigateToHistoryEntry(entry.getId());
    }

    @Override
    public void goForward() {
        final NavigationEntry entry = findNavigationEntry(1);
        if (entry == null) {
            return;
        }

        this.page.navigateToHistoryEntry(entry.getId());
    }

    private NavigationEntry findNavigationEntry(final int delta) {
        NavigationHistory history = this.page.getNavigationHistory();
        int index = history.getCurrentIndex() + delta;
        if (index < 0 || index >= history.getEntries().size()) {
            return null;
        }

        return history.getEntries().get(index);
    }

    @Override
    public void navigateStop() {
        this.page.stopLoading();
    }

    @Override
    public void navigateRefresh() {
        this.page.reload();
    }

    @Override
    public int getReadyState() {
        final String readyState = String.valueOf(Util.evaluate(this.service, "document.readyState"));
        if ("complete".equals(readyState)) {
            return READYSTATE_COMPLETE;
        } else if ("loading".equals(readyState)) {
            return READYSTATE_LOADING;
        } else if ("interactive".equals(readyState)) {
            return READYSTATE_INTERACTIVE;
        }

        return READYSTATE_UNINITIALIZED;
    }

    @Override
    public boolean isReady() {
        return this.getReadyState() == READYSTATE_COMPLETE;
    }

    @Override
    public String getLocationName() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isUrlExists() {
        throw new NotImplementedException();
    }

    @Override
    public int getNavigateErrorCode() {
        throw new NotImplementedException();
    }

    @Override
    public void setWebBrowserSilent(boolean bSilent) {
        throw new NotImplementedException();
    }

    @Override
    public void setDisableScriptDebugger(boolean bDisable) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isDisableScriptDebugger() {
        throw new NotImplementedException();
    }

    @Override
    public void highlightElementById(String id) {
        throw new NotImplementedException();
    }

    @Override
    public void hightlightElementByAttribute(String name, String value) {
        throw new NotImplementedException();
    }

    @Override
    public void clearHighlight() {
        throw new NotImplementedException();
    }

    @Override
    public void setFontSize(int fontSize) {
        throw new NotImplementedException();
    }

    @Override
    public int getFontSize() {
        throw new NotImplementedException();
    }

    @Override
    public IWebBrowserStyleInfo getStyleInfo() {
        return new IWebBrowserStyleInfo() {
            @Override
            public Map<String, ICurrentStyles> getCurrentStyles() {
                return Browser.this.getCurrentStyles();
            }

            @Override
            public ModelServiceSizeInfo getSizeInfo(boolean isWhole) {
                throw new NotImplementedException();
            }

            @Override
            public RGB getUnvisitedLinkColor() {
                throw new NotImplementedException();
            }

            @Override
            public RGB getVisitedLinkColor() {
                throw new NotImplementedException();
            }
        };
    }

    public Map<String, ICurrentStyles> getCurrentStyles() {
        final String js;
        try {
            js = Resources.toString(this.getClass().getResource("getCurrentStyles.js"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Object[] array = (Object[]) Util.evaluate(this.service, js);
        HashMap<String, ICurrentStyles> ret = new HashMap<>(array.length);
        for (Object _item : array) {
            CurrentStylesImpl currentStyles = CurrentStylesImpl.convertFrom((Map<String, Object>)_item);
            ret.put(currentStyles.getXPath(), currentStyles);
        }

        return ret;
    }

    @Override
    public int getBrowserAddress() {
        throw new NotImplementedException();
    }

    @Override
    public int setTimeout(String script, int interval) {
        throw new NotImplementedException();
    }

    @Override
    public boolean clearTimeout(int id) {
        throw new NotImplementedException();
    }

    @Override
    public int setInterval(String script, int interval) {
        throw new NotImplementedException();
    }

    @Override
    public boolean clearInterval(int id) {
        throw new NotImplementedException();
    }

    @Override
    public String[] getSupportMIMETypes() {
        throw new NotImplementedException();
    }

    @Override
    public String[] getSupportExtensions() {
        throw new NotImplementedException();
    }

    @Override
    public String getCurrentMIMEType() {
        throw new NotImplementedException();
    }

    @Override
    public void open(String url) {
        this.navigate(url);
    }

    @Override
    public void open(File target) {
        throw new NotImplementedException();
    }

    @Override
    public String getURL() {
        return this.service.getDOM().getDocument().getDocumentURL();
    }

    @Override
    public String getTitle() {
        final DOM dom = this.service.getDOM();
        dom.enable();

        final Integer nodeId = dom.getDocument().getNodeId();
        final RemoteObject remoteObject = dom.resolveNode(nodeId, null, null, this.getExecutionContextId());
        if (remoteObject == null) {
            return null;
        }

        final String objectId = remoteObject.getObjectId();
        if (objectId == null) {
            return null;
        }

        final Object title = Util.getPropertyByObjectId(this.service, objectId, "title");
        this.runtime.releaseObject(objectId);

        return String.valueOf(title);
    }

    @Override
    public String getID() {
        return this.serviceId;
    }

    @Override
    public Document getDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new ByteArrayInputStream(this.getContent().getBytes()));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Document getLiveDocument() {
        return getDocument();
    }

    @Override
    public Composite getTargetComposite() {
        throw new NotImplementedException();
    }

    public String getContent() {
        final DOM dom = this.service.getDOM();
        dom.enable();

        final Integer nodeId = dom.getDocument().getNodeId();
        final RemoteObject remoteObject = dom.resolveNode(nodeId, null, null, this.getExecutionContextId());
        if (remoteObject == null) {
            return null;
        }

        final Object html = Util.getPropertyByObjectId(this.service, remoteObject.getObjectId(), "documentElement.outerHTML");
        this.runtime.releaseObject(remoteObject.getObjectId());

        return String.valueOf(html);
    }

    @Override
    public File saveOriginalDocument(String file) {
        if (file == null) {
            return null;
        }

        try (FileWriter fileWrite = new FileWriter(file)) {
            fileWrite.write(this.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new File(file);
    }

    @Override
    public File saveDocumentAsHTMLFile(String file) {
        return saveOriginalDocument(file);
    }

    @Override
    public void jumpToNode(org.w3c.dom.Node target) {
        throw new NotImplementedException();
    }

    @Override
    public IModelServiceScrollManager getScrollManager() {
        throw new NotImplementedException();
    }

    @Override
    public ImagePositionInfo[] getAllImagePosition() {
        final DOM dom = this.service.getDOM();
        dom.enable();

        final List<Integer> nodeIds = dom.querySelectorAll(dom.getDocument().getNodeId(), "img");
        if (nodeIds.size() == 0) {
            return new ImagePositionInfo[0];
        }

        final List<ImagePositionInfo> list = new ArrayList<>(nodeIds.size());
        for (Integer nodeId : nodeIds) {
            final RemoteObject remoteObject = dom.resolveNode(nodeId, null, null, null);
            if (remoteObject == null) {
                continue;
            }

            final String objectId = remoteObject.getObjectId();
            if (objectId == null) {
                continue;
            }

            Object src = Util.getPropertyByObjectId(this.service, objectId, "src");
            Rect rect = getBoundingClientRect(objectId);

            this.runtime.releaseObject(objectId);

            int x = rect.getX().intValue();
            int y = rect.getY().intValue();
            int width = rect.getWidth().intValue();
            int height = rect.getHeight().intValue();

            list.add(new ImagePositionInfo(x, y, width, height, String.valueOf(src)));
        }

        ImagePositionInfo[] result = new ImagePositionInfo[list.size()];
        list.toArray(result);
        return result;
    }

    private Rect getBoundingClientRect(final String objectId) {
        final CallFunctionOn callFunctionOn = this.runtime.callFunctionOn(
                "function() { return JSON.stringify(this.getBoundingClientRect()); }",
                objectId,
                Collections.emptyList(),
                Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null, null);

        if (callFunctionOn == null) {
            return null;
        }

        Rect rect = null;
        {
            final RemoteObject remoteObject = callFunctionOn.getResult();
            if (remoteObject != null) {
                String value = (String) remoteObject.getValue();

                try {
                    rect = new ObjectMapper().readValue(value, Rect.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        String error = null;
        final ExceptionDetails exceptionDetails = callFunctionOn.getExceptionDetails();
        if (exceptionDetails != null) {
            final RemoteObject remoteObject = exceptionDetails.getException();
            if (remoteObject != null) {
                error = remoteObject.getDescription();
            }
        }

        if (error != null) {
            throw new RuntimeException(error);
        }

        return rect;
    }

    @Override
    public IModelServiceHolder getModelServiceHolder() {
        throw new NotImplementedException();
    }

    @Override
    public Object getAttribute(String key) {
        throw new NotImplementedException();
    }

    public void saveScreenshot(final String outputFilename) throws IOException {
        Util.saveScreenshot(this.service, outputFilename);
    }

    public byte[] takeScreenshot() {
        return Util.takeScreenshot(this.service);
    }
}
