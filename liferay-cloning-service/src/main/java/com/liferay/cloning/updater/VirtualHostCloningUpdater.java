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

import com.liferay.cloning.api.CloningPropsValues;
import com.liferay.cloning.api.CloningStep;
import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.model.VirtualHost;
import com.liferay.portal.kernel.service.VirtualHostLocalService;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gergely Mathe
 */
@Component(
	property = {"cloning.step.priority=50"},
	service = {CloningStep.class, VirtualHostCloningUpdater.class}
)
public class VirtualHostCloningUpdater extends BaseCloningUpdater {

	@Override
	protected void doExecute() throws Exception {
		if (!CloningPropsValues.
				VIRTUAL_HOST_CLONING_UPDATER_UPDATE_VIRTUAL_HOSTS) {

			return;
		}

		String[] oldVirtualHosts =
			CloningPropsValues.VIRTUAL_HOST_CLONING_UPDATER_OLD_VIRTUAL_HOSTS;

		for (String oldVirtualHost : oldVirtualHosts) {
			VirtualHost virtualHost = _virtualHostLocalService.fetchVirtualHost(
				oldVirtualHost);

			if (virtualHost == null) {
				continue;
			}

			Filter filter = new Filter(oldVirtualHost);

			String newVirtualHost = PropsUtil.get(
				CloningPropsValues.
					VIRTUAL_HOST_CLONING_UPDATER_NEW_VIRTUAL_HOST,
				filter);

			if (Validator.isNull(newVirtualHost)) {
				continue;
			}

			_virtualHostLocalService.updateVirtualHost(
				virtualHost.getCompanyId(), virtualHost.getLayoutSetId(),
				newVirtualHost);
		}

		System.out.println("\nCompleted VirtualHostCloningUpdater.");
	}

	@Reference(unbind = "-")
	protected void setVirtualHostLocalService(
		VirtualHostLocalService virtualHostLocalService) {

		_virtualHostLocalService = virtualHostLocalService;
	}

	private VirtualHostLocalService _virtualHostLocalService;

}
