/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.html.ui.elementViewer.impl;

import java.util.ArrayList;
import java.util.Iterator;

public class VisualizeStyleInfoManager {
	private static VisualizeStyleInfoManager INSTANCE;

	private ArrayList<IVisualizeStyleInfoListener> listeners = new ArrayList<IVisualizeStyleInfoListener>();

	public static VisualizeStyleInfoManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new VisualizeStyleInfoManager();
		}
		return INSTANCE;
	}

	private VisualizeStyleInfoManager() {
	}

	public void addLisnter(IVisualizeStyleInfoListener listener) {
		listeners.add(listener);
	}

	public boolean removeListener(IVisualizeStyleInfoListener listener) {
		return (listeners.remove(listener));
	}

	public void fireVisualizeStyleInfoUpdate(VisualizeStyleInfo styleInfo) {
		for (Iterator<IVisualizeStyleInfoListener> i = listeners.iterator(); i
				.hasNext();) {
			(i.next()).update(styleInfo);
		}
	}

}
