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
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gergely Mathe
 */
@Component(
	property = {"cloning.step.priority=30"},
	service = {CloningStep.class, StagingDataCloningUpdater.class}
)
public class StagingDataCloningUpdater extends BaseCloningUpdater {

	@Override
	protected void doExecute() throws Exception {
		if (!CloningPropsValues.
				STAGING_DATA_CLONING_UPDATER_UPDATE_STAGING_DATA) {

			return;
		}

		final String[] oldRemoteHosts =
			CloningPropsValues.
				STAGING_DATA_CLONING_UPDATER_OLD_REMOTE_HOSTS;

		ActionableDynamicQuery groupActionableDynamicQuery =
			_groupLocalService.getActionableDynamicQuery();

		groupActionableDynamicQuery.setInterval(BATCH_SIZE);

		groupActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<Group>() {

				@Override
				public void performAction(Group group) throws PortalException {
					UnicodeProperties typeSettingsProperties =
						group.getTypeSettingsProperties();

					String remoteAddress = typeSettingsProperties.getProperty(
						"remoteAddress");

					if (Validator.isNull(remoteAddress)) {
						return;
					}

					for (String oldRemoteHost : oldRemoteHosts) {
						if (!remoteAddress.equals(oldRemoteHost)) {
							continue;
						}

						Filter filter = new Filter(oldRemoteHost);

						String oldRemotePort = PropsUtil.get(
							CloningPropsValues.
								STAGING_DATA_CLONING_UPDATER_OLD_REMOTE_PORT,
							filter);

						String remotePort = typeSettingsProperties.getProperty(
							"remotePort");

						if (!remotePort.equals(oldRemotePort)) {
							continue;
						}

						String oldRemoteGroupId = PropsUtil.get(
							CloningPropsValues.
								STAGING_DATA_CLONING_UPDATER_OLD_REMOTE_GROUPID,
							filter);

						String remoteGroupId =
							typeSettingsProperties.getProperty("remoteGroupId");

						if (!remoteGroupId.equals(oldRemoteGroupId)) {
							continue;
						}

						String newRemoteHost = PropsUtil.get(
							CloningPropsValues.
								STAGING_DATA_CLONING_UPDATER_NEW_REMOTE_HOST,
							filter);

						String newRemotePort = PropsUtil.get(
							CloningPropsValues.
								STAGING_DATA_CLONING_UPDATER_NEW_REMOTE_PORT,
							filter);

						String newRemoteGroupId = PropsUtil.get(
							CloningPropsValues.
								STAGING_DATA_CLONING_UPDATER_NEW_REMOTE_GROUPID,
							filter);

						typeSettingsProperties.setProperty(
							"remoteAddress", newRemoteHost);
						typeSettingsProperties.setProperty(
							"remotePort", newRemotePort);
						typeSettingsProperties.setProperty(
							"remoteGroupId", newRemoteGroupId);

						_groupLocalService.updateGroup(
							group.getGroupId(),
							typeSettingsProperties.toString());
					}
				}

			});

		groupActionableDynamicQuery.performActions();
	}

	@Reference(unbind = "-")
	protected void setGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	private GroupLocalService _groupLocalService;

}
