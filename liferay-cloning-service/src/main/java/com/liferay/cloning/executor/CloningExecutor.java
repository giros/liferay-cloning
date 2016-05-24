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

package com.liferay.cloning.executor;

import com.liferay.cloning.updater.PasswordCloningUpdater;
import com.liferay.cloning.updater.PasswordPolicyCloningUpdater;
import com.liferay.cloning.updater.StagingDataCloningUpdater;
import com.liferay.cloning.updater.UserDataCloningUpdater;
import com.liferay.cloning.updater.VirtualHostCloningUpdater;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.util.InitUtil;

import org.apache.commons.lang.time.StopWatch;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Gergely Mathe
 */
public class CloningExecutor {

	public static void main(String[] args) {
		try {
			StopWatch stopWatch = new StopWatch();

			stopWatch.start();

			InitUtil.initWithSpring(true, false);

			executeCloningSteps();

			System.out.println(
				"\nCompleted Liferay instance cloning in " +
					(stopWatch.getTime() / Time.SECOND) + " seconds");
		}
		catch (Exception e) {
			e.printStackTrace();

			System.exit(1);
		}
	}

	protected static void executeCloningSteps() throws Exception {
		_passwordCloningUpdater.execute();
		_passwordPolicyCloningUpdater.execute();
		_stagingDataCloningUpdater.execute();
		_userDataCloningUpdater.execute();
		_virtualHostCloningUpdater.execute();
	}

	@Reference(unbind = "-")
	protected void setPasswordCloningUpdater(
		PasswordCloningUpdater passwordCloningUpdater) {

		_passwordCloningUpdater = passwordCloningUpdater;
	}

	@Reference(unbind = "-")
	protected void setPasswordPolicyCloningUpdater(
		PasswordPolicyCloningUpdater passwordPolicyCloningUpdater) {

		_passwordPolicyCloningUpdater = passwordPolicyCloningUpdater;
	}

	@Reference(unbind = "-")
	protected void setStagingDataCloningUpdater(
		StagingDataCloningUpdater stagingDataCloningUpdater) {

		_stagingDataCloningUpdater = stagingDataCloningUpdater;
	}

	@Reference(unbind = "-")
	protected void setUserDataCloningUpdater(
		UserDataCloningUpdater userDataCloningUpdater) {

		_userDataCloningUpdater = userDataCloningUpdater;
	}

	@Reference(unbind = "-")
	protected void setVirtualHostCloningUpdater(
		VirtualHostCloningUpdater virtualHostCloningUpdater) {

		_virtualHostCloningUpdater = virtualHostCloningUpdater;
	}

	private static PasswordCloningUpdater _passwordCloningUpdater;
	private static PasswordPolicyCloningUpdater _passwordPolicyCloningUpdater;
	private static StagingDataCloningUpdater _stagingDataCloningUpdater;
	private static UserDataCloningUpdater _userDataCloningUpdater;
	private static VirtualHostCloningUpdater _virtualHostCloningUpdater;

}
