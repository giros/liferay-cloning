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

package com.liferay.cloning.updater;

import com.liferay.cloning.api.CloningException;
import com.liferay.cloning.api.CloningStep;
import com.liferay.cloning.configuration.CloningConfiguration;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.VirtualHost;
import com.liferay.portal.kernel.service.VirtualHostLocalService;
import com.liferay.portal.kernel.util.Validator;

import aQute.bnd.annotation.metatype.Configurable;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gergely Mathe
 */
@Component(
	property = {"cloning.step.priority=50"},
	service = {CloningStep.class, VirtualHostCloningUpdater.class},
	configurationPid = "com.liferay.cloning.configuration.CloningConfiguration"
)
public class VirtualHostCloningUpdater extends BaseCloningUpdater {

	@Override
	protected void doExecute() throws Exception {
		if (!_cloningConfiguration.
				virtualHostCloningUpdaterUpdateVirtualHosts()) {

			return;
		}

		String[] oldVirtualHosts =
			_cloningConfiguration.virtualHostCloningUpdaterOldVirtualHosts();

		String[] newVirtualHosts =
			_cloningConfiguration.virtualHostCloningUpdaterNewVirtualHosts();

		if (oldVirtualHosts.length != newVirtualHosts.length) {
			throw new CloningException(
				"The number of oldVirtualHosts and newVirtualHosts are " +
				"different.");
		}

		for (int i = 0; i < oldVirtualHosts.length; i++) {
			VirtualHost virtualHost = _virtualHostLocalService.fetchVirtualHost(
				oldVirtualHosts[i]);

			if (virtualHost == null) {
				continue;
			}

			String newVirtualHost = newVirtualHosts[i];

			if (Validator.isNull(newVirtualHost)) {
				continue;
			}

			_virtualHostLocalService.updateVirtualHost(
				virtualHost.getCompanyId(), virtualHost.getLayoutSetId(),
				newVirtualHost);
		}

		_log.info("Completed VirtualHostCloningUpdater.");
	}

	@Activate
	@Modified
	protected void readConfiguration(Map<String, Object> properties) {
		_cloningConfiguration = Configurable.createConfigurable(
			CloningConfiguration.class, properties);
	}

	@Reference(unbind = "-")
	protected void setVirtualHostLocalService(
		VirtualHostLocalService virtualHostLocalService) {

		_virtualHostLocalService = virtualHostLocalService;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		VirtualHostCloningUpdater.class);

	private static CloningConfiguration _cloningConfiguration;
	private VirtualHostLocalService _virtualHostLocalService;

}
