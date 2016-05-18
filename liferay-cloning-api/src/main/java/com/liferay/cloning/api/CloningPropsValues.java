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

package com.liferay.cloning.api;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.PropsUtil;

/**
 * @author Gergely Mathe
 */
public class CloningPropsValues {

	public static final String PASSWORD_CLONING_UPDATER_NEW_PASSWORD = PropsUtil.get(CloningPropsKeys.PASSWORD_CLONING_UPDATER_NEW_PASSWORD);

	public static final boolean PASSWORD_CLONING_UPDATER_UPDATE_PASSWORDS = GetterUtil.getBoolean(PropsUtil.get(CloningPropsKeys.PASSWORD_CLONING_UPDATER_UPDATE_PASSWORDS));

	public static final String[] PASSWORD_CLONING_UPDATER_USER_IDS = StringUtil.splitLines(PropsUtil.get(CloningPropsKeys.PASSWORD_CLONING_UPDATER_USER_IDS));

	public static final boolean PASSWORD_POLICY_CLONING_UPDATER_DELETE_PASSWORD_POLICIES = GetterUtil.getBoolean(PropsUtil.get(CloningPropsKeys.PASSWORD_POLICY_CLONING_UPDATER_DELETE_PASSWORD_POLICIES));

	public static final boolean USER_DATA_CLONING_UPDATER_UPDATE_USER_DATA = GetterUtil.getBoolean(PropsUtil.get(CloningPropsKeys.USER_DATA_CLONING_UPDATER_UPDATE_USER_DATA));

}
