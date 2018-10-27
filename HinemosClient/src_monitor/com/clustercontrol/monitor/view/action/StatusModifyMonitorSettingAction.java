/*
 * Copyright (c) 2018 NTT DATA INTELLILINK Corporation. All rights reserved.
 *
 * Hinemos (http://www.hinemos.info/)
 *
 * See the LICENSE file for licensing information.
 */

package com.clustercontrol.monitor.view.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;

import com.clustercontrol.util.WidgetTestUtil;
import com.clustercontrol.bean.HinemosModuleConstant;
import com.clustercontrol.monitor.action.GetStatusListTableDefine;
import com.clustercontrol.monitor.composite.StatusListComposite;
import com.clustercontrol.monitor.view.StatusView;
import com.clustercontrol.view.ScopeListBaseView;

/**
 * 監視[ステータス]ビューの監視設定変更ダイアログ表示アクションを行うアクライアント側アクションクラス<BR>
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class StatusModifyMonitorSettingAction extends AbstractHandler implements IElementUpdater {
	/** ログ */
	private static Log m_log = LogFactory.getLog(StatusModifyMonitorSettingAction.class);

	/** アクションID */
	public static final String ID = StatusModifyMonitorSettingAction.class.getName();

	private IWorkbenchWindow window;
	/** ビュー */
	private IWorkbenchPart viewPart;

	/**
	 * Dispose
	 */
	@Override
	public void dispose() {
		this.viewPart = null;
		this.window = null;
	}

	/**
	 * 監視[イベント]ビューの選択されたアイテムの変更用ダイアログを表示します。
	 * <p>
	 * <ol>
	 * <li>監視[イベント]ビューで、選択されているアイテムを取得します。</li>
	 * <li>取得したイベント情報に応じた変更用ダイアログを表示します。 </li>
	 * </ol>
	 *
	 * @see org.eclipse.core.commands.IHandler#execute
	 * @see com.clustercontrol.monitor.view.StatusView
	 * @see com.clustercontrol.monitor.view.StatusView#update()
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		this.window = HandlerUtil.getActiveWorkbenchWindow(event);
		// In case this action has been disposed
		if( null == this.window || !isEnabled() ){
			return null;
		}

		// 選択アイテムの取得
		this.viewPart = HandlerUtil.getActivePart(event);
		ScopeListBaseView view = null;
		try {
			view = (StatusView) this.viewPart.getAdapter(StatusView.class);
		} catch (Exception e) { 
			m_log.info("execute " + e.getMessage()); 
			return null; 
		}

		if (view == null) {
			m_log.info("execute: view is null"); 
			return null;
		}

		StatusListComposite composite = (StatusListComposite) view.getListComposite();
		WidgetTestUtil.setTestId(this, null, composite);
		StructuredSelection  selection = (StructuredSelection) composite.getTableViewer().getSelection();

		List<?> list = (ArrayList<?>) selection.getFirstElement();
		
		if (list == null){
			return null;
		}
		
		String managerName = "";
		String pluginId = "";
		String monitorId = "";

		managerName = (String) list.get(GetStatusListTableDefine.MANAGER_NAME);
		pluginId = (String) list.get(GetStatusListTableDefine.PLUGIN_ID);
		if (pluginId == null)
			throw new InternalError("pluginId is null.");

		monitorId = (String) list.get(GetStatusListTableDefine.MONITOR_ID);

		if(monitorId != null){
			// ダイアログ名を取得
			MonitorModifyAction mmAction = new MonitorModifyAction();
			// ダイアログにて変更が選択された場合、入力内容をもって登録を行う。
			if (mmAction.dialogOpen(composite.getShell(), managerName, pluginId, monitorId) == IDialogConstants.OK_ID) {
				composite.update();
			}
		}
		return null;
	}

	@Override
	public void updateElement(UIElement element, @SuppressWarnings("rawtypes") Map parameters) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		// page may not start at state restoring
		if( null != window ){
			IWorkbenchPage page = window.getActivePage();
			if( null != page ){
				IWorkbenchPart part = page.getActivePart();

				boolean editEnable = false;
				if(part instanceof StatusView){
					// Enable button when 1 item is selected
					StatusView view = (StatusView)part;

					if(HinemosModuleConstant.JOB.equals(view.getPluginId())) {
						editEnable = false;
					} else if(view.getRowNum() == 1 && HinemosModuleConstant.isExist(view.getPluginId())){
						editEnable = true;
					}
				}
				this.setBaseEnabled(editEnable);
			} else {
				this.setBaseEnabled(false);
			}
		}
	}
}
