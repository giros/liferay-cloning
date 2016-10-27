/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.cloning.executor;

import com.liferay.cloning.api.CloningConstants;
import com.liferay.cloning.api.CloningException;
import com.liferay.cloning.api.CloningStep;
import com.liferay.cloning.configuration.CloningConfiguration;
import com.liferay.portal.instance.lifecycle.BasePortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.service.PortalPreferencesLocalService;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;

import aQute.bnd.annotation.metatype.Configurable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gergely Mathe
 */
@Component(
	immediate = true,
	service = PortalInstanceLifecycleListener.class,
	configurationPid = "com.liferay.cloning.configuration.CloningConfiguration"
)
public class CloningExecutor extends BasePortalInstanceLifecycleListener {

	@Override
	public void portalInstanceRegistered(Company company) throws Exception {
		PortletPreferences portalPreferences =
			_portalPreferencesLocalService.getPreferences(
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_COMPANY);

		String statusKey =
			CloningExecutor.class.getName() + CloningConstants.STATUS_POSTFIX;

		try {
			String statusValue = portalPreferences.getValue(statusKey, "");

			if (StringUtil.equalsIgnoreCase(
					statusValue, CloningConstants.STATUS_COMPLETE) &&
				!_cloningConfiguration.forcedExecution()) {

				_log.info(
					"The Cloning Tool has successfully completed earlier," +
					"so it will not run again. If you want to force it to run" +
					"use the configuration option forcedExecution=true");

				return;
			}

			executeCloningSteps();

			portalPreferences.setValue(
				statusKey, CloningConstants.STATUS_COMPLETE);

			portalPreferences.store();

			_log.info("Completed Liferay instance cloning.");
		}
		catch (Exception e) {
			portalPreferences.setValue(
				statusKey, CloningConstants.STATUS_FAILED);

			portalPreferences.store();

			_log.error(e.getMessage(), e);
			_log.error("Cloning Tool FAILED!");
		}
	}

	protected static void executeCloningSteps() throws Exception {
		Registry registry = RegistryUtil.getRegistry();

		for (int i = 0; i <= _cloningConfiguration.cloningStepsMaxNumber(); i++) {

			Collection<ServiceReference<CloningStep>> serviceReferences =
				registry.getServiceReferences(
					CloningStep.class, "(cloning.step.priority=" + i + ")");

			if (serviceReferences.isEmpty()) {
				continue;
			}

			Iterator<ServiceReference<CloningStep>> iterator =
				serviceReferences.iterator();

			while (iterator.hasNext()) {
				ServiceReference<CloningStep> serviceReference =
					iterator.next();

				CloningStep cloningStep = (CloningStep)registry.getService(
					serviceReference);

				try {
					cloningStep.execute();
				}
				catch (CloningException ce) {
					_log.error(ce.getMessage(), ce);
				}
			}
		}
	}

	@Activate
	@Modified
	protected void readConfiguration(Map<String, Object> properties) {
		_cloningConfiguration = Configurable.createConfigurable(
			CloningConfiguration.class, properties);
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	@Reference(unbind = "-")
	protected void setPortalPreferencesLocalService(
		PortalPreferencesLocalService portalPreferencesLocalService) {

		_portalPreferencesLocalService = portalPreferencesLocalService;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CloningExecutor.class);

	private static CloningConfiguration _cloningConfiguration;
	private PortalPreferencesLocalService _portalPreferencesLocalService;

}
