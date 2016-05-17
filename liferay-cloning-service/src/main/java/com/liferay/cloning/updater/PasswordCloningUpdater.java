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
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.UserLocalService;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gergely Mathe
 */
@Component(
	service = {PasswordCloningUpdater.class}
)
public class PasswordCloningUpdater extends BaseCloningUpdater {

	@Override
	protected void doClone() throws Exception {
		readProperties();

		List<Company> companies = _companyLocalService.getCompanies();

		for (Company company : companies) {
			updateUserPasswords(company);
		}
	}

	protected void updateUserPasswords(Company company) {
		int usersCount = _userLocalService.getCompanyUsersCount(
			company.getCompanyId());

		int start = 0;
		int end = BATCH_SIZE;

		while (start < usersCount) {
			List<User> users = _userLocalService.getCompanyUsers(
				company.getCompanyId(), start, end);

			for (User user : users) {
				_userLocalService.updatePassword(
					user.getUserId(), newPassword, newPassword, false, true);
			}

			start += BATCH_SIZE;
			end = start + BATCH_SIZE;
		}
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(
		CompanyLocalService companyLocalService) {

		_companyLocalService = companyLocalService;
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	private CompanyLocalService _companyLocalService;
	private UserLocalService _userLocalService;

}
