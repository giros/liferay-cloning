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
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gergely Mathe
 */
@Component(
	property = {"cloning.step.priority=0"},
	service = {CloningStep.class, PasswordCloningUpdater.class}
)
public class PasswordCloningUpdater extends BaseCloningUpdater {

	@Override
	protected void doExecute() throws Exception {
		if (!CloningPropsValues.PASSWORD_CLONING_UPDATER_UPDATE_PASSWORDS) {
			return;
		}

		String newPassword =
			CloningPropsValues.PASSWORD_CLONING_UPDATER_NEW_PASSWORD;

		List<String> userIds = ListUtil.toList(
			CloningPropsValues.PASSWORD_CLONING_UPDATER_USER_IDS); 

		for (String userId : userIds) {
			User user = _userLocalService.fetchUserById(Long.valueOf(userId));

			_userLocalService.updatePassword(
				user.getUserId(), newPassword, newPassword, false, true);
		}
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	private UserLocalService _userLocalService;

}
