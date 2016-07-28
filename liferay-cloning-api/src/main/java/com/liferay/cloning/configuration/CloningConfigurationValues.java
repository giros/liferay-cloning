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

package com.liferay.cloning.configuration;

import com.liferay.portal.kernel.util.GetterUtil;

/**
* @author Gergely Mathe
*/
public class CloningConfigurationValues {

	public static final int CLONING_STEPS_MAX_NUMBER = GetterUtil.getInteger(
		CloningConfigurationUtil.get("cloning.steps.max.number"));

	public static final String PASSWORD_CLONING_UPDATER_NEW_PASSWORD =
		CloningConfigurationUtil.get("password.cloning.updater.new.password");

	public static final boolean PASSWORD_CLONING_UPDATER_UPDATE_PASSWORDS =
		GetterUtil.getBoolean(
			CloningConfigurationUtil.get(
				"password.cloning.updater.update.passwords"));

	public static final String[] PASSWORD_CLONING_UPDATER_USER_IDS = 
		CloningConfigurationUtil.getArray("password.cloning.updater.user.ids");

	public static final boolean
		PASSWORD_POLICY_CLONING_UPDATER_DELETE_PASSWORD_POLICIES =
			GetterUtil.getBoolean(
				CloningConfigurationUtil.get(
					"password.policy.cloning.updater.delete.password.policies"));

	public static final String STAGING_DATA_CLONING_UPDATER_NEW_REMOTE_GROUPID =
		CloningConfigurationUtil.get(
			"staging.data.cloning.updater.new.remote.groupid");

	public static final String STAGING_DATA_CLONING_UPDATER_NEW_REMOTE_HOST =
		CloningConfigurationUtil.get(
			"staging.data.cloning.updater.new.remote.host");

	public static final String STAGING_DATA_CLONING_UPDATER_NEW_REMOTE_PORT =
		CloningConfigurationUtil.get(
			"staging.data.cloning.updater.new.remote.port");

	public static final String STAGING_DATA_CLONING_UPDATER_OLD_REMOTE_GROUPID =
		CloningConfigurationUtil.get(
			"staging.data.cloning.updater.old.remote.groupid");

	public static final String[] STAGING_DATA_CLONING_UPDATER_OLD_REMOTE_HOSTS =
		CloningConfigurationUtil.getArray(
			"staging.data.cloning.updater.old.remote.hosts");

	public static final String STAGING_DATA_CLONING_UPDATER_OLD_REMOTE_PORT =
		CloningConfigurationUtil.get(
			"staging.data.cloning.updater.old.remote.port");

	public static final boolean
		STAGING_DATA_CLONING_UPDATER_UPDATE_STAGING_DATA =
			GetterUtil.getBoolean(
				CloningConfigurationUtil.get(
					"staging.data.cloning.updater.update.staging.data"));

	public static final boolean USER_DATA_CLONING_UPDATER_UPDATE_USER_DATA =
		GetterUtil.getBoolean(
			CloningConfigurationUtil.get(
				"user.data.cloning.updater.update.user.data"));

	public static final String VIRTUAL_HOST_CLONING_UPDATER_NEW_VIRTUAL_HOST =
		CloningConfigurationUtil.get(
			"virtual.host.cloning.updater.new.virtual.host");

	public static final String[]
		VIRTUAL_HOST_CLONING_UPDATER_OLD_VIRTUAL_HOSTS =
			CloningConfigurationUtil.getArray(
				"virtual.host.cloning.updater.old.virtual.hosts");

	public static final boolean
		VIRTUAL_HOST_CLONING_UPDATER_UPDATE_VIRTUAL_HOSTS =
			GetterUtil.getBoolean(
				CloningConfigurationUtil.get(
					"virtual.host.cloning.updater.update.virtual.hosts"));

}
