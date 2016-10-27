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
import com.liferay.cloning.executor.CloningExecutor;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;

import aQute.bnd.annotation.metatype.Configurable;

import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gergely Mathe
 */
@Component(
	property = {"cloning.step.priority=10"},
	service = {CloningStep.class, PasswordCloningUpdater.class},
	configurationPid = "com.liferay.cloning.configuration.CloningConfiguration"
)
public class PasswordCloningUpdater extends BaseCloningUpdater {

	@Override
	protected void doExecute() throws Exception {
		if (!_cloningConfiguration.passwordCloningUpdaterUpdatePasswords()) {
			return;
		}

		String newPassword =
			_cloningConfiguration.passwordCloningUpdaterNewPassword();

		if (Validator.isNull(newPassword)) {
			throw new CloningException("New password is not defined.");
		}

		long[] userIds = _cloningConfiguration.passwordCloningUpdaterUserIds();

		ActionableDynamicQuery userActionableDynamicQuery =
			_userLocalService.getActionableDynamicQuery();

		userActionableDynamicQuery.setInterval(
			_cloningConfiguration.baseCloningUpdaterBatchSize());

		userActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<User>() {

				@Override
				public void performAction(User user) throws PortalException {
					if (ArrayUtil.isEmpty(userIds) ||
						ArrayUtil.contains(userIds, user.getUserId())) {

						_userLocalService.updatePassword(
							user.getUserId(), newPassword, newPassword, false,
							true);
					}
				}
			});

		userActionableDynamicQuery.performActions();

		_log.info("Completed PasswordCloningUpdater.");
	}

	@Activate
	@Modified
	protected void readConfiguration(Map<String, Object> properties) {
		_cloningConfiguration = Configurable.createConfigurable(
			CloningConfiguration.class, properties);
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PasswordCloningUpdater.class);

	private CloningConfiguration _cloningConfiguration;
	private UserLocalService _userLocalService;

}
