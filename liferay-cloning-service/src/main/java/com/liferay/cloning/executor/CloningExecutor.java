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

import com.liferay.cloning.api.CloningPropsValues;
import com.liferay.cloning.api.CloningStep;
import com.liferay.portal.instance.lifecycle.BasePortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;

import java.util.Collection;
import java.util.Iterator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gergely Mathe
 */
@Component(immediate = true, service = PortalInstanceLifecycleListener.class)
public class CloningExecutor extends BasePortalInstanceLifecycleListener {

	@Override
	public void portalInstanceRegistered(Company company) throws Exception {
		try {
			executeCloningSteps();

			System.out.println("\nCompleted Liferay instance cloning.");
		}
		catch (Exception e) {
			e.printStackTrace();

			System.exit(1);
		}
	}

	protected static void executeCloningSteps() throws Exception {
		Registry registry = RegistryUtil.getRegistry();

		for (int i = 0; i <= CloningPropsValues.CLONING_STEPS_MAX_NUMBER; i++) {
			Collection<ServiceReference<CloningStep>> serviceReferences =
				registry.getServiceReferences(
					CloningStep.class, "(cloning.step.priority=" + i + ")");

			if (serviceReferences.isEmpty()) {
				continue;
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
		}
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

}
