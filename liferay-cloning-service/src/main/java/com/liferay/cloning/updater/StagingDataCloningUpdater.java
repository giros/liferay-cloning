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
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.UnicodeProperties;
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
	property = {"cloning.step.priority=30"},
	service = {CloningStep.class, StagingDataCloningUpdater.class},
	configurationPid = "com.liferay.cloning.configuration.CloningConfiguration"
)
public class StagingDataCloningUpdater extends BaseCloningUpdater {

	@Override
	protected void doExecute() throws Exception {
		if (!_cloningConfiguration.
				stagingDataCloningUpdaterUpdateStagingData()) {

			return;
		}

		String[] oldRemoteHosts =
			_cloningConfiguration.stagingDataCloningUpdaterOldRemoteHosts();

		long[] oldRemotePorts =
			_cloningConfiguration.stagingDataCloningUpdaterOldRemotePorts();

		long[] oldRemoteGroupIds =
			_cloningConfiguration.stagingDataCloningUpdaterOldRemoteGroupIds();

		String[] newRemoteHosts =
			_cloningConfiguration.stagingDataCloningUpdaterNewRemoteHosts();

		long[] newRemotePorts =
			_cloningConfiguration.stagingDataCloningUpdaterNewRemotePorts();

		long[] newRemoteGroupIds =
			_cloningConfiguration.stagingDataCloningUpdaterNewRemoteGroupIds();

		if (oldRemoteHosts == null) {
			return;
		}

		if ((oldRemoteHosts.length != newRemoteHosts.length) ||
			(oldRemotePorts.length != newRemotePorts.length) ||
			(oldRemoteGroupIds.length != newRemoteGroupIds.length)) {

			throw new CloningException(
				"The number of old and new remote hosts/ports/groupIds are " +
				"different.");
		}

		ActionableDynamicQuery groupActionableDynamicQuery =
			_groupLocalService.getActionableDynamicQuery();

		groupActionableDynamicQuery.setInterval(
			_cloningConfiguration.baseCloningUpdaterBatchSize());

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

					for (int i = 0; i < oldRemoteHosts.length; i++) {
						String oldRemoteHost = oldRemoteHosts[i];

						if (!remoteAddress.equals(oldRemoteHost)) {
							continue;
						}

						String oldRemotePort = String.valueOf(
							oldRemotePorts[i]);

						String remotePort = typeSettingsProperties.getProperty(
							"remotePort");

						if (!remotePort.equals(oldRemotePort)) {
							continue;
						}

						String oldRemoteGroupId = String.valueOf(
							oldRemoteGroupIds[i]);

						String remoteGroupId =
							typeSettingsProperties.getProperty("remoteGroupId");

						if (!remoteGroupId.equals(oldRemoteGroupId)) {
							continue;
						}

						String newRemoteHost = newRemoteHosts[i];

						String newRemotePort = String.valueOf(
							newRemotePorts[i]);

						String newRemoteGroupId = String.valueOf(
							newRemoteGroupIds[i]);

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

		_log.info("Completed StagingDataCloningUpdater.");
	}

	@Activate
	@Modified
	protected void readConfiguration(Map<String, Object> properties) {
		_cloningConfiguration = Configurable.createConfigurable(
			CloningConfiguration.class, properties);
	}

	@Reference(unbind = "-")
	protected void setGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		StagingDataCloningUpdater.class);

	private static CloningConfiguration _cloningConfiguration;
	private GroupLocalService _groupLocalService;

}
