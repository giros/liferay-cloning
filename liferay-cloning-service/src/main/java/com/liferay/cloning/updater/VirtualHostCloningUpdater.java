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
import com.liferay.portal.kernel.model.VirtualHost;
import com.liferay.portal.kernel.service.VirtualHostLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gergely Mathe
 */
@Component(
	service = {VirtualHostCloningUpdater.class}
)
public class VirtualHostCloningUpdater extends BaseCloningUpdater {

	@Override
	protected void doClone() throws Exception {
		if (!CloningPropsValues.
				VIRTUAL_HOST_CLONING_UPDATER_UPDATE_VIRTUAL_HOSTS) {

			return;
		}

		String[] oldVirtualHosts =
			CloningPropsValues.VIRTUAL_HOST_CLONING_UPDATER_OLD_VIRTUAL_HOSTS;
		String[] newVirtualHosts =
			CloningPropsValues.VIRTUAL_HOST_CLONING_UPDATER_NEW_VIRTUAL_HOSTS;

		for (int i = 0; i < oldVirtualHosts.length; i++) {
			VirtualHost virtualHost =
				_virtualHostLocalService.fetchfetchVirtualHost(
					oldVirtualHosts[i]);

			if (virtualHost == null) {
				continue;
			}

			_virtualHostLocalService.updateVirtualHost(
				virtualHost.getCompanyId(), virtualHost.getLayoutSetId(),
				newVirtualHosts[i]);
		}
	}

	@Reference(unbind = "-")
	protected void setVirtualHostLocalService(
		VirtualHostLocalService virtualHostLocalService) {

		_virtualHostLocalService = virtualHostLocalService;
	}

	private VirtualHostLocalService _virtualHostLocalService;

}
