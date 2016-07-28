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

import com.liferay.cloning.api.CloningStep;
import com.liferay.cloning.configuration.CloningConfigurationValues;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.RequiredPasswordPolicyException;
import com.liferay.portal.kernel.model.PasswordPolicy;
import com.liferay.portal.kernel.service.PasswordPolicyLocalService;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gergely Mathe
 */
@Component(
	property = {"cloning.step.priority=20"},
	service = {CloningStep.class, PasswordPolicyCloningUpdater.class}
)
public class PasswordPolicyCloningUpdater extends BaseCloningUpdater {

	@Override
	protected void doExecute() throws Exception {
		if (!CloningConfigurationValues.
				PASSWORD_POLICY_CLONING_UPDATER_DELETE_PASSWORD_POLICIES) {

			return;
		}

		List<PasswordPolicy> passwordPolicies =
			_passwordPolicyLocalService.getPasswordPolicies(
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (PasswordPolicy passwordPolicy : passwordPolicies) {
			try {
				_passwordPolicyLocalService.deletePasswordPolicy(
					passwordPolicy);
			}
			catch (RequiredPasswordPolicyException rppe) {
			}
		}

		System.out.println("\nCompleted PasswordPolicyCloningUpdater.");
	}

	@Reference(unbind = "-")
	protected void setPasswordPolicyLocalService(
		PasswordPolicyLocalService passwordPolicyLocalService) {

		_passwordPolicyLocalService = passwordPolicyLocalService;
	}

	private PasswordPolicyLocalService _passwordPolicyLocalService;

}
