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
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;

import org.osgi.service.component.annotations.Component;

/**
 * @author Gergely Mathe
 */
@Component(
	service = {UserDataCloningUpdater.class}
)
public class UserDataCloningUpdater extends BaseCloningUpdater {

	@Override
	protected void doClone() throws Exception {
		if (!CloningPropsValues.USER_DATA_CLONING_UPDATER_UPDATE_USER_DATA) {
			return;
		}

		ActionableDynamicQuery userActionableDynamicQuery =
			_userLocalService.getActionableDynamicQuery();

		userActionableDynamicQuery.setInterval(BATCH_SIZE);

		userActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<User>() {

				@Override
				public void performAction(User user) throws PortalException {
					
				}

			});

		userActionableDynamicQuery.performActions();
	}

}
