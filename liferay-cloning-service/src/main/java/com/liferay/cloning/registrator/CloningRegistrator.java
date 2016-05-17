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

package com.liferay.cloning.registrator;

import com.liferay.cloning.database.CloningDatabase;
import com.liferay.cloning.updater.PasswordCloningUpdater;
import com.liferay.cloning.updater.PasswordPolicyCloningUpdater;
import com.liferay.cloning.updater.UserDataCloningUpdater;
import com.liferay.cloning.updater.VirtualHostCloningUpdater;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

/**
 * @author Gergely Mathe
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class CloningRegistrator implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register(
			"com.liferay.cloning", "0.0.0", "0.0.1",
			new CloningDatabase());
	}

	@Override
	public void register(Registry registry) {
		registry.register(
			"com.liferay.cloning", "0.0.1", "0.0.2",
			new VirtualHostCloningUpdater());
	}

	@Override
	public void register(Registry registry) {
		registry.register(
			"com.liferay.cloning", "0.0.2", "0.0.3",
			new PasswordCloningUpdater(), new PasswordPolicyCloningUpdater());
	}

	@Override
	public void register(Registry registry) {
		registry.register(
			"com.liferay.cloning", "0.0.2", "0.0.3",
			new UserDataCloningUpdater());
	}

}