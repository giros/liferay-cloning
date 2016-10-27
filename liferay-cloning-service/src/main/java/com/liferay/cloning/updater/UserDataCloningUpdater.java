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
import com.liferay.cloning.configuration.CloningConfiguration;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.AddressLocalService;
import com.liferay.portal.kernel.service.ContactLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;

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
	property = {"cloning.step.priority=40"},
	service = {CloningStep.class, UserDataCloningUpdater.class},
	configurationPid = "com.liferay.cloning.configuration.CloningConfiguration"
)
public class UserDataCloningUpdater extends BaseCloningUpdater {

	@Override
	protected void doExecute() throws Exception {
		if (!_cloningConfiguration.userDataCloningUpdaterUpdateUserData()) {
			return;
		}

		ActionableDynamicQuery userActionableDynamicQuery =
			_userLocalService.getActionableDynamicQuery();

		userActionableDynamicQuery.setInterval(
			_cloningConfiguration.baseCloningUpdaterBatchSize());

		userActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<User>() {

				@Override
				public void performAction(User user) throws PortalException {
					_userLocalService.deletePortrait(user.getUserId());

					List<Contact> contacts = _contactLocalService.getContacts(
						PortalUtil.getClassNameId(User.class.getName()),
						user.getUserId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS,
						null);

					for (Contact contact : contacts) {
						_contactLocalService.updateContact(
							contact.getContactId(), contact.getEmailAddress(),
							contact.getFirstName(), contact.getMiddleName(),
							contact.getLastName(), contact.getPrefixId(),
							contact.getSuffixId(),
							contact.getMale(), 0, 1, 1970,
							StringPool.BLANK, StringPool.BLANK,
							StringPool.BLANK, StringPool.BLANK,
							StringPool.BLANK, contact.getJobTitle());

						_addressLocalService.deleteAddresses(
							contact.getCompanyId(), Contact.class.getName(),
							contact.getContactId());
					}
				}

			});

		userActionableDynamicQuery.performActions();

		_log.info("Completed UserDataCloningUpdater.");
	}

	@Activate
	@Modified
	protected void readConfiguration(Map<String, Object> properties) {
		_cloningConfiguration = Configurable.createConfigurable(
			CloningConfiguration.class, properties);
	}

	@Reference(unbind = "-")
	protected void setAddressLocalService(
		AddressLocalService addressLocalService) {

		_addressLocalService = addressLocalService;
	}

	@Reference(unbind = "-")
	protected void setContactLocalService(
		ContactLocalService contactLocalService) {

		_contactLocalService = contactLocalService;
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UserDataCloningUpdater.class);

	private static CloningConfiguration _cloningConfiguration;
	private AddressLocalService _addressLocalService;
	private ContactLocalService _contactLocalService;
	private UserLocalService _userLocalService;

}
