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

package src.main.java.com.liferay.cloning.updater;

import com.liferay.cloning.api.CloningPropsValues;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gergely Mathe
 */
@Component(
	service = {StagingDataCloningUpdater.class}
)
public class StagingDataCloningUpdater extends BaseCloningUpdater {

	@Override
	protected void doClone() throws Exception {
		if (!CloningPropsValues.
				STAGING_DATA_CLONING_UPDATER_UPDATE_STAGING_DATA) {

			return;
		}

		String[] oldRemoteConnections =
			CloningPropsValues.
				STAGING_DATA_CLONING_UPDATER_OLD_REMOTE_CONNECTIONS;

		String[] newRemoteConnections =
			CloningPropsValues.
				STAGING_DATA_CLONING_UPDATER_NEW_REMOTE_CONNECTIONS;

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

					for (int i = 0; i < oldRemoteConnections.length; i++) {
						String[] oldRemoteConnection = StringUtil.split(
							oldRemoteConnections[i], StringPool.POUND);

						if (!remoteAddress.equals(oldRemoteConnection[0])) {
							continue;
						}

						String[] newRemoteConnection = StringUtil.split(
							newRemoteConnections[i], StringPool.POUND);

						typeSettingsProperties.setProperty(
							"remoteAddress", newRemoteConnection[0]);
						typeSettingsProperties.setProperty(
							"remotePort", newRemoteConnection[1]);
						typeSettingsProperties.setProperty(
							"remoteGroupId", newRemoteConnection[2]);

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
