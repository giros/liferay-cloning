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

import com.liferay.cloning.api.CloningStep;
import com.liferay.cloning.updater.PasswordCloningUpdater;
import com.liferay.cloning.updater.PasswordPolicyCloningUpdater;
import com.liferay.cloning.updater.StagingDataCloningUpdater;
import com.liferay.cloning.updater.UserDataCloningUpdater;
import com.liferay.cloning.updater.VirtualHostCloningUpdater;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.util.InitUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.time.StopWatch;

import org.osgi.framework.ServiceReference;

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
		Registry registry = RegistryUtil.getRegistry();

		int i = 0;

		while (true) {
			Collection<ServiceReference<CloningStep>> serviceReferences =
				registry.getServiceReferences(
					CloningStep.class, "cloning.step.priority=" + i);

			if (serviceReferences.isEmpty()) {
				break;
			}

			Iterator<ServiceReference<CloningStep>> iterator =
				serviceReferences.iterator();

			while (iterator.hasNext()) {
				ServiceReference<CloningStep> serviceReference =
					iterator.next();

				CloningStep cloningStep = (CloningStep)registry.getService(
					serviceReference);

				cloningStep.execute();
			}

			i++;
		}
	}

}
