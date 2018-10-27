/*
 * Copyright (c) 2018 NTT DATA INTELLILINK Corporation. All rights reserved.
 *
 * Hinemos (http://www.hinemos.info/)
 *
 * See the LICENSE file for licensing information.
 */

package com.clustercontrol.client.swt.widgets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.MouseTrackAdapter;

/**
 * Single-sourcing implementation for Button Widget
 * 
 * @version 5.0.0
 * @since 5.0.0
 */
public class Button extends org.eclipse.swt.widgets.Button {

	public Button(Composite parent, int style) {
		super(parent, style);
	}

	/*
	 * @see org.eclipse.swt.widgets.Control#addMouseListener(MouseListener listener)
	 */
	public void addMouseTrackListener(MouseTrackAdapter mouseTrackAdapter) {
		// Do nothing
	}
}
