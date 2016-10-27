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

import aQute.bnd.annotation.metatype.Meta;

/**
 * @author Gergely Mathe
 */
@Meta.OCD(
	id = "com.liferay.cloning.configuration.CloningConfiguration",
	name = "cloning.configuration.name"
)
public interface CloningConfiguration {

	@Meta.AD(deflt = "500", required = false)
	public int baseCloningUpdaterBatchSize();
	
	@Meta.AD(deflt = "100", required = false)
	public int cloningStepsMaxNumber();

	@Meta.AD(deflt = "false", required = false)
	public boolean forcedExecution();

	@Meta.AD(deflt = "true", required = false)
	public boolean passwordCloningUpdaterUpdatePasswords();

	@Meta.AD(required = false)
	public long[] passwordCloningUpdaterUserIds();

	@Meta.AD(required = false)
	public String passwordCloningUpdaterNewPassword();

	@Meta.AD(deflt = "true", required = false)
	public boolean passwordPolicyCloningUpdaterDeletePasswordPolicies();

	@Meta.AD(deflt = "true", required = false)
	public boolean stagingDataCloningUpdaterUpdateStagingData();

	@Meta.AD(required = false)
	public String[] stagingDataCloningUpdaterOldRemoteHosts();

	@Meta.AD(required = false)
	public long[] stagingDataCloningUpdaterOldRemotePorts();

	@Meta.AD(required = false)
	public long[] stagingDataCloningUpdaterOldRemoteGroupIds();

	@Meta.AD(required = false)
	public String[] stagingDataCloningUpdaterNewRemoteHosts();

	@Meta.AD(required = false)
	public long[] stagingDataCloningUpdaterNewRemotePorts();

	@Meta.AD(required = false)
	public long[] stagingDataCloningUpdaterNewRemoteGroupIds();

	@Meta.AD(deflt = "true", required = false)
	public boolean userDataCloningUpdaterUpdateUserData();

	@Meta.AD(deflt = "true", required = false)
	public boolean virtualHostCloningUpdaterUpdateVirtualHosts();

	@Meta.AD(required = false)
	public String[] virtualHostCloningUpdaterOldVirtualHosts();

	@Meta.AD(required = false)
	public String[] virtualHostCloningUpdaterNewVirtualHosts();

}
