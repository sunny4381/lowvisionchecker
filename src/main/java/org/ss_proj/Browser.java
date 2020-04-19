package org.ss_proj;

import io.webfolder.cdp.session.Session;
import org.eclipse.actf.model.ui.IModelService;
import org.eclipse.actf.model.ui.IModelServiceHolder;
import org.eclipse.actf.model.ui.IModelServiceScrollManager;
import org.eclipse.actf.model.ui.ImagePositionInfo;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.model.ui.editor.browser.IWebBrowserStyleInfo;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;

public class Browser implements IWebBrowserACTF, IModelService {
    private final Session session;

    public Browser(Session session) {
        this.session = session;
    }

    @Override
    public void setFocusAddressText(boolean selectAll) {
        throw new NotImplementedException();
    }

    @Override
    public void showAddressText(boolean flag) {
        throw new NotImplementedException();
    }

    @Override
    public void navigate(String url) {
        this.session.navigate(url);
        this.session.waitDocumentReady();
    }

    @Override
    public void goBackward() {
        this.session.back();
    }

    @Override
    public void goForward() {
        this.session.forward();
    }

    @Override
    public void navigateStop() {
        throw new NotImplementedException();
    }

    @Override
    public void navigateRefresh() {
        throw new NotImplementedException();
    }

    @Override
    public int getReadyState() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isReady() {
        return this.session.isDomReady();
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
        throw new NotImplementedException();
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
        throw new NotImplementedException();
    }

    @Override
    public void open(File target) {
        throw new NotImplementedException();
    }

    @Override
    public String getURL() {
        throw new NotImplementedException();
    }

    @Override
    public String getTitle() {
        throw new NotImplementedException();
    }

    @Override
    public String getID() {
        throw new NotImplementedException();
    }

    @Override
    public Document getDocument() {
        throw new NotImplementedException();
    }

    @Override
    public Document getLiveDocument() {
        throw new NotImplementedException();
    }

    @Override
    public Composite getTargetComposite() {
        throw new NotImplementedException();
    }

    @Override
    public File saveOriginalDocument(String file) {
        throw new NotImplementedException();
    }

    @Override
    public File saveDocumentAsHTMLFile(String file) {
        throw new NotImplementedException();
    }

    @Override
    public void jumpToNode(Node target) {
        throw new NotImplementedException();
    }

    @Override
    public IModelServiceScrollManager getScrollManager() {
        throw new NotImplementedException();
    }

    @Override
    public ImagePositionInfo[] getAllImagePosition() {
        throw new NotImplementedException();
    }

    @Override
    public IModelServiceHolder getModelServiceHolder() {
        throw new NotImplementedException();
    }

    @Override
    public Object getAttribute(String key) {
        throw new NotImplementedException();
    }
}
